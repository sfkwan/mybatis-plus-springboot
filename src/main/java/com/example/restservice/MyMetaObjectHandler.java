package com.example.restservice;

import java.time.ZonedDateTime;

import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;

@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "createTime", ZonedDateTime.class, ZonedDateTime.now());
        this.strictInsertFill(metaObject, "updateTime", ZonedDateTime.class, ZonedDateTime.now());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updateTime", ZonedDateTime.now(), metaObject);
    }
}
