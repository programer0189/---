package com.webproject.controller;

import com.webproject.pojo.Comment;
import com.webproject.pojo.Result;
import com.webproject.service.CommentService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//评论的控制器
@Slf4j
@RestController
@RequestMapping("/comments")
public class CommentController
{
    @Autowired
    private CommentService commentService;
    
    // 添加一个通用的GET方法，支持查询参数
    @GetMapping
    public Result getCommentsByPostIdParam(@RequestParam Integer postId)
    {
        log.info("通过查询参数获取帖子{}的评论", postId);
        List<Comment> comments = commentService.getById(postId);
        return Result.success(comments);
    }
    

    //添加评论
    @PostMapping
    public Result addComment(@RequestBody Comment comment)
    {
        log.info("添加评论{}", comment);
        commentService.addComment(comment);
        return Result.success();
    }
    
    //删除评论
    @DeleteMapping("/{commentId}")
    public Result deleteComment(@PathVariable Integer commentId, @RequestParam Integer userId)
    {
        log.info("删除id{}的评论", commentId);
        commentService.deleteComment(commentId, userId);
        return Result.success();
    }

    @PutMapping("/{commentId}")
    public Result updateComment(@PathVariable Integer commentId, @RequestBody Comment comment)
    {
        log.info("更新id{}的评论", commentId);
        commentService.updateComment(commentId, comment);
        return Result.success();
    }
    
    //获取用户发帖子的总评论数
    @GetMapping("/user/{userId}/count")
    public Result getUserCommentCount(@PathVariable Integer userId)
    {
        log.info("获取用户{}的评论总数", userId);
        Integer count = commentService.countByUserId(userId);
        return Result.success(count);
    }
    
    //获取某个帖子的总评论数
    @GetMapping("/post/{postId}/count")
    public Result getPostCommentCount(@PathVariable Integer postId)
    {
        log.info("获取帖子{}的评论总数", postId);
        Integer count = commentService.countByPostId(postId);
        return Result.success(count);
    }
    
    // 新增接口：分页查询所有用户的评论
    @GetMapping("/admin/all/page")
    public Result getAllCommentsWithPage(@RequestParam(defaultValue = "1") Integer page,
                                        @RequestParam(defaultValue = "10") Integer pageSize) {
        log.info("分页查询所有评论：第{}页，每页{}条", page, pageSize);
        PageHelper.startPage(page, pageSize);
        List<Comment> comments = commentService.getAllCommentsWithPage(page, pageSize);
        PageInfo<Comment> pageInfo = new PageInfo<>(comments);
        return Result.success(pageInfo);
    }
    

    
    // 新增接口：获取所有评论数量
    @GetMapping("/admin/count")
    public Result getAllCommentsCount() {
        log.info("获取所有评论数量");
        Integer count = commentService.getAllCommentsCount();
        return Result.success(count);
    }
}
