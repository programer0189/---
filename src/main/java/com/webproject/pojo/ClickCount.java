package com.webproject.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClickCount {
    private Integer id;
    private Integer locationId;
    private LocalDateTime clickTime;


}
