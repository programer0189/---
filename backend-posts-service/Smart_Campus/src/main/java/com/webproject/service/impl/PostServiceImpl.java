package com.webproject.service.impl;

import com.webproject.mapper.PostMapper;
import com.webproject.pojo.Post;
import com.webproject.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import com.webproject.pojo.Result;

@Slf4j
@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostMapper postMapper;

    @Override
    public List<Post> getById(Integer postId) {
        log.info("查询帖子{}", postId);
        return postMapper.getByPostId(postId);
    }

    @Override
    @Transactional
    public void addPost(Post post) {
        // 1. 设置创建和更新时间
        post.setCreateTime(LocalDateTime.now());
        post.setUpdateTime(LocalDateTime.now());
        
        // 2. 设置默认审核状态为待审核
        post.setJudgeStatus(Post.JUDGE_STATUS_PENDING);
        
        // 3. 保存帖子
        postMapper.insert(post);
        
        log.info("用户{}发布帖子{}: {}，状态：待审核", post.getUserId(), post.getId(), post.getTitle());
    }

    @Override
    @Transactional
    public void deletePost(Integer postId, Integer userId) {
        // 1. 查询帖子信息
        Post post = postMapper.getById(postId);
        if (post == null) {
            log.warn("帖子{}不存在", postId);
            return;
        }

        // 2. 检查权限（只有帖子作者能删除）
        if (!post.getUserId().equals(userId)) {
            log.warn("用户{}尝试删除不属于自己的帖子{}", userId, postId);
            return;
        }

        // 3. 删除帖子
        postMapper.deleteById(postId);

        log.info("用户{}删除帖子{}", userId, postId);
    }

    @Override
    @Transactional
    public void updatePost(Integer postId, Post post) {
        // 1. 查询原帖子信息
        Post originalPost = postMapper.getById(postId);
        if (originalPost == null) {
            log.warn("帖子{}不存在", postId);
            return;
        }
        
        // 2. 检查权限（只有帖子作者能编辑）
        if (!originalPost.getUserId().equals(post.getUserId())) {
            log.warn("用户{}尝试编辑不属于自己的帖子{}", post.getUserId(), postId);
            return;
        }
        
        // 3. 设置更新字段
        post.setId(postId);
        post.setCreateTime(originalPost.getCreateTime());
        post.setUpdateTime(LocalDateTime.now());
        post.setStatus(originalPost.getStatus());
        
        // 4. 设置审核状态为待审核（因为内容有更新）
        post.setJudgeStatus(Post.JUDGE_STATUS_PENDING);
        
        // 5. 更新帖子
        postMapper.update(post);
        
        log.info("用户{}更新帖子{}: {}，状态：待审核", post.getUserId(), postId, post.getTitle());
    }

    @Override
    @Transactional
    public void deleteByUserId(Integer userId) {
        // 删除用户的所有帖子
        postMapper.deleteByUserId(userId);
        log.info("删除用户{}的所有帖子", userId);
    }

    @Override
    public Integer countByUserId(Integer userId) {
        return postMapper.countByUserId(userId);
    }

    @Override
    public Integer countByType(Integer type) {
        return postMapper.countByType(type);
    }

    @Override
    public List<Post> getByType(Integer type) {
        log.info("查询类型{}的帖子", type);
        return postMapper.getByType(type);
    }

    @Override
    public List<Post> getByUserId(Integer userId) {
        log.info("查询用户{}的帖子", userId);
        return postMapper.getByUserId(userId);
    }

    @Override
    public List<Post> getAllPostsForAdmin() {
        log.info("管理员查询所有帖子（包含所有审核状态）");
        return postMapper.getAllPostsForAdmin();
    }

    @Override
    public List<Post> getPendingPosts() {
        log.info("获取待审核的帖子");
        return postMapper.getPendingPosts(Post.JUDGE_STATUS_PENDING);
    }
    

    
    @Override
    @Transactional
    public void approvePost(Integer postId, Integer adminId) {
        log.info("管理员{}通过帖子{}", adminId, postId);
        
        Post post = new Post();
        post.setId(postId);
        post.setJudgeStatus(Post.JUDGE_STATUS_APPROVED);
        post.setJudgerId(adminId);
        post.setJudgeTime(LocalDateTime.now());
        post.setUpdateTime(LocalDateTime.now());
        
        postMapper.updateJudgeStatus(post);
        log.info("帖子{}审核通过", postId);
    }
    
    @Override
    @Transactional
    public void rejectPost(Integer postId, Integer adminId, String reason) {
        log.info("管理员{}拒绝帖子{}，原因：{}", adminId, postId, reason);
        
        Post post = new Post();
        post.setId(postId);
        post.setJudgeStatus(Post.JUDGE_STATUS_REJECTED);
        post.setJudgerId(adminId);
        post.setJudgeTime(LocalDateTime.now());
        post.setRejectReason(reason);
        post.setUpdateTime(LocalDateTime.now());
        
        postMapper.updateJudgeStatus(post);
        log.info("帖子{}审核拒绝，原因：{}", postId, reason);
    }
    
    @Override
    @Transactional
    public void deletePostByAdmin(Integer postId, Integer adminId, String reason) {
        log.info("管理员{}删除帖子{}，原因：{}", adminId, postId, reason != null ? reason : "无");
        
        Post post = new Post();
        post.setId(postId);
        post.setJudgeStatus(Post.JUDGE_STATUS_DELETED);
        post.setJudgerId(adminId);
        post.setJudgeTime(LocalDateTime.now());
        post.setRejectReason(reason != null ? reason : "管理员删除");
        post.setUpdateTime(LocalDateTime.now());
        
        postMapper.updateJudgeStatus(post);
        log.info("帖子{}已被管理员{}删除", postId, adminId);
    }
    
    @Override
    public Result getPostStatistics() {
        try {
            Map<String, Integer> statistics = postMapper.getPostStatusCount();
            return Result.success(statistics);
        } catch (Exception e) {
            log.error("获取统计失败", e);
            return Result.error("获取统计信息失败");
        }
    }

}

