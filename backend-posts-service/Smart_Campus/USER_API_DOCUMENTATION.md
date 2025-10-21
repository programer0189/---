# 用户管理API接口文档

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

## 1. 新增用户接口

### 1.1 接口信息
- **接口**: `POST /users`
- **功能**: 新增用户
- **权限**: 需要管理员权限

### 1.2 请求参数
```json
{
  "username": "新用户名",
  "password": "密码",
  "phone": "13800138000",
  "role": "user",
  "image": "头像URL",
  "gender": "男"
}
```

### 1.3 字段说明
| 字段名 | 类型 | 必填 | 说明 | 示例值 |
|--------|------|------|------|--------|
| username | String | 是 | 用户名，唯一 | "zhangsan" |
| password | String | 是 | 密码 | "123456" |
| phone | String | 是 | 手机号，唯一 | "13800138000" |
| role | String | 否 | 角色，默认"user" | "user" 或 "admin" |
| image | String | 否 | 头像URL | "https://example.com/avatar.jpg" |
| gender | String | 否 | 性别 | "男"、"女"、"其他" |

### 1.4 响应示例

#### 成功响应
```json
{
  "code": 1,
  "msg": "success",
  "data": null
}
```

#### 失败响应
```json
{
  "code": 0,
  "msg": "用户名已存在",
  "data": null
}
```

### 1.5 业务逻辑
1. **用户名唯一性检查**: 检查用户名是否已存在
2. **手机号唯一性检查**: 检查手机号是否已存在
3. **默认值设置**: 
   - 如果没有指定role，默认设置为"user"
   - 自动设置创建时间和更新时间
4. **数据验证**: 验证必填字段

### 1.6 错误处理
| 错误情况 | 错误消息 |
|----------|----------|
| 用户名已存在 | "用户名已存在" |
| 手机号已存在 | "手机号已存在" |
| 必填字段缺失 | 根据具体字段返回相应错误 |

## 2. 其他用户接口

### 2.1 查询用户
- **根据ID查询**: `GET /users/{id}`
- **查询所有用户**: `GET /users`
- **根据用户名查询**: `GET /users/username/{username}`
- **根据手机号查询**: `GET /users/phone/{phone}`
- **根据角色查询**: `GET /users/role/{role}`

### 2.2 更新用户
- **更新用户信息**: `PUT /users/{id}`

### 2.3 删除用户
- **删除用户**: `DELETE /users/{id}`

## 3. 数据库表结构

### user表字段
```sql
CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `username` varchar(20) NOT NULL COMMENT '用户名',
  `password` varchar(64) NOT NULL COMMENT '密码',
  `phone` char(11) NOT NULL COMMENT '手机号',
  `role` varchar(10) NOT NULL DEFAULT 'user' COMMENT '角色：admin 或 user',
  `image` varchar(255) DEFAULT NULL COMMENT '头像',
  `gender` varchar(3) DEFAULT NULL COMMENT '性别',
  `create_time` date DEFAULT NULL COMMENT '创建时间',
  `update_time` date DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  UNIQUE KEY `uk_phone` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表';
```

## 4. 使用示例

### 4.1 新增普通用户
```bash
curl -X POST http://localhost:8080/users \
  -H "Content-Type: application/json" \
  -d '{
    "username": "lisi",
    "password": "123456",
    "phone": "13800138004",
    "gender": "女"
  }'
```

### 4.2 新增管理员用户
```bash
curl -X POST http://localhost:8080/users \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin2",
    "password": "admin123",
    "phone": "13800138005",
    "role": "admin",
    "gender": "男"
  }'
```

## 5. 注意事项

1. **权限控制**: 新增用户接口可能需要管理员权限
2. **数据验证**: 前端应该进行基本的数据验证
3. **密码安全**: 实际生产环境中应该对密码进行加密
4. **性别字段**: 性别字段为可选，支持"男"、"女"、"其他"等值
5. **唯一性约束**: 用户名和手机号在数据库层面有唯一性约束

## 6. 测试建议

1. **正常流程测试**: 测试各种字段组合的新增用户
2. **异常流程测试**: 测试重复用户名、重复手机号等错误情况
3. **边界值测试**: 测试字段长度限制、特殊字符等
4. **权限测试**: 测试不同角色的用户访问权限
