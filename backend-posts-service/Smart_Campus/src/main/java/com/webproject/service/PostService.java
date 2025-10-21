package com.webproject.service;

import com.webproject.pojo.Post;
import java.util.List;
import com.webproject.pojo.Result;

public interface PostService {
    
    List<Post> getById(Integer postId);
    
    void addPost(Post post);
    
    void deletePost(Integer postId, Integer userId);
    
    void updatePost(Integer postId, Post post);
    
    void deleteByUserId(Integer userId);
    
    Integer countByUserId(Integer userId);
    
    Integer countByType(Integer type);
    
    List<Post> getByType(Integer type);
    
    List<Post> getByUserId(Integer userId);
    
    // 管理员查询所有帖子（不需要审核状态条件）
    List<Post> getAllPostsForAdmin();
    
    // 审核相关方法
    List<Post> getPendingPosts();
    
    void approvePost(Integer postId, Integer adminId);
    
    // 审核功能 - 拒绝帖子
    void rejectPost(Integer postId, Integer adminId, String reason);
    
    // 审核功能 - 删除帖子（逻辑删除）
    void deletePostByAdmin(Integer postId, Integer adminId, String reason);
    
    // 获取帖子统计
    Result getPostStatistics();
}
