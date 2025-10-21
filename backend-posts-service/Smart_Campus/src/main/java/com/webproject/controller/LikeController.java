package com.webproject.controller;

import com.webproject.pojo.Like;
import com.webproject.pojo.Result;
import com.webproject.service.LikeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

//点赞的控制器
@Slf4j
@RestController
@RequestMapping("/likes")
public class LikeController {
    
    @Autowired
    private LikeService likeService;
    
    //添加点赞
    @PostMapping
    public Result addLike(@RequestBody Like like)
    {
        log.info("添加点赞{}", like);
        likeService.addLike(like);
        return Result.success();
    }
    
    //删除点赞
// 删除点赞
    @DeleteMapping("/{postId}")
    public Result deleteLike(@PathVariable Integer postId, @RequestParam Integer userId)
    {
        log.info("删除帖子{}用户{}的点赞", postId, userId);
        likeService.deleteLike(postId, userId);
        return Result.success();
    }
    
    //检查用户是否点赞
    @GetMapping("/check")
    public Result checkLike(@RequestParam Integer postId, @RequestParam Integer userId)
    {
        log.info("检查帖子{}用户{}是否点赞", postId, userId);
        Boolean isLiked = likeService.isLiked(postId, userId);
        return Result.success(isLiked);
    }
    
    //获取帖子的点赞总数
    @GetMapping("/post/{postId}/count")
    public Result getPostLikeCount(@PathVariable Integer postId)
    {
        log.info("获取帖子{}的点赞总数", postId);
        Integer count = likeService.countByPostId(postId);
        return Result.success(count);
    }
    
    //获取用户发出的点赞总数（用户给其他帖子的点赞数）
    @GetMapping("/user/{userId}/count")
    public Result getUserLikeCount(@PathVariable Integer userId)
    {
        log.info("获取用户{}发出的点赞总数", userId);
        Integer count = likeService.countByUserId(userId);
        return Result.success(count);
    }
    
    //获取用户获得的点赞总数（用户发布的帖子被点赞的总数）
    @GetMapping("/user/{userId}/received-count")
    public Result getUserReceivedLikeCount(@PathVariable Integer userId)
    {
        log.info("获取用户{}获得的点赞总数", userId);
        Integer count = likeService.countReceivedLikesByUserId(userId);
        return Result.success(count);
    }
}
