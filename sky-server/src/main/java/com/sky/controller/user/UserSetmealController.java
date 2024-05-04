package com.sky.controller.user;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.sky.constant.RedisConstant;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.json.JacksonObjectMapper;
import com.sky.result.Result;
import com.sky.service.ISetmealDishService;
import com.sky.service.ISetmealService;
import com.sky.vo.DishItemVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @ClassName UserSetmealController
 * @Description TODO
 * @Author qyh
 * @Date 2024/5/3 20:16
 * @Version 1.0
 **/
@Slf4j
@RestController
@RequestMapping("/user/setmeal")
public class UserSetmealController {
    @Autowired
    private ISetmealService setmealService;
    @Autowired
    private ISetmealDishService setmealDishService;
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @GetMapping("/list")
    public Result<List<Setmeal>> list(String categoryId) throws JsonProcessingException {
        List<Setmeal> setmeals = new ArrayList<>();
        JacksonObjectMapper objectMapper = new JacksonObjectMapper();

        if (!redisTemplate.opsForHash().hasKey(RedisConstant.CAHE_SHOP, "setmeal_"+categoryId)) {
            log.info("套餐缓存未生效");
            LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Setmeal::getCategoryId, categoryId);
            setmeals = setmealService.list(queryWrapper);
            String cacheData = objectMapper.writeValueAsString(setmeals);
            redisTemplate.opsForHash().put(RedisConstant.CAHE_SHOP, "setmeal_"+categoryId, cacheData);
        }

        String cacheData = (String) redisTemplate.opsForHash().get(RedisConstant.CAHE_SHOP, "setmeal_" + categoryId);
        setmeals = objectMapper.readValue(cacheData, new TypeReference<>() {
        });
        return Result.success(setmeals);
    }

    @GetMapping("/dish/{id}")
    public Result<List<DishItemVO>> getDishItem(@PathVariable Integer id) throws JsonProcessingException {
        List<DishItemVO> dishItemVOS = new ArrayList<>();
        JacksonObjectMapper objectMapper = new JacksonObjectMapper();

        if(!redisTemplate.opsForHash().hasKey(RedisConstant.CAHE_SHOP, "dishItem_" +id)) {
            log.info("套餐菜品缓存未生效");
            LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SetmealDish::getSetmealId, id);
            List<SetmealDish> setmealDishes = setmealDishService.list(queryWrapper);
            dishItemVOS = new ArrayList<>(setmealDishes.size());
            for(SetmealDish setmealDish:setmealDishes){
                DishItemVO dishItemVO = new DishItemVO();
                BeanUtils.copyProperties(setmealDish, dishItemVO);
                dishItemVOS.add(dishItemVO);
            }
            String cacheData = objectMapper.writeValueAsString(dishItemVOS);
            redisTemplate.opsForHash().put(RedisConstant.CAHE_SHOP, "dishItem_"+id, cacheData);
        }
        String cacheData = (String) redisTemplate.opsForHash().get(RedisConstant.CAHE_SHOP, "dishItem_"+id);
        dishItemVOS = objectMapper.readValue(cacheData, new TypeReference<>() {
        });
        return Result.success(dishItemVOS);
    }
}
