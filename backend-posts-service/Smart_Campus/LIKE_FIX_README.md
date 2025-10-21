# 点赞功能修复说明

## 问题描述
前端在显示用户获赞数时出现错误：当一个用户给别人的帖子点赞时，自己的获赞数会增加，这是不正确的。

## 问题分析
原有的 `countByUserId` 方法统计的是**用户发出的点赞数量**（即用户给其他帖子的点赞次数），而不是**用户获得的点赞数量**（即用户自己的帖子被点赞的次数）。

### 原有逻辑
```sql
-- 统计用户发出的点赞数
SELECT COUNT(*) FROM likes WHERE user_id = #{userId}
```

### 正确的逻辑应该是
```sql
-- 统计用户获得的点赞数（用户发布的帖子被点赞的总数）
SELECT COUNT(*) FROM likes l
INNER JOIN posts p ON l.post_id = p.id
WHERE p.user_id = #{userId}
```

## 修复方案

### 1. 新增方法
- 在 `LikeMapper.xml` 中添加 `countReceivedLikesByUserId` 方法
- 在 `LikeMapper` 接口中添加对应方法声明
- 在 `LikeService` 接口中添加对应方法声明
- 在 `LikeServiceImpl` 中实现该方法

### 2. 新增接口
- 新增 `/likes/user/{userId}/received-count` 接口，用于获取用户获得的点赞数
- 保留原有的 `/likes/user/{userId}/count` 接口，但明确其含义为"用户发出的点赞数"

### 3. 接口说明

#### 获取用户发出的点赞数
- **接口**: `GET /likes/user/{userId}/count`
- **含义**: 统计用户给其他帖子的点赞次数
- **用途**: 可用于统计用户的活跃度

#### 获取用户获得的点赞数
- **接口**: `GET /likes/user/{userId}/received-count`
- **含义**: 统计用户发布的帖子被点赞的总数
- **用途**: 显示用户的受欢迎程度，这是前端应该使用的接口

## 前端修改建议
前端需要将获取用户获赞数的接口从：
```
GET /likes/user/{userId}/count
```
改为：
```
GET /likes/user/{userId}/received-count
```

## 数据库表结构
确保数据库中有以下表：
- `likes` 表：存储点赞记录（post_id, user_id, create_time）
- `posts` 表：存储帖子信息（id, user_id, ...）
- `user` 表：存储用户信息

## 注意事项
1. 原有的 `countByUserId` 方法仍然保留，用于统计用户发出的点赞数
2. 新增的 `countReceivedLikesByUserId` 方法通过 JOIN 查询来正确统计用户获得的点赞数
3. 前端需要根据实际需求选择合适的接口
