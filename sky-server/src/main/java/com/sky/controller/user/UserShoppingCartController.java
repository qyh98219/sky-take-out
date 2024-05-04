package com.sky.controller.user;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.Query;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.IDishService;
import com.sky.service.ISetmealService;
import com.sky.service.IShoppingCartService;
import com.sky.utils.ThreadLocalUtil;
import org.apache.commons.lang3.ThreadUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

/**
 * @ClassName UserShoppingCartController
 * @Description TODO
 * @Author qyh
 * @Date 2024/5/4 15:12
 * @Version 1.0
 **/
@RestController
@RequestMapping("/user/shoppingCart")
public class UserShoppingCartController {
    @Autowired
    private IShoppingCartService shoppingCartService;
    @Autowired
    private IDishService dishService;
    @Autowired
    private ISetmealService setmealService;

    @PostMapping("/add")
    public Result addShoppingCart(@RequestBody ShoppingCartDTO shoppingCartDTO){
        //获取当前用户id
        Long userId = (Long) ThreadLocalUtil.get("user_user_id");
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        if (Objects.nonNull(shoppingCartDTO.getDishId())){
            queryWrapper.eq(ShoppingCart::getDishId, shoppingCartDTO.getDishId());
        }
        if (Objects.nonNull(shoppingCartDTO.getSetmealId())){
            queryWrapper.eq(ShoppingCart::getSetmealId, shoppingCartDTO.getSetmealId());
        }
        if (Objects.nonNull(shoppingCartDTO.getDishFlavor())){
            queryWrapper.and(qw -> qw.eq(ShoppingCart::getDishFlavor, shoppingCartDTO.getDishFlavor()));
        }
        queryWrapper.eq(ShoppingCart::getUserId, userId);
        ShoppingCart shoppingCart = shoppingCartService.getOne(queryWrapper);
        //如果之前已经存入购物车，则数量加一
        if (Objects.nonNull(shoppingCart)){
            shoppingCart.setNumber(shoppingCart.getNumber()+1);
            shoppingCartService.updateById(shoppingCart);
            return Result.success();
        }
        //新添加购物车
        shoppingCart = new  ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        shoppingCart.setNumber(1);
        shoppingCart.setUserId(userId);
        if (Objects.nonNull(shoppingCart.getDishId())){
            Dish dish = dishService.getById(shoppingCart.getDishId());
            shoppingCart.setAmount(dish.getPrice().multiply(new BigDecimal(shoppingCart.getNumber())));
            shoppingCart.setImage(dish.getImage());
            shoppingCart.setName(dish.getName());
        }else if (Objects.nonNull(shoppingCart.getSetmealId())){
            Setmeal setmeal = setmealService.getById(shoppingCart.getSetmealId());
            shoppingCart.setAmount(setmeal.getPrice().multiply(new BigDecimal(shoppingCart.getNumber())));
            shoppingCart.setImage(setmeal.getImage());
            shoppingCart.setName(setmeal.getName());
        }
        if (!shoppingCartService.save(shoppingCart)){
            return Result.error("操作失败");
        }
        return Result.success();
    }

    @GetMapping("/list")
    public Result<List<ShoppingCart>> list(){
        Long userId = (Long) ThreadLocalUtil.get("user_user_id");
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, userId);
        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);
        return Result.success(list);
    }

    @DeleteMapping("/clean")
    public Result cleanShoppingCart(){
        Long userId = (Long) ThreadLocalUtil.get("user_user_id");
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, userId);
        if(!shoppingCartService.remove(queryWrapper)){
            return Result.error("操作失败");
        }
        return Result.success();
    }

    @PostMapping("/sub")
    public Result subShoppingCart(@RequestBody ShoppingCartDTO shoppingCartDTO){
        Long userId = (Long) ThreadLocalUtil.get("user_user_id");
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        if (Objects.nonNull(shoppingCartDTO.getDishId())){
            queryWrapper.eq(ShoppingCart::getDishId, shoppingCartDTO.getDishId());
        }
        if (Objects.nonNull(shoppingCartDTO.getSetmealId())){
            queryWrapper.eq(ShoppingCart::getSetmealId, shoppingCartDTO.getSetmealId());
        }
        if (Objects.nonNull(shoppingCartDTO.getDishFlavor())){
            queryWrapper.and(qw -> qw.eq(ShoppingCart::getDishFlavor, shoppingCartDTO.getDishFlavor()));
        }
        queryWrapper.eq(ShoppingCart::getUserId,userId);
        ShoppingCart shoppingCart = shoppingCartService.getOne(queryWrapper);
        if (Objects.nonNull(shoppingCart) && shoppingCart.getNumber() > 1){
            shoppingCart.setNumber(shoppingCart.getNumber() - 1);

            if (Objects.nonNull(shoppingCart.getDishId())){
                Dish dish = dishService.getById(shoppingCart.getDishId());
                shoppingCart.setAmount(dish.getPrice().multiply(new BigDecimal(shoppingCart.getNumber())));
            }else if (Objects.nonNull(shoppingCart.getSetmealId())){
                Setmeal setmeal = setmealService.getById(shoppingCart.getSetmealId());
                shoppingCart.setAmount(setmeal.getPrice().multiply(new BigDecimal(shoppingCart.getNumber())));
            }
            shoppingCartService.updateById(shoppingCart);
            return Result.success();
        }else if(Objects.nonNull(shoppingCart)) {
            shoppingCartService.removeById(shoppingCart);
            return Result.success();
        }else {
            throw new RuntimeException("系统错误");
        }
    }
}
