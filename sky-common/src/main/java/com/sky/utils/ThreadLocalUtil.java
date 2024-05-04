package com.sky.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author qyh
 * @version 1.0
 * @className ThreadLocalUtil
 * @description TODO
 * @date 2024/4/29 17:22
 **/
public class ThreadLocalUtil {
    private static final ThreadLocal<Map<String,Object>> threadLocal = new ThreadLocal<>();
    private static Map<String,Object> paramMap = new HashMap<>(16);

    public static Object get(String key){
       return threadLocal.get().get(key);
    }

    public static void set(String key, Object value){
        paramMap.put(key, value);
        threadLocal.set(paramMap);
    }

    public static void remove(){
        threadLocal.remove();
    }
}
