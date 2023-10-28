package com.criiky0.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.criiky0.pojo.Menu;
import com.criiky0.service.MenuService;
import com.criiky0.mapper.MenuMapper;
import com.criiky0.utils.Result;
import com.criiky0.utils.ResultCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * @author criiky0
 * @description 针对表【menu】的数据库操作Service实现
 * @createDate 2023-10-26 14:24:12
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    private MenuMapper menuMapper;

    @Autowired
    public MenuServiceImpl(MenuMapper menuMapper) {
        this.menuMapper = menuMapper;
    }

    @Override
    public Result<HashMap<String,Menu>> addMenu(Menu menu) {
        int insert = menuMapper.insert(menu);
        if (insert > 0) {
            HashMap<String, Menu> map = new HashMap<>();
            map.put("menu", menu);
            return Result.ok(map);
        }
        return Result.build(null, ResultCodeEnum.UNKNOWN_ERROR);
    }
}
