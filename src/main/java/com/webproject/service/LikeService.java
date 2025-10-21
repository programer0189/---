package com.webproject.service;

import com.webproject.pojo.Like;

public interface LikeService {
    
    void addLike(Like like);
    
    void deleteLike(Integer postId, Integer userId);
    
    Boolean isLiked(Integer postId, Integer userId);
    
    Integer countByPostId(Integer postId);
    
    Integer countByUserId(Integer userId);
    
    Integer countReceivedLikesByUserId(Integer userId);
}
