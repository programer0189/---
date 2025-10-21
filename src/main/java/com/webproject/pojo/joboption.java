package com.webproject.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class joboption
{
    private List jobList;
    private List dataList;
}
