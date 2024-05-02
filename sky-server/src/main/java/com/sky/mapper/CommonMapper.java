package com.sky.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * @ClassName CommonMapper
 * @Description TODO
 * @Author qyh
 * @Date 2024/5/2 11:18
 * @Version 1.0
 **/
public interface CommonMapper<T> extends BaseMapper<T> {
    int insertBatchSomeColumn(List<T> entityList);
}
