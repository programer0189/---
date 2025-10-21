# 移动端客户端统计功能更新

## 概述
本次更新为校智通系统增加了移动端客户端的统计功能，包括评论管理和支持图片的帖子管理。

## 新增功能

### 1. 评论管理
- 新增评论管理页面 (`/admin/comment`)
- 支持分页查看所有用户评论
- 支持编辑和删除评论
- 显示评论统计信息（总评论数）

### 2. 帖子管理增强
- 支持图片显示功能
- 在帖子列表中添加图片列
- 支持图片预览
- 使用新的8081端口后端API

### 3. 新的后端支持
- 新增8081端口后端服务
- 支持图片上传和存储（阿里云OSS）
- 新增评论管理接口
- 更新Post实体类，增加image字段

## 技术架构

### 前端更新
- 新增 `src/utils/request8081.js` - 访问8081端口的请求工具
- 新增 `src/api/comment.js` - 评论管理API
- 新增 `src/api/post8081.js` - 新版本帖子管理API
- 新增 `src/views/comment/index.vue` - 评论管理组件
- 更新 `src/views/news/index.vue` - 帖子管理支持图片
- 更新 `src/views/layout/index.vue` - 添加评论管理菜单
- 更新 `src/router/index.js` - 添加评论管理路由

### 后端更新
- 新增8081端口服务
- 更新Post实体类，增加image字段
- 新增Comment实体类
- 新增评论管理Controller和Service
- 配置阿里云OSS用于图片存储

### 配置更新
- 更新 `vite.config.js`，添加8081端口代理
- 数据库结构更新，支持图片字段

## 使用方法

### 1. 启动服务
确保8081端口的后端服务已启动

### 2. 访问评论管理
- 登录管理员账户
- 在左侧菜单"我在校园"下选择"评论管理"
- 可以查看、编辑、删除评论

### 3. 查看带图片的帖子
- 在"论坛管理"中切换"所有帖子"模式
- 可以看到帖子是否包含图片
- 点击图片可以预览

## 数据库变更

### Post表新增字段
```sql
ALTER TABLE posts ADD COLUMN image VARCHAR(500) COMMENT '帖子图片URL';
```

### 新增Comment表
```sql
CREATE TABLE comments (
  id INT PRIMARY KEY AUTO_INCREMENT,
  post_id INT NOT NULL COMMENT '帖子ID',
  user_id INT NOT NULL COMMENT '用户ID',
  content TEXT NOT NULL COMMENT '评论内容',
  create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  FOREIGN KEY (post_id) REFERENCES posts(id),
  FOREIGN KEY (user_id) REFERENCES users(id)
);
```

## 注意事项

1. 确保8081端口后端服务正常运行
2. 确保阿里云OSS配置正确
3. 图片上传功能需要在前端实现（本次更新仅支持显示）
4. 评论管理需要管理员权限

## 后续开发建议

1. 实现图片上传功能
2. 添加评论审核功能
3. 增加更多统计图表
4. 优化移动端适配
