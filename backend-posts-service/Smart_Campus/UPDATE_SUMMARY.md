# 数据库更新总结

## 已完成的更新

### 1. 数据库配置更新
- ✅ 将 `src/main/resources/application.yml` 中的数据库名称从 `tlias` 改为 `school-chat`
- ✅ 将 `src/main/java/com/webproject/utils/LogFileToDbMain.java` 中的数据库名称从 `tlias` 改为 `school-chat`

### 2. Post实体类更新
- ✅ 在 `src/main/java/com/webproject/pojo/Post.java` 中添加了 `image` 字段
- ✅ 字段类型：`private String image;`
- ✅ 添加了相应的注释说明：`// 帖子的图片`

### 3. PostMapper.xml更新
- ✅ 在 `insert` 语句中添加了 `image` 字段支持
- ✅ 在 `update` 语句中添加了对 `image` 字段的更新支持
- ✅ 所有相关的SQL操作现在都支持 `image` 字段

### 4. 接口层自动支持
- ✅ `PostController` 中的 `addPost` 和 `updatePost` 方法自动支持 `image` 字段
- ✅ `PostService` 接口和 `PostServiceImpl` 实现类自动支持 `image` 字段
- ✅ 无需额外修改，MyBatis自动映射所有字段

## 数据库字段对应关系

根据提供的数据库表结构，Post实体类现在包含以下字段：

| 实体类字段 | 数据库字段 | 类型 | 说明 |
|-----------|-----------|------|------|
| id | id | int | 帖子ID，主键，自增 |
| userId | user_id | int | 用户ID |
| type | type | int | 帖子类型：1表白 2代取快递 3学习交流 4活动组织 5失物招领 6闲聊灌水 |
| title | title | varchar(100) | 帖子标题 |
| content | content | text | 帖子内容 |
| status | status | tinyint(1) | 是否匿名：0否 1是 |
| createTime | create_time | datetime | 发布时间 |
| updateTime | update_time | datetime | 更新时间 |
| judgeStatus | judge_status | tinyint | 帖子审核状态：0待审核 1通过 2拒绝 3删除 |
| judgerId | judger_id | int | 帖子的审核员 |
| judgeTime | judge_time | datetime | 审核时间 |
| rejectReason | reject_reason | varchar(500) | 拒绝的原因 |
| **image** | **image** | **text** | **帖子的图片** |

## 测试建议

### 1. 创建带图片的帖子
```bash
POST /posts
Content-Type: application/json

{
  "userId": 1,
  "type": 1,
  "title": "测试帖子",
  "content": "这是一个测试帖子",
  "status": 0,
  "image": "https://example.com/image.jpg"
}
```

### 2. 更新帖子图片
```bash
PUT /posts/{postId}
Content-Type: application/json

{
  "userId": 1,
  "title": "更新后的标题",
  "content": "更新后的内容",
  "image": "https://example.com/new-image.jpg"
}
```

### 3. 查询帖子验证
- 验证 `image` 字段是否正确保存和返回
- 验证更新操作是否正确处理 `image` 字段

## 注意事项

1. **数据库准备**：确保数据库 `school-chat` 已创建，且 `posts` 表包含 `image` 字段
2. **图片存储**：`image` 字段类型为 TEXT，可以存储较长的图片URL或Base64数据
3. **前端更新**：前端需要相应更新以支持图片上传和显示功能
4. **日志文件**：logback.xml中的日志文件名模式保持为 `tlias`，不影响数据库连接

## 下一步建议

1. 测试数据库连接是否正常
2. 测试Post的增删改查操作
3. 验证image字段的存储和读取
4. 根据需要在PostController中添加图片上传相关的接口
5. 更新前端代码以支持图片功能
