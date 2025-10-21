package com.webproject.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 封装分页结果
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class mapPageResult<T> {
    private Long total; // 总记录数
    private List<T> rows;// 当前页的数据
}
