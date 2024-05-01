package com.sky.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sky.entity.Category;
import com.sky.mapper.CategoryMapper;
import com.sky.service.ICategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 菜品及套餐分类 服务实现类
 * </p>
 *
 * @author baomidou
 * @since 2024-04-29
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements ICategoryService {

}
