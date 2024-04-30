package com.sky.meta;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author qyh
 * @version 1.0
 * @className MyMetaObjectHandler
 * @description TODO
 * @date 2024/4/30 9:03
 **/
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject,"create_time", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "update_time", LocalDateTime.class, LocalDateTime.now());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject,"update_time", LocalDateTime.class, LocalDateTime.now());
    }
}
