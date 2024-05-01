package com.sky.vo;

import lombok.Data;

import java.util.List;

/**
 * @author qyh
 * @version 1.0
 * @className EmployeeListVO
 * @description TODO
 * @date 2024/4/30 14:37
 **/
@Data
public class PageResult<T> {
    private Long total;
    private List<T> records;
}
