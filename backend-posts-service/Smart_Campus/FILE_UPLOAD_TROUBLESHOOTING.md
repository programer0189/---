# 文件上传问题排查指南

## 问题描述
文件上传时出现 `MultipartException: Failed to parse multipart servlet request` 错误，提示 `no multipart boundary was found`。

## 错误原因分析

### 1. 主要错误
```
org.springframework.web.multipart.MultipartException: Failed to parse multipart servlet request
Caused by: org.apache.tomcat.util.http.fileupload.FileUploadException: the request was rejected because no multipart boundary was found
```

### 2. 常见原因
- **Content-Type 不正确**：请求头没有设置正确的 `multipart/form-data`
- **前端请求格式错误**：没有正确构造文件上传请求
- **文件上传配置缺失**：Spring Boot 没有配置文件上传相关参数
- **请求体格式错误**：请求体不是正确的 multipart 格式

## 解决方案

### 1. 配置文件上传参数
已在 `application.yml` 中添加：
```yaml
spring:
  servlet:
    multipart:
      enabled: true                    # 启用文件上传
      max-file-size: 10MB             # 单个文件最大大小
      max-request-size: 100MB         # 单次请求最大大小
      file-size-threshold: 2KB        # 文件写入磁盘的阈值
      location: ${java.io.tmpdir}     # 临时文件存储位置
```

### 2. 改进的 UploadController
- 添加了 `@RequestParam("file")` 注解
- 增加了参数验证
- 添加了错误处理
- 改进了日志记录

### 3. 前端请求格式要求

#### 正确的请求格式
```javascript
// 使用 FormData
const formData = new FormData();
formData.append('file', fileInput.files[0]);

fetch('/upload', {
  method: 'POST',
  body: formData
  // 注意：不要手动设置 Content-Type，让浏览器自动设置
});
```

#### 错误的请求格式
```javascript
// ❌ 错误：手动设置 Content-Type
fetch('/upload', {
  method: 'POST',
  headers: {
    'Content-Type': 'multipart/form-data'  // 这会覆盖 boundary
  },
  body: formData
});

// ❌ 错误：使用 JSON 格式
fetch('/upload', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({file: fileData})
});
```

## 测试方法

### 1. 使用 curl 测试
```bash
# 正确的文件上传请求
curl -X POST http://localhost:8080/upload \
  -F "file=@test.jpg"

# 测试健康检查接口
curl http://localhost:8080/upload/health
```

### 2. 使用 Postman 测试
1. 选择 POST 方法
2. URL: `http://localhost:8080/upload`
3. Body 选择 `form-data`
4. Key 设置为 `file`，Type 选择 `File`
5. Value 选择要上传的文件
6. **不要手动设置 Content-Type 头**

### 3. 前端代码测试
```html
<!DOCTYPE html>
<html>
<head>
    <title>文件上传测试</title>
</head>
<body>
    <input type="file" id="fileInput" accept="image/*">
    <button onclick="uploadFile()">上传文件</button>
    
    <script>
        async function uploadFile() {
            const fileInput = document.getElementById('fileInput');
            const file = fileInput.files[0];
            
            if (!file) {
                alert('请选择文件');
                return;
            }
            
            const formData = new FormData();
            formData.append('file', file);
            
            try {
                const response = await fetch('/upload', {
                    method: 'POST',
                    body: formData
                });
                
                const result = await response.json();
                if (result.code === 1) {
                    alert('上传成功：' + result.data);
                } else {
                    alert('上传失败：' + result.msg);
                }
            } catch (error) {
                alert('上传失败：' + error.message);
            }
        }
    </script>
</body>
</html>
```

## 常见问题排查

### 问题1: 仍然出现 MultipartException
**检查点**：
1. 确认应用已重启，配置生效
2. 检查前端请求格式是否正确
3. 查看请求头中的 Content-Type

**解决方案**：
```javascript
// 确保使用 FormData
const formData = new FormData();
formData.append('file', file);

// 不要设置任何 headers
fetch('/upload', {
  method: 'POST',
  body: formData
});
```

### 问题2: 文件大小超限
**检查点**：
1. 文件大小是否超过 10MB
2. 检查 application.yml 中的配置

**解决方案**：
```yaml
spring:
  servlet:
    multipart:
      max-file-size: 50MB  # 增加文件大小限制
      max-request-size: 100MB
```

### 问题3: 文件类型不支持
**检查点**：
1. 文件扩展名是否合法
2. 文件内容是否损坏

**解决方案**：
```java
// 在 Controller 中添加文件类型检查
String contentType = file.getContentType();
if (contentType == null || !contentType.startsWith("image/")) {
    return Result.error("只支持图片文件上传");
}
```

## 调试技巧

### 1. 查看请求日志
```java
@PostMapping
public Result upload(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
    // 打印请求头信息
    log.info("Content-Type: {}", request.getContentType());
    log.info("Content-Length: {}", request.getContentLength());
    log.info("User-Agent: {}", request.getHeader("User-Agent"));
    
    // ... 其他代码
}
```

### 2. 检查文件信息
```java
if (file != null) {
    log.info("文件名: {}", file.getOriginalFilename());
    log.info("文件大小: {} bytes", file.getSize());
    log.info("文件类型: {}", file.getContentType());
    log.info("是否为空: {}", file.isEmpty());
}
```

### 3. 使用浏览器开发者工具
- 查看 Network 标签页
- 检查请求头和请求体
- 确认 Content-Type 包含 boundary

## 总结

文件上传问题通常由以下原因导致：
1. ✅ **配置问题**：已添加文件上传配置
2. ✅ **Controller 改进**：已优化参数验证和错误处理
3. ⚠️ **前端请求格式**：确保使用 FormData，不手动设置 Content-Type
4. ⚠️ **文件选择**：确保选择了有效的文件

按照以上步骤操作，文件上传功能应该可以正常工作。

