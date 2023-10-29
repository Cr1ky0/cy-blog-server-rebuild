package com.criiky0.mapper;

import com.criiky0.pojo.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.criiky0.pojo.dto.MenuDTO;

import java.util.List;

/**
 * @author criiky0
 * @description 针对表【menu】的数据库操作Mapper
 * @createDate 2023-10-26 14:24:12
 * @Entity com.criiky0.pojo.Menu
 */
public interface MenuMapper extends BaseMapper<Menu> {
    /**
     * 筛选出所有belong_menu_id下的菜单并挑出其中sort的最大值
     * 如果没有belong_menu_id则找出level=1下的所有菜单的sort最大值
     * 因为没有belong_menu_id则必是第一层，即最外层
     * @param belongMenuId
     */
    Integer findMaxSort(Long belongMenuId);

    /**
     * 获取parent的level，无parent返回null
     * @param belongMenuId
     * @return
     */
    Integer findParentLevel(Long belongMenuId);

    /**
     * 获取Criiky0的Top层级菜单
     * @return
     */
    List<MenuDTO> selectTopLevelMenuOfCriiky0();

    /**
     * 获取子菜单List
     * @param menuId
     */
    List<MenuDTO> selectSubMenu(Long menuId);
}



