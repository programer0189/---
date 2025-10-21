# 新增评论接口文档

## 已添加的新接口

### 1. 分页查询所有用户的评论
**接口地址：** `GET /comments/admin/all/page`

**请求参数：**
- `page` (可选): 页码，默认为1
- `pageSize` (可选): 每页数量，默认为10

**响应示例：**
```json
{
  "code": 1,
  "msg": "success",
  "data": {
    "pageNum": 1,
    "pageSize": 10,
    "size": 10,
    "total": 25,
    "pages": 3,
    "list": [
      {
        "id": 1,
        "postId": 1,
        "userId": 1,
        "content": "这是一条评论",
        "createTime": "2025-01-20T10:30:00",
        "username": "张三",
        "userAvatar": "https://example.com/avatar.jpg"
      }
    ]
  }
}
```

### 2. 删除帖子的评论
**接口地址：** `DELETE /comments/admin/post/{postId}`

**路径参数：**
- `postId`: 帖子ID

**响应示例：**
```json
{
  "code": 1,
  "msg": "success",
  "data": "帖子评论删除成功"
}
```

### 3. 获取所有评论数量
**接口地址：** `GET /comments/admin/count`

**响应示例：**
```json
{
  "code": 1,
  "msg": "success",
  "data": 25
}
```

## 技术实现

### 分页查询实现
- 使用 **PageHelper** 进行分页处理
- 在Controller层调用 `PageHelper.startPage(page, pageSize)`
- 返回 `PageInfo<Comment>` 对象，包含完整的分页信息

### 数据库查询
- `getAllComments`: 查询所有评论，关联用户信息
- `getAllCommentsCount`: 统计所有评论数量
- 按创建时间倒序排列，最新的评论在前

### 权限控制
- 所有新增接口都使用 `/admin/` 路径前缀
- 建议在实际使用时添加管理员权限验证

## 测试建议

### 1. 测试分页查询
```bash
# 查询第1页，每页10条
GET /comments/admin/all/page?page=1&pageSize=10

# 查询第2页，每页5条
GET /comments/admin/all/page?page=2&pageSize=5
```

### 2. 测试删除帖子评论
```bash
# 删除帖子ID为1的所有评论
DELETE /comments/admin/post/1
```

### 3. 测试获取评论总数
```bash
# 获取所有评论数量
GET /comments/admin/count
```

## 注意事项

1. **分页参数验证**: 建议添加对page和pageSize的合理性验证
2. **权限控制**: 这些接口应该只允许管理员访问
3. **事务处理**: 删除操作已添加事务注解
4. **日志记录**: 所有操作都有详细的日志记录

## 数据库表结构

评论表 `comments` 包含以下字段：
- `id`: 评论ID (主键，自增)
- `post_id`: 帖子ID
- `user_id`: 评论用户ID
- `content`: 评论内容 (varchar 500)
- `create_time`: 评论时间 (datetime，默认当前时间)
