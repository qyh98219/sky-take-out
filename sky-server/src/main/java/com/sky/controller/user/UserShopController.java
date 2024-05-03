package com.sky.controller.user;

import com.sky.constant.RedisConstant;
import com.sky.result.Result;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName ShopController
 * @Description TODO
 * @Author qyh
 * @Date 2024/5/3 18:00
 * @Version 1.0
 **/
@RestController
@RequestMapping("/user/shop")
@Tag(name = "店铺接口")
public class UserShopController {
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @GetMapping("/status")
    public Result getStatus(){
        Integer status = (Integer) redisTemplate.opsForValue().get(RedisConstant.SHOP_STATUS);
        return Result.success(status);
    }
}
