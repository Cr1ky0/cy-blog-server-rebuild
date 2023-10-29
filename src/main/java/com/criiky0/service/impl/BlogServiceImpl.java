package com.criiky0.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.DeleteResponse;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.UpdateResponse;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.criiky0.pojo.Blog;
import com.criiky0.pojo.BlogDoc;
import com.criiky0.pojo.vo.UpdateBlogVO;
import com.criiky0.service.BlogService;
import com.criiky0.mapper.BlogMapper;
import com.criiky0.utils.ElasticSearchUtil;
import com.criiky0.utils.Result;
import com.criiky0.utils.ResultCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

/**
 * @author criiky0
 * @description 针对表【blog】的数据库操作Service实现
 * @createDate 2023-10-26 14:24:12
 */
@Service
@Transactional
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements BlogService {

    private BlogMapper blogMapper;

    private final co.elastic.clients.elasticsearch._types.Result CREATED =
        co.elastic.clients.elasticsearch._types.Result.Created;

    private final co.elastic.clients.elasticsearch._types.Result DELETED =
        co.elastic.clients.elasticsearch._types.Result.Deleted;

    private final co.elastic.clients.elasticsearch._types.Result UPDATED =
        co.elastic.clients.elasticsearch._types.Result.Updated;

    @Autowired
    public BlogServiceImpl(BlogMapper blogMapper) {
        this.blogMapper = blogMapper;
    }

    @Override
    public Result<HashMap<String, Blog>> addBlog(Blog blog) {
        // 自动计算sort
        Integer maxSort = blogMapper.findMaxSort(blog.getMenuId());
        if (maxSort != null) {
            blog.setSort(maxSort + 1);
        }
        long curTime = System.currentTimeMillis();
        blog.setCreateAt(new Date(curTime));
        blog.setUpdateAt(new Date(curTime));
        blog.setLikes(0);
        blog.setViews(0);
        // 存入数据库
        int insert = blogMapper.insert(blog);
        if (insert > 0) {
            // 加入ES索引
            ElasticsearchClient client = ElasticSearchUtil.client;
            IndexResponse response;
            try {
                response = client.index(i -> i.index("blogs").id(blog.getBlogId().toString())
                    .document(new BlogDoc(blog.getTitle(), blog.getContent())));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            // 如果加入ES索引失败则回滚
            if (!response.result().equals(CREATED)) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return Result.build(null, ResultCodeEnum.UNKNOWN_ERROR);
            }
            HashMap<String, Blog> map = new HashMap<>();
            map.put("blog", blog);
            return Result.ok(map);
        }

        return Result.build(null, ResultCodeEnum.UNKNOWN_ERROR);
    }

    @Override
    public Result<ResultCodeEnum> deleteBlog(Long blogId, Long userId) {
        Blog blog = blogMapper.selectById(blogId);
        if (blog == null) {
            return Result.build(null, ResultCodeEnum.CANNOT_FIND_ERROR);
        }
        if (!blog.getUserId().equals(userId)) {
            return Result.build(null, ResultCodeEnum.OPERATION_ERROR);
        }
        // 数据库删除
        int rows = blogMapper.deleteById(blogId);
        if (rows > 0) {
            // ES索引删除
            ElasticsearchClient client = ElasticSearchUtil.client;
            DeleteResponse response;
            try {
                response = client.delete(d -> d.index("blogs").id(blogId.toString()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            // 如果索引删除失败则回滚
            if (!response.result().equals(DELETED)) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return Result.build(null, ResultCodeEnum.UNKNOWN_ERROR);
            }
            return Result.ok(null);
        }
        return Result.build(null, ResultCodeEnum.UNKNOWN_ERROR);
    }

    @Override
    public Result<HashMap<String, Blog>> updateBlog(UpdateBlogVO updateBlogVO, Long userId) {
        Blog blog = blogMapper.selectById(updateBlogVO.getBlogId());
        if (blog == null) {
            return Result.build(null, ResultCodeEnum.CANNOT_FIND_ERROR);
        }
        if (!blog.getUserId().equals(userId)) {
            return Result.build(null, ResultCodeEnum.OPERATION_ERROR);
        }
        LambdaUpdateWrapper<Blog> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Blog::getBlogId, updateBlogVO.getBlogId())
            .set(updateBlogVO.getTitle() != null, Blog::getTitle, updateBlogVO.getTitle())
            .set(updateBlogVO.getContent() != null, Blog::getContent, updateBlogVO.getContent())
            .set(updateBlogVO.getLikes() != null, Blog::getLikes, updateBlogVO.getLikes())
            .set(updateBlogVO.getViews() != null, Blog::getViews, updateBlogVO.getViews())
            .set(updateBlogVO.getUpdateAt() != null, Blog::getUpdateAt, updateBlogVO.getUpdateAt())
            .set(updateBlogVO.getMenuId() != null, Blog::getMenuId, updateBlogVO.getMenuId());
        int update = blogMapper.update(null, updateWrapper);
        if (update > 0) {
            Blog updatedBlog = blogMapper.selectById(updateBlogVO.getBlogId());
            // ES索引修改
            if (updateBlogVO.getContent() != null || updateBlogVO.getTitle() != null) {
                ElasticsearchClient client = ElasticSearchUtil.client;
                UpdateResponse<BlogDoc> response;
                try {
                    response =
                        client
                            .update(
                                u -> u.index("blogs").id(updatedBlog.getBlogId().toString())
                                    .doc(new BlogDoc(updatedBlog.getTitle(), updatedBlog.getContent())),
                                BlogDoc.class);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                if (!response.result().equals(UPDATED)) {
                    return Result.build(null, ResultCodeEnum.UNKNOWN_ERROR);
                }
                HashMap<String, Blog> map = new HashMap<>();
                map.put("updatedBlog", updatedBlog);
                return Result.ok(map);
            }
        }
        return Result.build(null, ResultCodeEnum.UNKNOWN_ERROR);
    }
}
