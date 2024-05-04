package com.sky.controller.user;

import com.sky.dto.ShoppingCartDTO;
import com.sky.result.Result;
import com.sky.service.IShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    /*@PostMapping("/add")
    public Result addShoppingCart(@RequestBody ShoppingCartDTO shoppingCartDTO){

    }*/
}
