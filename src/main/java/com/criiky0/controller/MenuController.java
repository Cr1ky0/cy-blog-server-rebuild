package com.criiky0.controller;

import com.criiky0.pojo.Menu;
import com.criiky0.pojo.dto.MenuDTO;
import com.criiky0.pojo.vo.UpdateMenuVO;
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
import java.util.Optional;

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
    public Result<ResultCodeEnum> delMenu(@RequestParam("menu_id") Long menuId,
        @RequestAttribute("userid") Long userId) {
        return menuService.deleteMenu(menuId, userId);
    }

    /**
     * 获取我个人的菜单
     */
    @GetMapping("/criiky0")
    public Result<HashMap<String, List<MenuDTO>>> getMenuOfCriiky0() {
        return menuService.getMenuOfCriiky0();
    }

    /**
     * 查找单个Menu
     * @param menuId
     * @return
     */
    @GetMapping("/single")
    public Result<HashMap<String, MenuDTO>> getMenu(@RequestParam("menu_id") Long menuId) {
        Optional<Menu> opt = menuService.getOptById(menuId);
        if (opt.isEmpty()) {
            return Result.build(null, ResultCodeEnum.CANNOT_FIND_ERROR);
        }
        HashMap<String, MenuDTO> map = new HashMap<>();
        Menu menu = opt.get();
        MenuDTO menuDTO = new MenuDTO(menu.getMenuId(), menu.getTitle(), menu.getIcon(), menu.getColor(),
            menu.getLevel(), menu.getSort(), menu.getBelongMenuId(), menu.getUserId(), null);
        List<MenuDTO> subMenu = menuService.findSubMenu(menuDTO);
        menuDTO.setSubMenu(subMenu);
        map.put("menu", menuDTO);
        return Result.ok(map);
    }

    /**
     * 更新菜单
     * @param updateMenuVO 可更新belong、title、icon以及color
     * @param result
     * @param userId
     */
    @PatchMapping
    public Result<ResultCodeEnum> updateMenu(@Validated @RequestBody UpdateMenuVO updateMenuVO, BindingResult result,
        @RequestAttribute("userid") Long userId) {
        if (result.hasErrors()) {
            return Result.build(null, ResultCodeEnum.PARAM_NULL_ERROR);
        }
        if (updateMenuVO.getTitle() == null && updateMenuVO.getBelongMenuId() == null && updateMenuVO.getIcon() == null
            && updateMenuVO.getColor() == null)
            return Result.build(null, ResultCodeEnum.PARAM_NULL_ERROR);
        return menuService.updateMenu(updateMenuVO, userId);
    }
}
