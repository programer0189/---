package com.webproject.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Like {
    private Integer postId; // 帖子的id
    private Integer userId; // 用户的id
    private LocalDateTime createTime; // 点赞的创建时间
}
