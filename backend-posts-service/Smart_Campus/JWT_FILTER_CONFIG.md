# JWT过滤器配置说明

## 问题描述
原有的JWT过滤器拦截了所有请求，导致未登录用户无法访问注册、头像上传等必要接口，返回401错误。

## 解决方案
在TokenFilter中添加白名单机制，允许特定路径无需登录即可访问。

## 白名单配置

### 当前白名单路径
```java
private static final List<String> WHITE_LIST = Arrays.asList(
    "/user/login",      // 用户登录接口
    "/users",           // 用户注册接口
    "/upload",          // 文件上传接口（头像等）
    "/static",          // 静态资源
    "/favicon.ico",     // 网站图标
    "/error"            // 错误页面
);
```

### 白名单路径说明

| 路径 | 说明 | 用途 |
|------|------|------|
| `/user/login` | 用户登录接口 | 用户登录，获取JWT令牌 |
| `/users` | 用户注册接口 | 新用户注册，无需登录 |
| `/upload` | 文件上传接口 | 头像上传等文件操作 |
| `/static` | 静态资源 | CSS、JS、图片等静态文件 |
| `/favicon.ico` | 网站图标 | 浏览器标签页图标 |
| `/error` | 错误页面 | 系统错误页面 |

## 配置原理

### 1. 白名单检查逻辑
```java
private boolean isWhiteListPath(String requestURI) {
    // 检查是否包含白名单路径
    for (String whitePath : WHITE_LIST) {
        if (requestURI.contains(whitePath)) {
            return true;
        }
    }
    return false;
}
```

### 2. 请求处理流程
```
请求进入 → 检查白名单 → 白名单中？ → 是：直接放行
                                    ↓
                                  否：检查JWT令牌
                                    ↓
                              令牌有效？ → 是：放行
                                        ↓
                                      否：返回401
```

## 扩展配置

### 添加新的白名单路径
如果需要添加新的白名单路径，只需在`WHITE_LIST`中添加：

```java
private static final List<String> WHITE_LIST = Arrays.asList(
    "/login",           // 登录
    "/users",           // 用户注册
    "/upload",          // 文件上传
    "/static",          // 静态资源
    "/favicon.ico",     // 网站图标
    "/error",           // 错误页面
    "/public",          // 公开接口
    "/api/docs"         // API文档
);
```

### 支持通配符路径
如果需要支持更复杂的路径匹配，可以使用正则表达式：

```java
private boolean isWhiteListPath(String requestURI) {
    // 支持通配符的路径匹配
    if (requestURI.matches("/public/.*")) {
        return true;
    }
    
    // 原有的白名单检查
    for (String whitePath : WHITE_LIST) {
        if (requestURI.contains(whitePath)) {
            return true;
        }
    }
    return false;
}
```

## 安全考虑

### 1. 白名单路径安全
- 只将真正需要公开的接口加入白名单
- 避免将敏感接口（如用户信息、管理接口）加入白名单
- 定期审查白名单配置

### 2. 接口权限控制
- 白名单接口仍然可以在业务逻辑层面进行权限控制
- 例如：注册接口可以限制IP、频率等

### 3. 日志记录
- 记录所有白名单请求的访问日志
- 监控异常访问行为

## 测试验证

### 1. 测试白名单接口
```bash
# 测试注册接口（应该成功）
curl -X POST http://localhost:8080/users \
  -H "Content-Type: application/json" \
  -d '{"username":"test","password":"123456","phone":"13800138000"}'

# 测试头像上传（应该成功）
curl -X POST http://localhost:8080/upload \
  -F "file=@avatar.jpg"
```

### 2. 测试受保护接口
```bash
# 测试需要登录的接口（应该返回401）
curl http://localhost:8080/users/1
```

## 常见问题

### Q1: 为什么某些路径仍然被拦截？
A: 检查路径是否完全匹配白名单，注意大小写和路径前缀。

### Q2: 如何添加新的白名单路径？
A: 在`WHITE_LIST`数组中添加新路径，重启应用即可生效。

### Q3: 白名单路径是否支持正则表达式？
A: 当前版本使用简单的`contains`匹配，如需正则支持可以扩展`isWhiteListPath`方法。

### Q4: 如何调试白名单配置？
A: 查看日志输出，白名单路径会显示"白名单路径: xxx, 放行"的日志。

## 总结

通过添加白名单机制，我们解决了以下问题：
1. ✅ 用户注册时无需登录
2. ✅ 头像上传时无需登录
3. ✅ 静态资源正常访问
4. ✅ 保持了其他接口的安全性

这种配置既保证了系统的安全性，又提供了必要的用户体验。
