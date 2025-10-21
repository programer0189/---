package com.webproject.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Map
{

    private Integer id;
    private String name;
    private BigDecimal longitude;
    private BigDecimal latitude;
    private String image;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    // 点击记录
    private List<ClickCount> clickList;
}
