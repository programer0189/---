package com.webproject.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class sysLog {
    private Integer id;
    private String level;
    private String logger;
    private String message;
    private LocalDateTime createTime;
}
