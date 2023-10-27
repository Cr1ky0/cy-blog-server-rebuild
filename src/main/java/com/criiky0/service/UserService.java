package com.criiky0.service;

import com.criiky0.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.criiky0.pojo.dto.UserDTO;
import com.criiky0.utils.Result;

import java.util.HashMap;

/**
* @author criiky0
* @description 针对表【user】的数据库操作Service
* @createDate 2023-10-26 14:24:13
*/
public interface UserService extends IService<User> {

    Result<HashMap<String, String>> login(User user);

    Result<HashMap<String, String>> register(User user);

    Result<HashMap<String, UserDTO>>  getUserInfo(Long userId);
}
