package com.webproject.service;

import com.webproject.pojo.Comment;

import java.util.List;

public interface CommentService {
    List<Comment> getById(Integer postId);
    
    void addComment(Comment comment);
    
    void deleteComment(Integer commentId, Integer userId);
    
    void updateComment(Integer commentId, Comment comment);
    
    void deleteByPostId(Integer postId);
    
    Integer countByUserId(Integer userId);
    
    Integer countByPostId(Integer postId);
    
    // 新增方法：分页查询所有用户的评论
    List<Comment> getAllCommentsWithPage(Integer page, Integer pageSize);
    
    // 新增方法：删除帖子的评论
    void deleteCommentsByPostId(Integer postId);
    
    // 新增方法：获取所有评论数量
    Integer getAllCommentsCount();
}
