package com.webproject.controller;

import com.webproject.pojo.Post;
import com.webproject.pojo.Result;
import com.webproject.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

//帖子的控制器
@Slf4j
@RestController
@RequestMapping("/posts")
public class PostController {
    
    @Autowired
    private PostService postService;
    
    // 支持查询参数的方式
    @GetMapping
    public Result getPostsByPostIdParam(@RequestParam Integer postId) {
        log.info("通过查询参数获取帖子{}", postId);
        List<Post> posts = postService.getById(postId);
        return Result.success(posts);
    }
    
    @GetMapping("/{postId}")
    public Result getPostById(@PathVariable Integer postId) {
        log.info("获取帖子{}", postId);
        List<Post> posts = postService.getById(postId);
        return Result.success(posts);
    }
    
    //添加帖子
    @PostMapping
    public Result addPost(@RequestBody Post post) {
        log.info("添加帖子{}", post);
        postService.addPost(post);
        return Result.success();
    }
    
    //删除帖子
    @DeleteMapping("/{postId}")
    public Result deletePost(@PathVariable Integer postId, @RequestParam Integer userId) {
        log.info("删除id{}的帖子", postId);
        postService.deletePost(postId, userId);
        return Result.success();
    }
    
    //修改帖子
    @PutMapping("/{postId}")
    public Result updatePost(@PathVariable Integer postId, @RequestBody Post post) {
        log.info("更新id{}的帖子", postId);
        postService.updatePost(postId, post);
        return Result.success();
    }
    
    //获取用户发帖子的总数
    @GetMapping("/user/{userId}/count")
    public Result getUserPostCount(@PathVariable Integer userId) {
        log.info("获取用户{}的帖子总数", userId);
        Integer count = postService.countByUserId(userId);
        return Result.success(count);
    }
    
    //获取某个类型的帖子总数
    @GetMapping("/type/{type}/count")
    public Result getTypePostCount(@PathVariable Integer type) {
        log.info("获取类型{}的帖子总数", type);
        Integer count = postService.countByType(type);
        return Result.success(count);
    }
    
    //获取某个类型的所有帖子
    @GetMapping("/type/{type}")
    public Result getPostsByType(@PathVariable Integer type) {
        log.info("获取类型{}的所有帖子", type);
        List<Post> posts = postService.getByType(type);
        return Result.success(posts);
    }
    
    //获取某个用户的所有帖子
    @GetMapping("/user/{userId}")
    public Result getPostsByUserId(@PathVariable Integer userId) {
        log.info("获取用户{}的所有帖子", userId);
        List<Post> posts = postService.getByUserId(userId);
        return Result.success(posts);
    }

    // 审核功能 - 获取待审核的帖子（管理员使用）
    @GetMapping("/admin/pending")
    public Result getPendingPosts() {
        log.info("获取待审核的帖子");
        List<Post> posts = postService.getPendingPosts();
        return Result.success(posts);
    }
    
    // 审核功能 - 获取待审核的帖子（分页）
    @GetMapping("/admin/pending/page")
    public Result getPendingPostsWithPage(@RequestParam(defaultValue = "1") Integer page, 
                                        @RequestParam(defaultValue = "10") Integer pageSize) {
        log.info("获取待审核帖子分页数据：第{}页，每页{}条", page, pageSize);
        PageHelper.startPage(page, pageSize);
        List<Post> posts = postService.getPendingPosts();
        PageInfo<Post> pageInfo = new PageInfo<>(posts);
        return Result.success(pageInfo);
    }
    
    // 审核功能 - 通过帖子
    @PutMapping("/admin/{postId}/approve")
    public Result approvePost(@PathVariable Integer postId, @RequestParam Integer adminId) {
        log.info("管理员{}通过帖子{}", adminId, postId);
        postService.approvePost(postId, adminId);
        return Result.success("帖子审核通过");
    }
    
    // 审核功能 - 拒绝帖子
    @PutMapping("/admin/{postId}/delete")
    public Result rejectPost(@PathVariable Integer postId, @RequestParam Integer adminId, @RequestParam String reason) {
        log.info("管理员{}拒绝帖子{}，原因：{}", adminId, postId, reason);
        postService.rejectPost(postId, adminId, reason);
        return Result.success("帖子审核拒绝");
    }
    
    // 审核功能 - 删除帖子（逻辑删除）
    @DeleteMapping("/admin/{postId}")
    public Result deletePostByAdmin(@PathVariable Integer postId, @RequestParam Integer adminId, @RequestParam(required = false) String reason) {
        log.info("管理员{}删除帖子{}，原因：{}", adminId, postId, reason != null ? reason : "无");
        postService.deletePostByAdmin(postId, adminId, reason);
        return Result.success("帖子已删除");
    }
    
    // 获取帖子统计
    @GetMapping("/admin/statistics")
    public Result getPostStatistics() {
        return postService.getPostStatistics();
    }

    // 管理员查询所有帖子（不需要审核状态条件）
    @GetMapping("/admin/all")
    public Result getAllPostsForAdmin() {
        log.info("管理员查询所有帖子（包含所有审核状态）");
        List<Post> posts = postService.getAllPostsForAdmin();
        return Result.success(posts);
    }

    // 管理员分页查询所有帖子（不需要审核状态条件）
    @GetMapping("/admin/all/page")
    public Result getAllPostsForAdminWithPage(@RequestParam(defaultValue = "1") Integer page,
                                            @RequestParam(defaultValue = "10") Integer pageSize) {
        log.info("管理员分页查询所有帖子：第{}页，每页{}条", page, pageSize);
        PageHelper.startPage(page, pageSize);
        List<Post> posts = postService.getAllPostsForAdmin();
        PageInfo<Post> pageInfo = new PageInfo<>(posts);
        return Result.success(pageInfo);
    }

}
