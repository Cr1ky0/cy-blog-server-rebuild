package com.criiky0.service;

import com.criiky0.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.criiky0.pojo.dto.UserDTO;
import com.criiky0.pojo.vo.LoginVo;
import com.criiky0.pojo.vo.UpdatePswVo;
import com.criiky0.utils.Result;
import com.criiky0.utils.ResultCodeEnum;

import java.util.HashMap;

/**
* @author criiky0
* @description 针对表【user】的数据库操作Service
* @createDate 2023-10-26 14:24:13
*/
public interface UserService extends IService<User> {

    Result login(LoginVo loginVo);

    Result register(User user);

    Result<HashMap<String, UserDTO>>  getUserInfo(Long userId);

    Result<ResultCodeEnum> uploadAvatar(String avatar,Long userId);

    Result<HashMap<String,UserDTO>> updateUserInfo(User user);

    Result<ResultCodeEnum> updateuserRole(User user);

    Result<ResultCodeEnum> updatePsw(UpdatePswVo pswVo, Long userId);
}
