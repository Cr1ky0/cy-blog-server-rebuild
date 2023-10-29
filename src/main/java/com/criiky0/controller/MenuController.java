package com.criiky0.controller;

import com.criiky0.pojo.Menu;
import com.criiky0.pojo.dto.MenuDTO;
import com.criiky0.service.MenuService;
import com.criiky0.utils.Result;
import com.criiky0.utils.ResultCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/menu")
@CrossOrigin
@Slf4j
public class MenuController {

    private MenuService menuService;

    @Autowired
    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    /**
     * 添加菜单
     * 
     * @param menu body
     * @param userId 拦截器附带
     * @param bindingResult 参数校验
     */
    @PostMapping
    public Result<HashMap<String, Menu>> addMenu(@Validated @RequestBody Menu menu, BindingResult bindingResult,
        @RequestAttribute("userid") Long userId) {
        if (bindingResult.hasErrors()) {
            return Result.build(null, ResultCodeEnum.PARAM_ERROR);
        }
        menu.setUserId(userId);
        return menuService.addMenu(menu);
    }

    @DeleteMapping
    public Result<ResultCodeEnum> delMenu(@RequestParam("menu_id") Long menuId){
        return menuService.deleteMenu(menuId);
    }

    /**
     * 获取我个人的菜单
     */
    @GetMapping("/criiky0")
    public Result<HashMap<String, List<MenuDTO>>> getMenuOfCriiky0(){
        return menuService.getMenuOfCriiky0();
    }
}
