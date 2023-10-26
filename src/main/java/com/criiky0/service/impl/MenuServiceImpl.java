package com.criiky0.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.criiky0.pojo.Menu;
import com.criiky0.service.MenuService;
import com.criiky0.mapper.MenuMapper;
import org.springframework.stereotype.Service;

/**
* @author 50309
* @description 针对表【menu】的数据库操作Service实现
* @createDate 2023-10-26 14:24:12
*/
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu>
    implements MenuService{

}




