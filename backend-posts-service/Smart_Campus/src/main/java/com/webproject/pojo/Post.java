package com.webproject.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

//这里定义帖子实体类
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Post {
    private Integer id; // 帖子ID
    private Integer userId; // 用户ID
    private Integer type; // 帖子类型：1表白 2代取快递 3学习交流 4活动组织 5失物招领 6闲聊灌水
    private String title; // 帖子标题
    private String content; // 帖子内容
    private Integer status; // 是否匿名：0否 1是
    private LocalDateTime createTime; // 发布时间
    private LocalDateTime updateTime; // 更新时间
    
    // 审核相关字段
    private Integer judgeStatus; // 审核状态：0待审核 1通过 2拒绝 3删除
    private Integer judgerId; // 审核员ID
    private LocalDateTime judgeTime; // 审核时间
    private String rejectReason; // 拒绝原因
    private String image; // 帖子的图片
    
    // 扩展字段（用于前端展示）
    private String username; // 用户名
    private String userAvatar; // 用户头像
    private Integer likes; // 点赞数
    private Integer comments; // 评论数
    private Boolean isLiked; // 当前用户是否点赞
    private Boolean showComments; // 是否显示评论区
    private String commentText; // 评论输入框内容
    private List<Comment> commentList; // 评论列表
    
    // 审核状态常量
    public static final int JUDGE_STATUS_PENDING = 0;   // 待审核
    public static final int JUDGE_STATUS_APPROVED = 1;  // 已通过
    public static final int JUDGE_STATUS_REJECTED = 2;  // 已拒绝
    public static final int JUDGE_STATUS_DELETED = 3;   // 已删除
}
