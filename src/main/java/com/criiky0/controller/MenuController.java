package com.criiky0.controller;

import com.criiky0.pojo.Menu;
import com.criiky0.pojo.dto.BlogDTO;
import com.criiky0.pojo.dto.MenuDTO;
import com.criiky0.pojo.vo.UpdateMenuVO;
import com.criiky0.service.MenuService;
import com.criiky0.utils.Result;
import com.criiky0.utils.ResultCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/menu")
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
     */
    @PostMapping
    public Result<HashMap<String, Menu>> addMenu(@Validated @RequestBody Menu menu,
        @RequestAttribute("userid") Long userId) {
        menu.setUserId(userId);
        return menuService.addMenu(menu);
    }

    /**
     * 删除菜单（递归删除子菜单和Blogs）
     *
     * @param menuId
     * @param userId
     * @return
     */
    @DeleteMapping
    public Result<ResultCodeEnum> delMenu(@RequestParam("menu_id") Long menuId,
        @RequestAttribute("userid") Long userId) {
        Result<MenuDTO> result = menuService.findMenuIncludesSubMenu(menuId);
        if (result.getCode() != 200) {
            return Result.build(null, result.getCode(), result.getMessage());
        }
        MenuDTO menu = result.getData();
        Result r = menuService.deleteMenuRecursion(menu, userId);
        if (r.getCode() != 200) {
            return Result.build(null, r.getCode(), r.getMessage());
        }
        return Result.ok(null);
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
     *
     * @param menuId
     * @return
     */
    @GetMapping("/single/{id}")
    public Result<HashMap<String, MenuDTO>> getMenu(@PathVariable("id") Long menuId) {
        Optional<Menu> opt = menuService.getOptById(menuId);
        if (opt.isEmpty()) {
            return Result.build(null, ResultCodeEnum.CANNOT_FIND_ERROR);
        }
        HashMap<String, MenuDTO> map = new HashMap<>();
        Menu menu = opt.get();
        MenuDTO menuDTO = new MenuDTO(menu.getMenuId(), menu.getTitle(), menu.getIcon(), menu.getColor(),
            menu.getLevel(), menu.getSort(), menu.getBelongMenuId(), menu.getUserId(), null, null);
        List<MenuDTO> subMenu = menuService.findSubMenu(menuDTO);
        menuDTO.setSubMenu(subMenu);
        map.put("menu", menuDTO);
        return Result.ok(map);
    }

    /**
     * 更新菜单
     *
     * @param updateMenuVO 可更新belong、title、icon以及color
     * @param userId
     */
    @PatchMapping
    public Result<HashMap<String, Menu>> updateMenu(@Validated @RequestBody UpdateMenuVO updateMenuVO,
        @RequestAttribute("userid") Long userId) {
        List<Object> paramList = Arrays.asList(updateMenuVO.getTitle(), updateMenuVO.getBelongMenuId(),
            updateMenuVO.getIcon(), updateMenuVO.getColor());
        boolean isAllNull = paramList.stream().allMatch(Objects::isNull);
        if (isAllNull) {
            return Result.build(null, ResultCodeEnum.PARAM_NULL_ERROR);
        }
        return menuService.updateMenu(updateMenuVO, userId);
    }

    /**
     * 修改排序
     * @param idList
     * @return
     */
    @PostMapping("/sort")
    public Result<ResultCodeEnum> sort(@RequestBody List<Long> idList, @RequestAttribute("userid") Long userId) {
        return menuService.sort(idList, userId);
    }

}
