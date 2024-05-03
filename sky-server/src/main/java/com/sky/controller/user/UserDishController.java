package com.sky.controller.user;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sky.entity.Dish;
import com.sky.result.Result;
import com.sky.service.IDishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName UserDishController
 * @Description TODO
 * @Author qyh
 * @Date 2024/5/3 20:09
 * @Version 1.0
 **/
@RestController
@RequestMapping("/user/dish")
public class UserDishController {
    @Autowired
    private IDishService dishService;

    @GetMapping("/list")
    public Result<List<Dish>> list(Integer categoryId){
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dish::getCategoryId, categoryId);
        List<Dish> list = dishService.list(queryWrapper);
        return Result.success(list);
    }
}
