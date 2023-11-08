package com.criiky0.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.criiky0.pojo.Menu;
import com.criiky0.pojo.dto.MenuDTO;
import com.criiky0.pojo.vo.UpdateMenuVO;
import com.criiky0.service.BlogService;
import com.criiky0.service.MenuService;
import com.criiky0.mapper.MenuMapper;
import com.criiky0.utils.Result;
import com.criiky0.utils.ResultCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

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

    private BlogService blogService;

    @Autowired
    public MenuServiceImpl(MenuMapper menuMapper, BlogService blogService) {
        this.menuMapper = menuMapper;
        this.blogService = blogService;
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

    /**
     * 删除菜单及菜单下的blogs
     * 
     * @param menuId
     * @param userId
     * @return
     */
    @Override
    public Result<ResultCodeEnum> deleteMenu(Long menuId, Long userId) {
        Menu menu = menuMapper.selectById(menuId);
        if (menu == null) {
            return Result.build(null, ResultCodeEnum.CANNOT_FIND_ERROR);
        }
        if (!menu.getUserId().equals(userId)) {
            return Result.build(null, ResultCodeEnum.OPERATION_ERROR);
        }
        // 先删除所有该Menu下的blog，不然后面会查找不到menu
        Result<ResultCodeEnum> result = blogService.deleteBlogsOfMenu(menuId, userId);
        if (result.getCode() != 200) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return result;
        }
        // 再删除Menu
        int rows = menuMapper.deleteById(menuId);
        if (rows > 0) {
            return Result.ok(null);
        }
        return Result.build(null, ResultCodeEnum.UNKNOWN_ERROR);
    }

    /**
     * 查找该Menu完整结构
     * 
     * @param menuId
     * @return
     */
    @Override
    public Result<MenuDTO> findMenuIncludesSubMenu(Long menuId) {
        MenuDTO menuDTO = menuMapper.selectMenuWithDetails(menuId);
        if (menuDTO == null) {
            return Result.build(null, ResultCodeEnum.CANNOT_FIND_ERROR);
        }
        return Result.ok(menuDTO);
    }

    /**
     * 递归删除该menu下所有结构
     * 
     * @param menuDTO
     * @return
     */
    @Override
    public Result deleteMenuRecursion(MenuDTO menuDTO, Long userId) throws RuntimeException {
        if (menuDTO != null) {
            // 删除该菜单及其下的所有Blogs
            Result<ResultCodeEnum> result = deleteMenu(menuDTO.getMenuId(), userId);
            if (result.getCode() == 200) {
                // SubMenu不存在则停止
                if (menuDTO.getSubMenu() == null || menuDTO.getSubMenu().isEmpty()) {
                    return Result.ok(null);
                }
                // 否则递归删除子菜单
                for (MenuDTO menu : menuDTO.getSubMenu()) {
                    deleteMenuRecursion(menu, userId);
                }
            } else {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return Result.build(null, result.getCode(), result.getMessage());
            }
        }
        return Result.ok(null);
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
        // for (MenuDTO menu : menus) {
        // List<MenuDTO> subMenu = findSubMenu(menu);
        // menu.setSubMenu(subMenu);
        // }
        HashMap<String, List<MenuDTO>> map = new HashMap<>();
        map.put("menus", menus);
        return Result.ok(map);
    }

    @Override
    public Result<HashMap<String, Menu>> updateMenu(UpdateMenuVO updateMenuVO, Long userId) {
        Long menuId = updateMenuVO.getMenuId();
        Menu menu = menuMapper.selectById(menuId);
        int level = 0;
        if (updateMenuVO.getBelongMenuId() != null && updateMenuVO.getBelongMenuId() != 0) {
            Menu belong = menuMapper.selectById(updateMenuVO.getBelongMenuId());
            if (belong == null)
                return Result.build(null, ResultCodeEnum.CANNOT_FIND_ERROR);
            level = belong.getLevel() + 1;
        }
        if (menu == null)
            return Result.build(null, ResultCodeEnum.CANNOT_FIND_ERROR);
        if (!menu.getUserId().equals(userId))
            return Result.build(null, ResultCodeEnum.OPERATION_ERROR);
        LambdaUpdateWrapper<Menu> wrapper = new LambdaUpdateWrapper<>();
        if (menu.getBelongMenuId() == null) {
            updateMenuVO.setBelongMenuId(null);
            level = 1;
        }
        wrapper.eq(Menu::getMenuId, updateMenuVO.getMenuId())
            .set(updateMenuVO.getTitle() != null, Menu::getTitle, updateMenuVO.getTitle())
            .set(updateMenuVO.getIcon() != null, Menu::getIcon, updateMenuVO.getIcon())
            .set(updateMenuVO.getColor() != null, Menu::getColor, updateMenuVO.getColor())
            .set(Menu::getBelongMenuId, updateMenuVO.getBelongMenuId()).set(Menu::getLevel, level);
        int update = menuMapper.update(null, wrapper);
        if (update > 0) {
            Menu updatedMenu = menuMapper.selectById(updateMenuVO.getMenuId());
            HashMap<String, Menu> map = new HashMap<>();
            map.put("updatedMenu", updatedMenu);
            return Result.ok(map);
        }
        return Result.build(null, ResultCodeEnum.PARAM_NULL_ERROR);
    }

    @Override
    public Result<ResultCodeEnum> sort(List<Long> idList, Long userId) {
        int count = 0;
        for (Long id : idList) {
            Menu menu = menuMapper.selectById(id);
            if (menu == null) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return Result.build(null, ResultCodeEnum.CANNOT_FIND_ERROR);
            }
            if (!menu.getUserId().equals(userId)) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return Result.build(null, ResultCodeEnum.OPERATION_ERROR);
            }
            int update = menuMapper.update(null,
                new LambdaUpdateWrapper<Menu>().eq(Menu::getMenuId, id).set(Menu::getSort, count));
            if (update > 0) {
                count++;
                continue;
            }
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Result.build(null, ResultCodeEnum.UNKNOWN_ERROR);
        }
        return Result.ok(null);
    }

    @Override
    public MenuDTO getSingleMenuDTO(Long menuId) {
        return menuMapper.selectMenuWithDetails(menuId);
    }

}
