# 社交平台API接口文档

## 基础信息
- 基础URL: `http://localhost:8080`
- 请求格式: JSON
- 响应格式: JSON

## 统一响应格式
```json
{
  "code": 1,        // 1: 成功, 0: 失败
  "msg": "success", // 响应消息
  "data": {}        // 响应数据
}
```

## 1. 帖子管理接口

### 1.1 分页查询帖子列表
- **接口**: `GET /posts`
- **参数**: 
  - `page`: 页码（默认1）
  - `pageSize`: 每页大小（默认10）
  - `type`: 帖子类型（可选）
  - `title`: 标题关键词（可选）
  - `userId`: 用户ID（可选）
  - `begin`: 开始日期（可选）
  - `end`: 结束日期（可选）

### 1.2 查询帖子详情
- **接口**: `GET /posts/{id}`
- **参数**: `id` - 帖子ID

### 1.3 新增帖子
- **接口**: `POST /posts`
- **请求体**:
```json
{
  "userId": 1,
  "type": 1,
  "title": "帖子标题",
  "content": "帖子内容",
  "status": 0
}
```

### 1.4 更新帖子
- **接口**: `PUT /posts`
- **请求体**: 同新增帖子，需要包含id字段

### 1.5 删除帖子
- **接口**: `DELETE /posts/{id}`
- **参数**: `id` - 帖子ID

### 1.6 批量删除帖子
- **接口**: `DELETE /posts?ids=1,2,3`
- **参数**: `ids` - 帖子ID列表，逗号分隔

### 1.7 点赞/取消点赞
- **接口**: `POST /posts/{postId}/like?userId={userId}`
- **参数**: 
  - `postId` - 帖子ID
  - `userId` - 用户ID

### 1.8 添加评论
- **接口**: `POST /posts/{postId}/comment?userId={userId}&content={content}`
- **参数**:
  - `postId` - 帖子ID
  - `userId` - 用户ID
  - `content` - 评论内容

### 1.9 删除评论
- **接口**: `DELETE /posts/comment/{commentId}?userId={userId}`
- **参数**:
  - `commentId` - 评论ID
  - `userId` - 用户ID

### 1.10 获取用户统计信息
- **接口**: `GET /posts/user/{userId}/stats`
- **参数**: `userId` - 用户ID

### 1.11 根据类型查询帖子
- **接口**: `GET /posts/type/{type}`
- **参数**: `type` - 帖子类型

### 1.12 根据用户ID查询帖子
- **接口**: `GET /posts/user/{userId}`
- **参数**: `userId` - 用户ID

## 2. 社交平台分类接口

### 2.1 表白板块
- **查询**: `GET /confession`
- **发布**: `POST /confession`

### 2.2 代取快递板块
- **查询**: `GET /express`
- **发布**: `POST /express`

### 2.3 学习交流板块
- **查询**: `GET /study`
- **发布**: `POST /study`

### 2.4 活动组织板块
- **查询**: `GET /activity`
- **发布**: `POST /activity`

### 2.5 失物招领板块
- **查询**: `GET /lost`
- **发布**: `POST /lost`

### 2.6 闲聊灌水板块
- **查询**: `GET /chat`
- **发布**: `POST /chat`

## 3. 帖子类型说明
- `1`: 表白
- `2`: 代取快递
- `3`: 学习交流
- `4`: 活动组织
- `5`: 失物招领
- `6`: 闲聊灌水

## 4. 使用示例

### 4.1 发布表白帖子
```bash
curl -X POST http://localhost:8080/confession \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "title": "表白小红",
    "content": "小红，我喜欢你！",
    "status": 0
  }'
```

### 4.2 查询表白帖子
```bash
curl "http://localhost:8080/confession?page=1&pageSize=10"
```

### 4.3 点赞帖子
```bash
curl -X POST "http://localhost:8080/posts/1/like?userId=2"
```

### 4.4 添加评论
```bash
curl -X POST "http://localhost:8080/posts/1/comment?userId=2&content=支持你！"
```

## 5. 注意事项
1. 所有接口都需要用户认证（JWT Token）
2. 用户ID必须存在于emp表中
3. 删除操作会级联删除相关的评论和点赞记录
4. 点赞和评论数会自动更新到帖子表中
5. 支持匿名发布（status=1）
