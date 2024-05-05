package com.sky.utils;

import java.util.UUID;

/**
 * @ClassName OrderNumberUtil
 * @Description TODO
 * @Author qyh
 * @Date 2024/5/5 15:03
 * @Version 1.0
 **/
public class OrderNumberUtil {
    public static String generateOrderNumber(){
        return UUID.randomUUID().toString().replaceAll("-", "").toLowerCase();
    }
}
