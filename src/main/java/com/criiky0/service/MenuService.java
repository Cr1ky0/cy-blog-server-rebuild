package com.criiky0.service;

import com.criiky0.pojo.Menu;
import com.baomidou.mybatisplus.extension.service.IService;
import com.criiky0.pojo.dto.MenuDTO;
import com.criiky0.pojo.vo.UpdateMenuVO;
import com.criiky0.utils.Result;
import com.criiky0.utils.ResultCodeEnum;

import java.util.HashMap;
import java.util.List;

/**
 * @author criiky0
 * @description 针对表【menu】的数据库操作Service
 * @createDate 2023-10-26 14:24:12
 */
public interface MenuService extends IService<Menu> {

    Result<HashMap<String, Menu>> addMenu(Menu menu);

    Result<ResultCodeEnum> deleteMenu(Long menuId, Long userId);

    Result<MenuDTO> findMenuIncludesSubMenu(Long menuId);

    Result deleteMenuRecursion(MenuDTO menuDTO, Long userId);

    List<MenuDTO> findSubMenu(MenuDTO rootMenu);

    Result<HashMap<String, List<MenuDTO>>> getMenuOfCriiky0();

    Result<HashMap<String, Menu>> updateMenu(UpdateMenuVO updateMenuVO, Long userId);

    Result<ResultCodeEnum> sort(List<Long> idList,Long userId);

    MenuDTO getSingleMenuDTO(Long menuId);
}
