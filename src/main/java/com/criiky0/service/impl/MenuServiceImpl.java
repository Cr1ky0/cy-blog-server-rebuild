package com.criiky0.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.criiky0.pojo.Menu;
import com.criiky0.pojo.dto.MenuDTO;
import com.criiky0.pojo.vo.UpdateMenuVO;
import com.criiky0.service.MenuService;
import com.criiky0.mapper.MenuMapper;
import com.criiky0.utils.Result;
import com.criiky0.utils.ResultCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

/**
 * @author criiky0
 * @description 针对表【menu】的数据库操作Service实现
 * @createDate 2023-10-26 14:24:12
 */
@Service
@Transactional
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    private MenuMapper menuMapper;

    @Autowired
    public MenuServiceImpl(MenuMapper menuMapper) {
        this.menuMapper = menuMapper;
    }

    @Override
    public Result<HashMap<String, Menu>> addMenu(Menu menu) {
        // 自动计算sort
        Integer maxSort = menuMapper.findMaxSort(menu.getBelongMenuId());
        if (maxSort != null)
            menu.setSort(maxSort + 1);
        // 自动计算level
        Integer level = menuMapper.findParentLevel(menu.getBelongMenuId());
        if (level != null)
            menu.setLevel(level + 1);
        // 插入
        int insert = menuMapper.insert(menu);
        if (insert > 0) {
            HashMap<String, Menu> map = new HashMap<>();
            map.put("menu", menu);
            return Result.ok(map);
        }
        return Result.build(null, ResultCodeEnum.UNKNOWN_ERROR);
    }

    @Override
    public Result<ResultCodeEnum> deleteMenu(Long menuId, Long userId) {
        Menu menu = menuMapper.selectById(menuId);
        if (menu == null) {
            return Result.build(null, ResultCodeEnum.CANNOT_FIND_ERROR);
        }
        if (!menu.getUserId().equals(userId)) {
            return Result.build(null, ResultCodeEnum.OPERATION_ERROR);
        }
        int rows = menuMapper.deleteById(menuId);
        if (rows > 0)
            return Result.ok(null);
        return Result.build(null, ResultCodeEnum.UNKNOWN_ERROR);
    }

    /**
     * 递归查询生成menu完整结构
     * 
     * @param rootMenu
     * @return 返回对应menu的子菜单（子菜单结构是完整的）
     */
    @Override
    public List<MenuDTO> findSubMenu(MenuDTO rootMenu) {
        Long menuId = rootMenu.getMenuId();
        List<MenuDTO> subMenu = menuMapper.selectSubMenu(menuId);
        // 子菜单为空返回null
        if (subMenu.isEmpty())
            return null;
        // 否则递归查找子菜单并返回subMenu
        for (MenuDTO menu : subMenu) {
            List<MenuDTO> childs = findSubMenu(menu);
            menu.setSubMenu(childs);
        }
        return subMenu;
    }

    @Override
    public Result<HashMap<String, List<MenuDTO>>> getMenuOfCriiky0() {
        List<MenuDTO> menus = menuMapper.selectTopLevelMenuOfCriiky0();
        for (MenuDTO menu : menus) {
            List<MenuDTO> subMenu = findSubMenu(menu);
            menu.setSubMenu(subMenu);
        }
        HashMap<String, List<MenuDTO>> map = new HashMap<>();
        map.put("menus", menus);
        return Result.ok(map);
    }

    @Override
    public Result<ResultCodeEnum> updateMenu(UpdateMenuVO updateMenuVO, Long userId) {
        Long menuId = updateMenuVO.getMenuId();
        Menu menu = menuMapper.selectById(menuId);
        if (menu == null)
            return Result.build(null, ResultCodeEnum.CANNOT_FIND_ERROR);
        if (!menu.getUserId().equals(userId))
            return Result.build(null, ResultCodeEnum.OPERATION_ERROR);
        LambdaUpdateWrapper<Menu> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Menu::getMenuId, updateMenuVO.getMenuId())
            .set(updateMenuVO.getTitle() != null, Menu::getTitle, updateMenuVO.getTitle())
            .set(updateMenuVO.getIcon() != null, Menu::getIcon, updateMenuVO.getIcon())
            .set(updateMenuVO.getColor() != null, Menu::getColor, updateMenuVO.getColor())
            .set(updateMenuVO.getBelongMenuId() != null, Menu::getBelongMenuId, updateMenuVO.getBelongMenuId());
        int update = menuMapper.update(null, wrapper);
        if(update > 0){
            return Result.ok(null);
        }
        return Result.build(null,ResultCodeEnum.PARAM_NULL_ERROR);
    }

}
