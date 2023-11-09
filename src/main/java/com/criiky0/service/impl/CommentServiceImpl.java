package com.criiky0.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.criiky0.pojo.Comment;
import com.criiky0.pojo.dto.CommentDTO;
import com.criiky0.service.CommentService;
import com.criiky0.mapper.CommentMapper;
import com.criiky0.utils.QueryHelper;
import com.criiky0.utils.Result;
import com.criiky0.utils.ResultCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author criiky0
 * @description 针对表【comment】的数据库操作Service实现
 * @createDate 2023-10-26 14:24:13
 */
@Service
@Transactional
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    private CommentMapper commentMapper;

    @Autowired
    public CommentServiceImpl(CommentMapper commentMapper) {
        this.commentMapper = commentMapper;
    }

    @Override
    public Result<HashMap<String, Comment>> addComment(Comment comment) {
        int insert = commentMapper.insert(comment);
        if (insert > 0) {
            Comment newComment = commentMapper.selectById(comment.getCommentId());
            HashMap<String, Comment> map = new HashMap<>();
            map.put("comment", newComment);
            return Result.ok(map);
        }
        return Result.build(null, ResultCodeEnum.UNKNOWN_ERROR);
    }

    @Override
    public List<CommentDTO> findSubComment(CommentDTO rootComment) {
        Long commentId = rootComment.getCommentId();
        List<CommentDTO> subComment = commentMapper.selectSubComment(commentId);
        // 子评论为空返回null
        if (subComment.isEmpty())
            return null;
        // 否则递归查找子评论并返回subMenu
        for (CommentDTO comment : subComment) {
            List<CommentDTO> childs = findSubComment(comment);
            comment.setSubComments(childs);
        }
        return subComment;
    }

    @Override
    public Result<HashMap<String, Object>> getCommentPageOfBlog(Long blogId, Integer page, Integer size, String sort,
        String options) {
        IPage<CommentDTO> myPage = new Page<>(page, size);
        Map<String, String> queryMap = QueryHelper.filterOptions(options);
        commentMapper.selectCommentDTOsOfBlog(myPage, blogId, sort, queryMap);
        HashMap<String, Object> pageMap = new HashMap<>();
        pageMap.put("records", myPage.getRecords());
        pageMap.put("pageNum", myPage.getCurrent());
        pageMap.put("pageSize", myPage.getSize());
        pageMap.put("totalPage", myPage.getPages());
        pageMap.put("totalSize", myPage.getTotal());
        return Result.ok(pageMap);
    }

    @Override
    public Result<ResultCodeEnum> deleteAllOfBlog(Long blogId) {
        commentMapper.delete(new LambdaQueryWrapper<Comment>().eq(Comment::getBlogId, blogId));
        return Result.ok(null);
    }

    @Override
    public Result<ResultCodeEnum> deleteComment(Long commentId) {
        Comment comment = commentMapper.selectById(commentId);
        if (comment == null)
            return Result.build(null, ResultCodeEnum.CANNOT_FIND_ERROR);
        // 删除子评论（这里评论仅两层）
        commentMapper.delete(new LambdaQueryWrapper<Comment>().eq(Comment::getBelongCommentId, commentId));
        // 删除主评论
        commentMapper.deleteById(commentId);
        return Result.ok(null);
    }

    @Override
    public HashMap<String, List<CommentDTO>> selectAllOfBlog(Long blogId) {
        List<CommentDTO> comments = commentMapper.selectAllOfBlog(blogId);
        HashMap<String, List<CommentDTO>> map = new HashMap<>();
        map.put("comments", comments);
        return map;
    }
}
