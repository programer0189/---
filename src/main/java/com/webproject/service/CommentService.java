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
}
