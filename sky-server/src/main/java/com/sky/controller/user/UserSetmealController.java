package com.sky.controller.user;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.result.Result;
import com.sky.service.ISetmealDishService;
import com.sky.service.ISetmealService;
import com.sky.vo.DishItemVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @ClassName UserSetmealController
 * @Description TODO
 * @Author qyh
 * @Date 2024/5/3 20:16
 * @Version 1.0
 **/
@RestController
@RequestMapping("/user/setmeal")
public class UserSetmealController {
    @Autowired
    private ISetmealService setmealService;
    @Autowired
    private ISetmealDishService setmealDishService;

    @GetMapping("/list")
    public Result<List<Setmeal>> list(String categoryId){
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Setmeal::getCategoryId, categoryId);
        List<Setmeal> list = setmealService.list(queryWrapper);
        return Result.success(list);
    }

    @GetMapping("/dish/{id}")
    public Result<List<DishItemVO>> getDishItem(@PathVariable Integer id) {
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId, id);
        List<SetmealDish> setmealDishes = setmealDishService.list(queryWrapper);
        List<DishItemVO> dishItemVOS = new ArrayList<>(setmealDishes.size());
        setmealDishes.forEach(setmealDish -> {
            DishItemVO dishItemVO = new DishItemVO();
            BeanUtils.copyProperties(setmealDish, dishItemVO);
            dishItemVOS.add(dishItemVO);
        });
        return Result.success(dishItemVOS);
    }
}
