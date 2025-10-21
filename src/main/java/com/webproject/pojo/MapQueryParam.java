package com.webproject.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class MapQueryParam
{private Integer page=1;
    private Integer pageSize=10;

    private Integer id;
    private String name;
    private BigDecimal longitude;
    private BigDecimal latitude;
    private String image;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime updateTime;

}
