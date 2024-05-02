package com.sky.controller.admin;

import com.sky.constant.RedisConstant;
import com.sky.result.Result;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName ShopConftroller
 * @Description TODO
 * @Author qyh
 * @Date 2024/5/2 16:49
 * @Version 1.0
 **/
@RestController
@RequestMapping("/admin/shop")
@Tag(name = "经营状态")
public class ShopController {
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @GetMapping("/status")
    public Result<Integer> getShopStatus(){
        Integer status = (Integer) redisTemplate.opsForValue().get(RedisConstant.SHOP_STATUS);
        return Result.success(status);
    }

    @PutMapping("/{status}")
    public Result setShopStatus(@PathVariable Integer status){
        redisTemplate.opsForValue().set(RedisConstant.SHOP_STATUS, status);
        return Result.success();
    }
}
