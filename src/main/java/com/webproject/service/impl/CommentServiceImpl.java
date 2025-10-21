package com.webproject.service.impl;

import com.webproject.mapper.CommentMapper;

import com.webproject.pojo.Comment;
import com.webproject.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class CommentServiceImpl implements CommentService {
    
    @Autowired
    private CommentMapper commentMapper;
    

    
    @Override
    public List<Comment> getById(Integer postId) {
        log.info("查询帖子{}的评论", postId);
        return commentMapper.getByPostId(postId);
    }
    
    @Override
    @Transactional
    public void addComment(Comment comment) {
        // 1. 设置创建时间
        comment.setCreateTime(LocalDateTime.now());
        
        // 2. 保存评论
        commentMapper.insert(comment);
        

    }
    
    @Override
    @Transactional
    public void deleteComment(Integer commentId, Integer userId) {
        // 1. 查询评论信息
        Comment comment = commentMapper.getById(commentId);
        if (comment == null) {
            log.warn("评论{}不存在", commentId);
            return;
        }
        
        // 2. 检查权限（只有评论作者能删除）
        if (!comment.getUserId().equals(userId)) {
            log.warn("用户{}尝试删除不属于自己的评论{}", userId, commentId);
            return;
        }
        
        // 3. 删除评论
        commentMapper.deleteById(commentId);

        
        log.info("用户{}删除评论{}", userId, commentId);
    }
    
    @Override
    @Transactional
    public void updateComment(Integer commentId, Comment comment) {
        // 1. 查询原评论信息
        Comment originalComment = commentMapper.getById(commentId);
        if (originalComment == null) {
            log.warn("评论{}不存在", commentId);
            return;
        }
        
        // 2. 检查权限（只有评论作者能编辑）
        if (!originalComment.getUserId().equals(comment.getUserId())) {
            log.warn("用户{}尝试编辑不属于自己的评论{}", comment.getUserId(), commentId);
            return;
        }
        
        // 3. 设置更新字段
        comment.setId(commentId);
        comment.setPostId(originalComment.getPostId());
        comment.setUserId(originalComment.getUserId());
        comment.setCreateTime(originalComment.getCreateTime());
        
        // 4. 更新评论
        commentMapper.update(comment);
        
        log.info("用户{}更新评论{}: {}", comment.getUserId(), commentId, comment.getContent());
    }
    
    @Override
    @Transactional
    public void deleteByPostId(Integer postId) {
        // 删除帖子的所有评论
        commentMapper.deleteByPostId(postId);
        log.info("删除帖子{}的所有评论", postId);
    }
    
    @Override
    public Integer countByUserId(Integer userId) {
        return commentMapper.countByUserId(userId);
    }
    
    @Override
    public Integer countByPostId(Integer postId) {
        return commentMapper.countByPostId(postId);
    }
}
