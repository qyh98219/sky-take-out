package com.sky.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.*;
import com.sky.exception.SetmealEnableFailedException;
import com.sky.result.Result;
import com.sky.service.ICategoryService;
import com.sky.service.IDishService;
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

import java.util.*;

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
    @Autowired
    private IDishService dishService;

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

    @PostMapping("/status/{status}")
    @ApiOperation("套餐起售/禁售")
    public Result updateStatus(@PathVariable Integer status, Integer id){
       if (status.equals(StatusConstant.ENABLE)){
           LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
           setmealDishLambdaQueryWrapper.eq(SetmealDish::getSetmealId, id);
           List<SetmealDish> setmealDishes = setmealDishService.list(setmealDishLambdaQueryWrapper);
           setmealDishes.stream().forEach(setmealDish -> {
               Long dishId = setmealDish.getDishId();
               Dish dish = dishService.getById(dishId);
               if (Objects.nonNull(dish) && dish.getStatus().equals(StatusConstant.DISABLE)){
                   throw new SetmealEnableFailedException(MessageConstant.SETMEAL_ENABLE_FAILED);
               }
           });
       }

        Setmeal setmeal = setmealService.getById(id);
        setmeal.setStatus(status);
        if(!setmealService.updateById(setmeal)){
            return Result.error("操作失败");
        }
        return Result.success("操作成功");
    }

    @GetMapping("/{id}")
    @ApiOperation("套餐信息回显")
    public Result<SetmealVO> reviewSetmealInfo(@PathVariable Integer id){
        Setmeal setmeal = setmealService.getById(id);
        if(Objects.isNull(setmeal)){
            throw new RuntimeException("非法参数");
        }

        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId, setmeal.getId());
        List<SetmealDish> setmealDishes = setmealDishService.list(queryWrapper);

        SetmealVO setmealVO = new SetmealVO();
        BeanUtils.copyProperties(setmeal, setmealVO);
        setmealVO.setSetmealDishes(setmealDishes);
        return Result.success(setmealVO);
    }

    @PutMapping()
    @ApiOperation("套餐修改")
    public Result<String> updateSetmeal(@RequestBody SetmealDTO setmealDTO){
        Setmeal oldSetmeal = setmealService.getById(setmealDTO.getId());
        if (!oldSetmeal.getName().equals(setmealDTO.getName())){
            LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Setmeal::getName, setmealDTO.getName());
            Setmeal one = setmealService.getOne(queryWrapper);
            if (Objects.nonNull(one)){
                return Result.error("名称重复");
            }
        }

        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId, setmealDTO.getId());
        List<SetmealDish> oldSetmealDishes = setmealDishService.list(queryWrapper);
        if (!oldSetmealDishes.isEmpty()){
            setmealDishService.removeBatchByIds(oldSetmealDishes);
        }

        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes().stream().peek(setmealDish -> setmealDish.setSetmealId(setmealDTO.getId())).toList();
        setmealDishService.insertBatchSomeColumn(setmealDishes);

        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmealService.updateById(setmeal);
        return Result.success();
    }

    @DeleteMapping()
    @ApiOperation("套餐删除")
    public Result delSetmeal(String ids){
        String[] idArr = ids.split(",");
        List<String> list = Arrays.stream(idArr).filter(id -> {
            Setmeal setmeal = setmealService.getById(id);
            if (Objects.nonNull(setmeal) && setmeal.getStatus().equals(StatusConstant.DISABLE)) {
                return true;
            }
            return false;
        }).toList();

        //删除对应的菜品关系
        list.forEach(id -> {
            LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SetmealDish::getSetmealId, id);
            List<SetmealDish> setmealDishes = setmealDishService.list(queryWrapper);
            if (!setmealDishes.isEmpty()){
                setmealDishService.removeBatchByIds(setmealDishes);
            }
        });

        if(!setmealService.removeBatchByIds(list)){
            return Result.error("操作失败");
        }else {
            return Result.success("操作成功");
        }
    }


}
