package com.webproject.mapper;

import com.webproject.pojo.Post;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

@Mapper
public interface PostMapper {
    
    List<Post> getByPostId(Integer postId);
    
    void insert(Post post);
    
    void update(Post post);
    
    void deleteById(Integer id);
    
    void deleteByUserId(Integer userId);
    
    Integer countByUserId(Integer userId);
    
    Integer countByType(Integer type);
    
    List<Post> getByType(Integer type);
    
    List<Post> getByUserId(Integer userId);
    
    Post getById(Integer id);
    
    // 管理员查询所有帖子（不需要审核状态条件）
    List<Post> getAllPostsForAdmin();
    
    // 获取待审核的帖子
    List<Post> getPendingPosts(Integer judgeStatus);
    
    // 更新帖子审核状态
    void updateJudgeStatus(Post post);
    
    // 获取各状态帖子数量统计
    Map<String, Integer> getPostStatusCount();
    
    // 获取帖子数据统计
    Map<String, Object> getPostData();
}
