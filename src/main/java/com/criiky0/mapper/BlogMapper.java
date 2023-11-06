package com.criiky0.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.criiky0.pojo.Blog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.criiky0.pojo.dto.BlogDTO;
import org.apache.ibatis.annotations.MapKey;

import java.util.List;
import java.util.Map;

/**
 * @author criiky0
 * @description 针对表【blog】的数据库操作Mapper
 * @createDate 2023-10-26 14:24:12
 * @Entity com.criiky0.pojo.Blog
 */
public interface BlogMapper extends BaseMapper<Blog> {

    /**
     * 筛选出当前菜单下Blog的最大Sort
     * 
     * @param menuId
     * @return
     */
    Integer findMaxSort(Long menuId);

    /**
     * 删除当前Menu下的所有Blog
     * 
     * @param menuId
     * @return
     */
    int deleteAllOfMenu(Long menuId);

    List<BlogDTO> selectCollectedBlogDTO(Long userId);

    List<BlogDTO> selectTimeLine(Long userId);

    List<BlogDTO> getBlogDTOOfMenu(Long menuId);

    @MapKey("blogs")
    IPage<Map> selectPageOfUser(IPage<Blog> page, Long userId, String sort, Map<String, String> queryMap);

    @MapKey("blogs")
    IPage<Map> selectBlogHasCommentOfUser(IPage<Blog> page,Long userId);

    long countByUserWithOptions(Long userId, Map<String, String> queryMap);
}
