package com.criiky0.controller;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.criiky0.pojo.User;
import com.criiky0.pojo.dto.UserDTO;
import com.criiky0.pojo.vo.RegisterVo;
import com.criiky0.service.UserService;
import com.criiky0.utils.JavaMailUtil;
import com.criiky0.utils.Result;
import com.criiky0.utils.ResultCodeEnum;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/user")
@CrossOrigin
@Slf4j
public class UserController {

    private UserService userService;

    @Resource
    private HttpServletResponse response;

    private static final String AVATAR_DIR = System.getProperty("user.dir") + "/public/avatars";

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 发送验证码
     * 
     * @param email 发送邮箱
     * @param session session
     * @return
     */
    @GetMapping("code")
    public Result<HashMap<String, Integer>> code(@RequestParam String email, HttpSession session) {
        // 验证邮箱是否被占用
        boolean exists = userService.exists(new LambdaQueryWrapper<User>().eq(User::getEmail, email));
        if (exists) {
            return Result.build(null, ResultCodeEnum.EMAIL_USED);
        }
        Integer code = JavaMailUtil.sendCode(email);
        HashMap<String, Integer> codeMap = new HashMap<>();
        codeMap.put("code", code);
        // 放入session
        session.setAttribute("verify-code", code);
        session.setMaxInactiveInterval(10 * 60); // 10分钟内有效
        return Result.ok(codeMap);
    }

    /**
     * 用户登录
     *
     * @param user user对象
     */
    @PostMapping("login")
    public Result<HashMap<String, String>> login(@RequestBody User user) {
        return userService.login(user);
    }

    /**
     * 注册模块
     *
     * @param data RegisterVo
     * @param session session
     */
    @PostMapping("register")
    public Result<HashMap<String, String>> register(@RequestBody RegisterVo data, HttpSession session) {
        // 验证code
        Integer code = (Integer)session.getAttribute("verify-code");
        if (!Objects.equals(code, data.getCode())) {
            return Result.build(null, ResultCodeEnum.CODE_ERROR);
        }
        User user = new User();
        user.setUsername(data.getUsername());
        user.setPassword(data.getPassword());
        user.setNickname(data.getNickname());
        user.setEmail(data.getEmail());

        // 注册
        Result<HashMap<String, String>> r = userService.register(user);
        // 移除session
        session.removeAttribute("verify-code");

        return r;
    }

    /**
     * 根据当前token获取用户数据
     * 
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

    @GetMapping("avatar")
    public void getAvatar(@RequestAttribute("userid") Long userId) {
        try (OutputStream os = response.getOutputStream()) {
            User user = userService.getById(userId);
            String avatar = user.getAvatar();
            File file = new File(AVATAR_DIR, avatar);
            String fileName = file.getName();
            int lastDotIndex = fileName.lastIndexOf('.');
            String suffix = fileName.substring(lastDotIndex + 1);
            // 读取图片
            BufferedImage image = ImageIO.read(new FileInputStream(file));
            response.setContentType("image/" + suffix);

            if (image != null) {
                ImageIO.write(image, "png", os);
            }
        } catch (IOException e) {
            log.error("获取图片异常{}", e.getMessage());
        }
    }
}
