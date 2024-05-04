package com.sky.controller.user;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.sky.constant.RedisConstant;
import com.sky.constant.StatusConstant;
import com.sky.entity.Dish;
import com.sky.json.JacksonObjectMapper;
import com.sky.result.Result;
import com.sky.service.IDishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @ClassName UserDishController
 * @Description TODO
 * @Author qyh
 * @Date 2024/5/3 20:09
 * @Version 1.0
 **/
@Slf4j
@RestController
@RequestMapping("/user/dish")
public class UserDishController {
    @Autowired
    private IDishService dishService;
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @GetMapping("/list")
    public Result<List<Dish>> list(Integer categoryId) throws JsonProcessingException {
        List<Dish> cacheDishs = new ArrayList<>();
        JacksonObjectMapper objectMapper = new JacksonObjectMapper();

        if (!redisTemplate.opsForHash().hasKey(RedisConstant.CAHE_SHOP, "dish_"+categoryId)) {
            log.info("菜品缓存未生效");
            LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Dish::getCategoryId, categoryId);
            queryWrapper.eq(Dish::getStatus, StatusConstant.ENABLE);
            cacheDishs = dishService.list(queryWrapper);
            if (!cacheDishs.isEmpty()){
                String cacheData = objectMapper.writeValueAsString(cacheDishs);
                redisTemplate.opsForHash().put(RedisConstant.CAHE_SHOP, "dish_" + categoryId, cacheData);
            }
        }

        String cacheData = (String) redisTemplate.opsForHash().get(RedisConstant.CAHE_SHOP, "dish_"+categoryId);
        if (StringUtils.hasText(cacheData)){
            cacheDishs = objectMapper.readValue(cacheData, new TypeReference<>() {
            });
        }
        return Result.success(cacheDishs);
    }
}
