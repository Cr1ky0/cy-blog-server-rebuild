package com.criiky0.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.criiky0.pojo.User;
import com.criiky0.service.UserService;
import com.criiky0.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
* @author 50309
* @description 针对表【user】的数据库操作Service实现
* @createDate 2023-10-26 14:24:13
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

}




