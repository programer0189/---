package com.webproject.mapper;

import com.webproject.pojo.Like;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface LikeMapper {
    
    void insert(Like like);
    
    void delete(@org.apache.ibatis.annotations.Param("postId") Integer postId, @org.apache.ibatis.annotations.Param("userId") Integer userId);
    
    Like getByPostIdAndUserId(@org.apache.ibatis.annotations.Param("postId") Integer postId, @org.apache.ibatis.annotations.Param("userId") Integer userId);
    
    Integer countByPostId(Integer postId);
    
    Integer countByUserId(Integer userId);
    
    Integer countReceivedLikesByUserId(Integer userId);
    
    List<Like> getByPostId(Integer postId);
}
