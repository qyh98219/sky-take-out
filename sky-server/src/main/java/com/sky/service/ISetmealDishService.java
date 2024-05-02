package com.sky.service;

import com.sky.entity.SetmealDish;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 套餐菜品关系 服务类
 * </p>
 *
 * @author baomidou
 * @since 2024-04-29
 */
public interface ISetmealDishService extends IService<SetmealDish> {
    int insertBatchSomeColumn(List<SetmealDish> entityList);
}
