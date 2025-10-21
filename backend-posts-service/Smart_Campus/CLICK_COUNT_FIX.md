# 点击统计接口修复说明

## 问题描述
前端点击地图记录点击数据时出现JSON反序列化错误：
```
Cannot deserialize value of type `java.lang.Integer` from Object value (token `JsonToken.START_OBJECT`)
```

## 错误原因
前端发送的JSON数据中，`locationId`字段是一个对象，但后端期望的是一个整数：
```json
// 前端发送的错误格式
{
  "locationId": {
    "id": 1,
    "name": "图书馆"
  }
}

// 后端期望的正确格式
{
  "locationId": 1
}
```

## 解决方案
创建了新的请求DTO类 `ClickCountRequest`，支持两种数据格式：

### 1. 直接整数格式（推荐）
```json
{
  "locationId": 1
}
```

### 2. 对象格式（兼容现有前端）
```json
{
  "locationId": {
    "id": 1,
    "name": "图书馆"
  }
}
```

## 修复后的接口

### 接口地址
```
POST /click
```

### 请求头
```
Content-Type: application/json
Authorization: Bearer {JWT_TOKEN}
```

### 请求体示例

#### 方式1：直接传递整数（推荐）
```json
{
  "locationId": 1
}
```

#### 方式2：传递对象（兼容）
```json
{
  "locationId": {
    "id": 1,
    "name": "图书馆"
  }
}
```

### 响应格式
```json
{
  "code": 1,
  "msg": "success",
  "data": null
}
```

### 错误响应
```json
{
  "code": 0,
  "msg": "locationId不能为空或格式不正确",
  "data": null
}
```

## 技术实现

### ClickCountRequest类
```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClickCountRequest {
    
    private Object locationId;
    
    public Integer getLocationIdAsInteger() {
        if (locationId == null) {
            return null;
        }
        
        if (locationId instanceof Integer) {
            return (Integer) locationId;
        }
        
        if (locationId instanceof java.util.Map) {
            @SuppressWarnings("unchecked")
            java.util.Map<String, Object> map = (java.util.Map<String, Object>) locationId;
            Object idValue = map.get("id");
            if (idValue instanceof Integer) {
                return (Integer) idValue;
            }
            if (idValue instanceof String) {
                try {
                    return Integer.parseInt((String) idValue);
                } catch (NumberFormatException e) {
                    return null;
                }
            }
        }
        
        return null;
    }
}
```

### 控制器修改
```java
@PostMapping
public Result addClick(@RequestBody ClickCountRequest request) {
    log.info("点击位置请求: {}", request);
    
    Integer locationId = request.getLocationIdAsInteger();
    if (locationId == null) {
        return Result.error("locationId不能为空或格式不正确");
    }

    clickcountService.addClick(locationId);
    return Result.success();
}
```

## 建议
1. **前端优化**：建议前端直接发送 `{"locationId": 1}` 格式，避免发送不必要的对象数据
2. **数据验证**：后端增加了更严格的数据验证，确保数据格式正确
3. **错误处理**：提供了更清晰的错误提示信息

## 测试
修复后的接口现在可以正常处理两种数据格式，不会再出现JSON反序列化错误。
