package com.sky.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.result.Result;
import com.sky.service.ISetmealDishService;
import com.sky.service.ISetmealService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @ClassName SetmealController
 * @Description TODO
 * @Author qyh
 * @Date 2024/5/2 19:40
 * @Version 1.0
 **/
@RestController
@RequestMapping("/admin/setmeal")
@Tag(name = "套餐管理")
public class SetmealController {
    @Autowired
    private ISetmealService setmealService;
    @Autowired
    private ISetmealDishService setmealDishService;

    @PostMapping("")
    @ApiOperation("添加套餐")
    public Result saveSetmeal(@RequestBody SetmealDTO setmealDTO){
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Setmeal::getName, setmealDTO.getName());
        Setmeal one = setmealService.getOne(queryWrapper);
        if(Objects.nonNull(one)){
            return Result.error("名称重复");
        }

        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmeal.setStatus(StatusConstant.DISABLE);
        setmealService.save(setmeal);

        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes().stream().peek(setmealDish -> setmealDish.setSetmealId(setmeal.getId())).toList();
        setmealDishService.insertBatchSomeColumn(setmealDishes);

        return Result.success();
    }
}
