package com.webproject.mapper;

import com.webproject.pojo.Comment;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface CommentMapper {
    
    List<Comment> getByPostId(Integer postId);
    
    void insert(Comment comment);
    
    void update(Comment comment);
    
    void deleteById(Integer id);
    
    void deleteByPostId(Integer postId);
    
    Integer countByUserId(Integer userId);
    
    Integer countByPostId(Integer postId);
    
    Comment getById(Integer id);
}
