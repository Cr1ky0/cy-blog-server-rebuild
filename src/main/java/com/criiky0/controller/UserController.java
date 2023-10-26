package com.criiky0.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.criiky0.pojo.User;
import com.criiky0.pojo.vo.RegisterVo;
import com.criiky0.service.UserService;
import com.criiky0.utils.JavaMailUtil;
import com.criiky0.utils.Result;
import com.criiky0.utils.ResultCodeEnum;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Objects;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private UserService userService;

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
}
