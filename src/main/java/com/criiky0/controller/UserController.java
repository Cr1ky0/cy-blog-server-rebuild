package com.criiky0.controller;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.criiky0.pojo.User;
import com.criiky0.pojo.dto.UserDTO;
import com.criiky0.pojo.vo.LoginVo;
import com.criiky0.pojo.vo.RegisterVO;
import com.criiky0.pojo.vo.UpdateEmailVo;
import com.criiky0.pojo.vo.UpdatePswVo;
import com.criiky0.service.UserService;
import com.criiky0.utils.*;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import jakarta.annotation.Resource;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Base64;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/user")
@Slf4j
@Validated
public class UserController {

    private UserService userService;

    private EnvironmentChecker environmentChecker;

    private JwtHelper jwtHelper;

    @Resource
    private HttpServletResponse response;

    @Resource
    DefaultKaptcha defaultKaptcha;

    private static final String AVATAR_DIR = System.getProperty("user.dir") + "/public/avatars";

    @Autowired
    public UserController(UserService userService, EnvironmentChecker environmentChecker, JwtHelper jwtHelper) {
        this.userService = userService;
        this.environmentChecker = environmentChecker;
        this.jwtHelper = jwtHelper;
    }

    // 获取头像util
    private void getCertainUserAvatar(Long userId) throws RuntimeException {
        try (OutputStream os = response.getOutputStream()) {
            User user = userService.getById(userId);
            String avatar = user.getAvatar();
            // 获取后缀
            File file = new File(AVATAR_DIR, avatar);
            String fileName = file.getName();
            int lastDotIndex = fileName.lastIndexOf('.');
            String suffix = fileName.substring(lastDotIndex + 1);
            // 读取图片
            BufferedImage image = ImageIO.read(new FileInputStream(file));
            response.setContentType("image/" + suffix);
            if (image != null) {
                ImageIO.write(image, suffix, os);
            }
        } catch (IOException e) {
            log.error("获取图片异常{}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    // 向指定邮箱发送信息并设置session
    private void sendCodeToEmail(String email, HttpSession session) throws RuntimeException {
        Integer code;
        try {
            code = JavaMailUtil.sendCode(email);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
        HashMap<String, Integer> codeMap = new HashMap<>();
        codeMap.put("code", code);
        // 放入session
        session.setAttribute("verify-code", code);
        session.setAttribute("verify-email", email);
        session.setMaxInactiveInterval(10 * 60); // 10分钟内有效
    }

    /**
     * 发送注册验证码
     * 
     * @param email 发送邮箱
     * @param session session
     * @return
     */
    @GetMapping("code")
    public Result<ResultCodeEnum> code(@RequestParam String email, HttpSession session) {
        // 验证邮箱是否被占用
        boolean exists = userService.exists(new LambdaQueryWrapper<User>().eq(User::getEmail, email));
        if (exists) {
            return Result.build(null, ResultCodeEnum.EMAIL_USED);
        }
        try {
            sendCodeToEmail(email, session);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
        return Result.ok(null);
    }

    /**
     * 获取登录验证码
     * 
     * @param session
     * @return
     * @throws IOException
     */
    @GetMapping("/verify")
    public Result<HashMap<String, String>> getVerificationCode(HttpSession session) throws IOException {
        // 生成文字验证码并放入session
        String text = defaultKaptcha.createText();
        session.setAttribute("login_code", text);
        session.setMaxInactiveInterval(10 * 60); // 10分钟内有效
        if (environmentChecker.isDevelopment()) {
            System.out.println("验证码为：" + text);
        }
        // 生成图片验证码
        ByteArrayOutputStream out = null;
        BufferedImage image = defaultKaptcha.createImage(text);
        out = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", out);
        // 对字节组Base64编码
        HashMap<String, String> map = new HashMap<>();
        String img = "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(out.toByteArray());
        map.put("img", img);
        return Result.ok(map);
    }

    /**
     * 用户登录
     * @param loginVo
     */
    @PostMapping("login")
    public Result<HashMap<String, Long>> login(@RequestBody LoginVo loginVo, HttpSession session) {
        if (environmentChecker.isProduction()) {
            Object code = session.getAttribute("login_code");
            if (code == null || !code.toString().equalsIgnoreCase(loginVo.getVerificationCode())) {
                return Result.build(null, ResultCodeEnum.CODE_ERROR);
            }
        }
        Result result = userService.login(loginVo);
        if (result.getCode() == 200) {
            String token = result.getData().toString();
            Cookie cookie = new Cookie("token", token);
            cookie.setHttpOnly(true);
            cookie.setPath("/api");
            // 生产环境打开HTTPS
            if (environmentChecker.isProduction()) {
                cookie.setSecure(true);
            }
            response.addCookie(cookie);
            // 删除session
            session.removeAttribute("login_code");
            // 添加过期时间
            Long expireTime = jwtHelper.getExpiration(token);
            HashMap<String, Long> map = new HashMap<>();
            map.put("expireTime", expireTime);
            // 添加Session
            session.setAttribute("token",token);
            // 设置过期时间
            int time = (int)(expireTime - System.currentTimeMillis());
            session.setMaxInactiveInterval(time);
            return Result.ok(map);
        }
        return result;
    }

    /**
     * 注册模块
     * 
     * @param data RegisterVo
     * @param session session
     */
    @PostMapping("register")
    public Result<HashMap<String, Long>> register(@Valid @RequestBody RegisterVO data, HttpSession session) {
        // 验证code
        Integer code = (Integer)session.getAttribute("verify-code");
        if (!Objects.equals(code, data.getCode())) {
            return Result.build(null, ResultCodeEnum.CODE_ERROR);
        }
        // 验证邮箱
        String email = session.getAttribute("verify-email").toString();
        if (!email.equals(data.getEmail())) {
            return Result.build(null, ResultCodeEnum.EMAIL_NOT_CORRECT);
        }
        User user = new User();
        user.setUsername(data.getUsername());
        user.setPassword(data.getPassword());
        user.setNickname(data.getNickname());
        user.setEmail(data.getEmail());

        // 注册
        Result r = userService.register(user);
        Integer status = r.getCode();
        // 返回token
        if (status == 200) {
            String token = r.getData().toString();
            Cookie cookie = new Cookie("token", token);
            cookie.setHttpOnly(true);
            cookie.setPath("/api");
            // 生产环境打开HTTPS
            if (environmentChecker.isProduction()) {
                cookie.setSecure(true);
            }
            response.addCookie(cookie);
            // 添加过期时间
            HashMap<String, Long> map = new HashMap<>();
            Long expireTime = jwtHelper.getExpiration(token);
            map.put("expireTime", expireTime);
            // 添加Session
            session.setAttribute("token",token);
            // 设置过期时间
            int time = (int)(expireTime - System.currentTimeMillis());
            session.setMaxInactiveInterval(time);
            return Result.ok(map);
        }
        // 移除session
        if (status != 400) {
            session.removeAttribute("verify-code");
            session.removeAttribute("verify-email");
        }
        return r;
    }

    /**
     * 根据当前token获取用户数据
     * @param userId 拦截器解析出来的userId
     */
    @GetMapping("info")
    public Result<HashMap<String, UserDTO>> getUserInfo(@RequestAttribute("userid") Long userId) {
        return userService.getUserInfo(userId);
    }

    /**
     * 上传头像
     * 
     * @param file blob类型文件
     * @param userId 拦截器附带id
     */
    @PostMapping("avatar")
    public Result<ResultCodeEnum> uploadAvatar(@RequestParam("file") MultipartFile file,
        @RequestAttribute("userid") Long userId) {
        if (file.isEmpty()) {
            return Result.build(null, ResultCodeEnum.PARAM_NULL_ERROR);
        }

        if (file.getSize() > 2048) {
            return Result.build(null, ResultCodeEnum.AVATAR_TO_LARGE);
        }

        try {
            // 获取文件后缀
            String originalFileName = file.getOriginalFilename();
            assert originalFileName != null;
            int lastDotIndex = originalFileName.lastIndexOf(".");
            String suffix = originalFileName.substring(lastDotIndex + 1);

            // 将MultipartFile转换为字节数组
            byte[] bytes = file.getBytes();

            // 保存文件至本地
            String id = UUID.randomUUID().toString();
            String fileName = id + "." + suffix;
            try (FileOutputStream stream = new FileOutputStream(new File(AVATAR_DIR, fileName))) {
                stream.write(bytes);
            }

            // 删除上次avatar
            User user = userService.getById(userId);
            String avatar = user.getAvatar();
            if (!StringUtils.isEmpty(avatar)) {
                File deleteFile = new File(AVATAR_DIR, avatar);
                if (deleteFile.exists()) {
                    deleteFile.delete();
                }
            }

            // 调用事务存至服务器
            return userService.uploadAvatar(fileName, userId);
        } catch (IOException e) {
            log.error("文件写入异常{}", e.getMessage());
            return Result.build(null, ResultCodeEnum.UNKNOWN_ERROR);
        }
    }

    /**
     * 获取头像，直接返回图片流
     * @param userId 拦截器
     */
    @GetMapping("avatar")
    public void getAvatar(@RequestAttribute("userid") Long userId) {
        getCertainUserAvatar(userId);
    }

    /**
     * 更新用户nickname或brief
     * @param user 请求体
     * @param userId 拦截器
     */
    @PatchMapping("info")
    public Result<HashMap<String, UserDTO>> updateUserInfo(@RequestBody User user,
        @RequestAttribute("userid") Long userId) {
        user.setUserId(userId);
        return userService.updateUserInfo(user);
    }

    /**
     * 修改用户Role（管理员方法）
     * 
     * @param user
     */
    @PatchMapping("role")
    public Result<ResultCodeEnum> updateUserRole(@RequestBody User user) {
        return userService.updateuserRole(user);
    }

    /**
     * 获取我个人信息
     * 
     * @return
     */
    @GetMapping("/criiky0")
    public Result<HashMap<String, UserDTO>> getCriiky0Info() {
        User user = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getUsername, "criiky0"));
        HashMap<String, UserDTO> map = new HashMap<>();
        UserDTO userDTO = new UserDTO(user.getUserId(), user.getUsername(), user.getNickname(), user.getBrief(),
            user.getEmail(), user.getAvatar(), user.getRole());
        map.put("user", userDTO);
        return Result.ok(map);
    }

    @GetMapping("/criiky0/avatar")
    public void getCriiky0Avatar() {
        User criiky0 = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getUsername, "criiky0"));
        getCertainUserAvatar(criiky0.getUserId());
    }

    /**
     * 更新密码
     * 
     * @param pswVo
     * @param userId
     * @return
     */
    @PatchMapping("/password")
    public Result<ResultCodeEnum> updatePsw(@RequestBody UpdatePswVo pswVo, @RequestAttribute("userid") Long userId) {
        return userService.updatePsw(pswVo, userId);
    }

    /**
     * 根据Id获取UserInfo
     * 
     * @param userId
     * @return
     */
    @GetMapping("/{id}")
    public Result<HashMap<String, User>> getUserInfoById(@PathVariable("id") Long userId) {
        User user = userService.getById(userId);
        if (user == null) {
            return Result.build(null, ResultCodeEnum.CANNOT_FIND_ERROR);
        }
        HashMap<String, User> map = new HashMap<>();
        map.put("user", user);
        return Result.ok(map);
    }

    /**
     * 更新邮箱
     * 
     * @param updateEmailVo
     * @return
     */
    @PatchMapping("/email")
    public Result<HashMap<String,User>> updateEmail(@RequestBody UpdateEmailVo updateEmailVo,
        @RequestAttribute("userid") Long userId, HttpSession session) {
        // 验证code
        Integer code = (Integer)session.getAttribute("verify-code");
        if (!Objects.equals(code, updateEmailVo.getCode())) {
            return Result.build(null, ResultCodeEnum.CODE_ERROR);
        }
        // 验证邮箱
        String email = session.getAttribute("verify-email").toString();
        if (!email.equals(updateEmailVo.getNewEmail())) {
            return Result.build(null, ResultCodeEnum.EMAIL_NOT_CORRECT);
        }
        boolean updated = userService.update(new LambdaUpdateWrapper<User>().eq(User::getUserId, userId).set(User::getEmail,
                updateEmailVo.getNewEmail()));
        if(!updated){
            return Result.build(null,400,"因未知原因，邮箱更新失败！");
        }
        User user = userService.getById(userId);
        HashMap<String, User> map = new HashMap<>();
        map.put("user",user);
        return Result.ok(map);
    }

    /**
     * 获取指定用户头像
     * @param userId
     * @return
     */
    @GetMapping("/avatar/{id}")
    public void getAvatarById(@PathVariable("id") Long userId) {
        User user = userService.getById(userId);
        if (user == null) {
            LoginProtectUtil.writeToResponse(response, Result.build(null, ResultCodeEnum.CANNOT_FIND_ERROR));
        }
        getCertainUserAvatar(user.getUserId());
    }

    /**
     * logout
     * @param session
     * @return
     */
    @DeleteMapping("/logout")
    public Result<ResultCodeEnum> logout(HttpSession session){
        session.removeAttribute("token");
        return Result.ok(null);
    }

}
