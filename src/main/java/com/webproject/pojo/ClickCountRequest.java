package com.webproject.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 点击统计请求DTO
 * 用于接收前端发送的点击数据
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClickCountRequest {
    
    /**
     * 位置ID，可以是整数或包含id字段的对象
     */
    private Object locationId;
    
    /**
     * 获取位置ID的整数值
     * 支持两种格式：
     * 1. 直接是整数：{"locationId": 1}
     * 2. 是对象：{"locationId": {"id": 1, "name": "图书馆"}}
     */
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
