package com.webproject.service.impl;

import com.webproject.mapper.LikeMapper;
import com.webproject.pojo.Like;
import com.webproject.service.LikeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
public class LikeServiceImpl implements LikeService {
    
    @Autowired
    private LikeMapper likeMapper;
    
    @Override
    @Transactional
    public void addLike(Like like) {
        // 1. 设置创建时间
        like.setCreateTime(LocalDateTime.now());
        
        // 2. 保存点赞
        likeMapper.insert(like);
        
        log.info("用户{}为帖子{}添加点赞", like.getUserId(), like.getPostId());
    }
    
    @Override
    @Transactional
    public void deleteLike(Integer postId, Integer userId) {
        // 删除点赞
        likeMapper.delete(postId, userId);
        
        log.info("用户{}取消帖子{}的点赞", userId, postId);
    }
    
    @Override
    public Boolean isLiked(Integer postId, Integer userId) {
        // 查询用户是否点赞
        Like like = likeMapper.getByPostIdAndUserId(postId, userId);
        return like != null;
    }
    
    @Override
    public Integer countByPostId(Integer postId) {
        return likeMapper.countByPostId(postId);
    }
    
    @Override
    public Integer countByUserId(Integer userId) {
        return likeMapper.countByUserId(userId);
    }
    
    @Override
    public Integer countReceivedLikesByUserId(Integer userId) {
        return likeMapper.countReceivedLikesByUserId(userId);
    }
}
