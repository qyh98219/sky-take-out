package com.sky.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Category;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.result.Result;
import com.sky.service.ICategoryService;
import com.sky.service.ISetmealDishService;
import com.sky.service.ISetmealService;
import com.sky.vo.PageResult;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
    @Autowired
    private ICategoryService categoryService;

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

    @GetMapping("/page")
    @ApiOperation("套餐分页查询")
    public Result<PageResult<SetmealVO>> page(SetmealPageQueryDTO  pageQueryDTO){
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        if(Objects.nonNull(pageQueryDTO.getName())){
            queryWrapper.and(qw -> qw.like(Setmeal::getName, pageQueryDTO.getName()));
        }
        if(Objects.nonNull(pageQueryDTO.getStatus())){
            queryWrapper.and(qw -> qw.eq(Setmeal::getStatus, pageQueryDTO.getStatus()));
        }
        if(Objects.nonNull(pageQueryDTO.getCategoryId())){
            queryWrapper.and(qw -> qw.eq(Setmeal::getCategoryId, pageQueryDTO.getCategoryId()));
        }
        queryWrapper.orderByDesc(Setmeal::getCreateTime);

        IPage<Setmeal> page = new Page<>(pageQueryDTO.getPage(), pageQueryDTO.getPageSize());
        List<Setmeal> list = setmealService.list(page, queryWrapper);
        List<SetmealVO> records = new ArrayList<>(list.size());
        list.forEach(item -> {
            Category category = categoryService.getById(item.getCategoryId());
            SetmealVO setmealVO = new SetmealVO();
            BeanUtils.copyProperties(item, setmealVO);
            setmealVO.setCategoryName(category.getName());
            records.add(setmealVO);
        });
        PageResult<SetmealVO> pageResult = new PageResult<>();
        pageResult.setTotal(page.getTotal());
        pageResult.setRecords(records);
        return Result.success(pageResult);
    }
}
