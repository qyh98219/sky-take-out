package com.sky.service;

import com.sky.entity.DishFlavor;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 菜品口味关系表 服务类
 * </p>
 *
 * @author baomidou
 * @since 2024-04-29
 */
public interface IDishFlavorService extends IService<DishFlavor> {

    int insertBatchSomeColumn(List<DishFlavor> entiyList);
}
