package com.criiky0.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.criiky0.pojo.Comment;
import com.criiky0.service.CommentService;
import com.criiky0.mapper.CommentMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
* @author criiky0
* @description 针对表【comment】的数据库操作Service实现
* @createDate 2023-10-26 14:24:13
*/
@Service
@Transactional
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment>
    implements CommentService{

}




