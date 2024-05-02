package com.sky.service.impl;

import com.sky.entity.SetmealDish;
import com.sky.mapper.SetmealDishMapper;
import com.sky.service.ISetmealDishService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 套餐菜品关系 服务实现类
 * </p>
 *
 * @author baomidou
 * @since 2024-04-29
 */
@Service
public class SetmealDishServiceImpl extends ServiceImpl<SetmealDishMapper, SetmealDish> implements ISetmealDishService {

    @Override
    public int insertBatchSomeColumn(List<SetmealDish> entityList) {
        return this.baseMapper.insertBatchSomeColumn(entityList);
    }
}
