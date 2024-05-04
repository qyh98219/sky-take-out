package com.sky.controller.admin;

import com.baomidou.mybatisplus.core.batch.MybatisBatch;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.Query;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.MybatisBatchUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sky.constant.RedisConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.*;
import com.sky.properties.JwtProperties;
import com.sky.result.Result;
import com.sky.service.*;
import com.sky.vo.DishVO;
import com.sky.vo.PageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.poi.hssf.record.cf.IconMultiStateThreshold;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName DishController
 * @Description TODO
 * @Author qyh
 * @Date 2024/5/2 9:46
 * @Version 1.0
 **/
@RestController
@RequestMapping("/admin/dish")
@Tag(name = "菜单管理")
public class DishController {
    @Autowired
    private IDishService dishService;
    @Autowired
    private IDishFlavorService dishFlavorService;
    @Autowired
    private ICategoryService categoryService;
    @Autowired
    private ISetmealService setmealService;
    @Autowired
    private ISetmealDishService setmealDishService;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @PostMapping("")
    @ApiOperation("新增菜单")
    public Result addDish(@RequestBody @Validated DishDTO dishDTO){
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dish::getName, dishDTO.getName());
        Dish one = dishService.getOne(queryWrapper);
        if (Objects.nonNull(one)){
            return Result.error("名字重复");
        }

        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dish.setStatus(StatusConstant.DISABLE);
        dishService.save(dish);
        if (!dishDTO.getFlavors().isEmpty()){
            List<DishFlavor> flavors = dishDTO.getFlavors().stream().peek(dishFlavor -> dishFlavor.setDishId(dish.getId())).toList();
            dishFlavorService.insertBatchSomeColumn(flavors);
        }
        return Result.success();
    }

    @GetMapping("/page")
    @ApiOperation("菜品分页查询")
    public Result<PageResult<DishVO>> list(DishPageQueryDTO queryDTO){
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        if (Objects.nonNull(queryDTO.getName())){
            queryWrapper.and(qw -> qw.like(Dish::getName, queryDTO.getName()));
        }
        if(Objects.nonNull(queryDTO.getCategoryId())){
            queryWrapper.and(qw -> qw.eq(Dish::getCategoryId, queryDTO.getCategoryId()));
        }
        if (Objects.nonNull(queryDTO.getStatus())){
            queryWrapper.and(qw -> qw.eq(Dish::getStatus, queryDTO.getStatus()));
        }

        queryWrapper.orderByDesc(Dish::getCreateTime);
        IPage<Dish> page = new Page<>(queryDTO.getPage(), queryDTO.getPageSize());
        List<Dish> dishs = dishService.list(page, queryWrapper);
        List<DishVO> records = new ArrayList<>(dishs.size());
        dishs.forEach(dish -> {
            Category category = categoryService.getById(dish.getCategoryId());
            DishVO dishVO = new DishVO();
            BeanUtils.copyProperties(dish, dishVO);
            dishVO.setCategoryName(category.getName());
            records.add(dishVO);
        });
        PageResult<DishVO> pageResult = new PageResult<>();
        pageResult.setRecords(records);
        pageResult.setTotal(page.getTotal());
        return Result.success(pageResult);
    }

    @PostMapping("/status/{status}")
    @ApiOperation("菜品起售/禁售")
    public Result updateDishStatus(@PathVariable("status") Integer status,Integer id){
        //菜品禁售
        if(Objects.equals(status, StatusConstant.DISABLE)) {
            //对包含菜品的相关套餐也禁售
            LambdaQueryWrapper<SetmealDish> sdQueryWrapper = new LambdaQueryWrapper<>();
            sdQueryWrapper.eq(SetmealDish::getDishId, id);
            List<SetmealDish> setmealDishes = setmealDishService.list(sdQueryWrapper);
            if(!setmealDishes.isEmpty()){
                setmealDishes.forEach(setmealDish -> {
                    Setmeal setmeal = setmealService.getById(setmealDish.getSetmealId());
                    setmeal.setStatus(StatusConstant.DISABLE);
                    setmealService.updateById(setmeal);
                });
            }
        }

        LambdaUpdateWrapper<Dish> dishLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        dishLambdaUpdateWrapper.set(Dish::getStatus, status);
        dishLambdaUpdateWrapper.eq(Dish::getId, id);
        if(!dishService.update(dishLambdaUpdateWrapper)) {
            return Result.error("操作失败");
        }
        //删除缓存
        redisTemplate.opsForHash().delete(RedisConstant.CAHE_SHOP);
        return Result.success("操作成功");
    }

    @GetMapping("/{id}")
    @ApiOperation("菜品信息回显")
    public Result<DishVO> reviewDish(@PathVariable Integer id){
        Dish dish = dishService.getById(id);

        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, id);
        List<DishFlavor> dishFlavors = dishFlavorService.list(queryWrapper);

        Category category = categoryService.getById(dish.getCategoryId());

        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish, dishVO);
        dishVO.setCategoryName(category.getName());
        dishVO.setFlavors(dishFlavors);

        return Result.success(dishVO);
    }

    @PutMapping()
    @ApiOperation("菜品修改")
    public Result<String> updateDish(@RequestBody @Validated DishDTO dishDTO){
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dishDTO.getId());
        dishFlavorService.remove(queryWrapper);

        if(!dishDTO.getFlavors().isEmpty()){
            List<DishFlavor> flavors = dishDTO.getFlavors().stream().peek(dishFlavor -> dishFlavor.setDishId(dishDTO.getId())).toList();
            dishFlavorService.insertBatchSomeColumn(flavors);
        }

        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);

        if(!dishService.updateById(dish)){
            return Result.error("操作失败");
        }
        //删除缓存
        redisTemplate.opsForHash().delete(RedisConstant.CAHE_SHOP);
        return Result.success("操作成功");
    }

    @DeleteMapping()
    @ApiOperation("菜品删除")
    public Result<String> delDish(String ids, HttpServletRequest request){
        String[] idArr = ids.split(",");
        List<String> idList = Arrays.stream(idArr).filter(dishId -> {
            Dish dish = dishService.getById(dishId);
            if(Objects.nonNull(dish) && dish.getStatus().equals(StatusConstant.DISABLE)) {
                 return true;
            }
            return false;
        }).toList();

        if (idList.isEmpty()){
            return Result.error("无法删除在售菜品");
        }

        StringBuffer sb = new StringBuffer();
        //删除对应的套餐
        idList.forEach(id -> {
            LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SetmealDish::getDishId, id);
            List<SetmealDish> setmealDishes = setmealDishService.list(queryWrapper);
            setmealDishes.forEach(setmealDish -> {
                sb.append(setmealDish.getSetmealId()).append(",");
            });
        });
        String setmealIds = sb.toString();
        String replace = setmealIds.replace(setmealIds.charAt(setmealIds.lastIndexOf(",")), ' ');
        setmealIds = replace.trim();
        //调用套餐删除
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(jwtProperties.getAdminTokenName(), request.getHeader(jwtProperties.getAdminTokenName()));
        HttpEntity<String> httpEntity = new HttpEntity<>(httpHeaders);
        Map<String,Object> paramMap = new HashMap<>(1);
        paramMap.put("ids", setmealIds);
        restTemplate.exchange("http://localhost:8080/admin/setmeal?ids={ids}", HttpMethod.DELETE,httpEntity, String.class, paramMap);

        //删除对应的口味关系
        idList.forEach(id -> {
            LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(DishFlavor::getDishId, id);
            List<DishFlavor> dishFlavors = dishFlavorService.list(queryWrapper);
            if (!dishFlavors.isEmpty()){
                dishFlavorService.removeBatchByIds(dishFlavors);
            }
        });

        if(!dishService.removeBatchByIds(idList)){
            return Result.error("操作失败");
        }

        //删除缓存
        redisTemplate.opsForHash().delete(RedisConstant.CAHE_SHOP);
        return Result.success("操作成功");
    }

    @GetMapping("/list")
    public Result<List<Dish>> listByCategoryId(Integer categoryId){
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dish::getCategoryId, categoryId);
        List<Dish> list = dishService.list(queryWrapper);
        return Result.success(list);
    }
}
