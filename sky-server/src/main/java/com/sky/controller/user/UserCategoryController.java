package com.sky.controller.user;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sky.entity.Category;
import com.sky.result.Result;
import com.sky.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * @ClassName UserCategoryController
 * @Description TODO
 * @Author qyh
 * @Date 2024/5/3 19:49
 * @Version 1.0
 **/
@RestController
@RequestMapping("/user/category")
public class UserCategoryController {
    @Autowired
    private ICategoryService categoryService;

    @GetMapping("/list")
    public Result<List<Category>> list(@RequestParam(required = false) String type){
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        if(Objects.nonNull(type)){
            queryWrapper.eq(Category::getType, type);
        }

        List<Category> list = categoryService.list(queryWrapper);
        return Result.success(list);
    }
}
