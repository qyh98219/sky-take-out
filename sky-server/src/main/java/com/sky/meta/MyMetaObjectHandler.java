package com.sky.meta;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.sky.utils.ThreadLocalUtil;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

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

        this.strictInsertFill(metaObject,"createTime", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "createUser", Long.class, (Long)ThreadLocalUtil.get("admin_user_id"));
        this.strictInsertFill(metaObject, "updateUser", Long.class, (Long)ThreadLocalUtil.get("admin_user_id"));
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject,"updateTime", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "updateUser", Long.class, (Long)ThreadLocalUtil.get("admin_user_id"));
    }
}
