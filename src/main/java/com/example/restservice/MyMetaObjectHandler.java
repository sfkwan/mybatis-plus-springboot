package com.example.restservice;

import java.time.ZonedDateTime;

import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;

/**
 * Custom MetaObject handler for automatic field filling during insert and
 * update operations.
 * Automatically sets creation and update timestamps when records are inserted
 * or modified.
 * 
 * @author Application Development Team
 * @since 1.0
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    /**
     * Fills metadata fields when inserting a new record.
     * Sets both createTime and updateTime to the current ZonedDateTime.
     * 
     * @param metaObject the MetaObject containing the entity data
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "createTime", ZonedDateTime.class, ZonedDateTime.now());
        this.strictInsertFill(metaObject, "updateTime", ZonedDateTime.class, ZonedDateTime.now());
    }

    /**
     * Fills metadata fields when updating an existing record.
     * Sets updateTime to the current ZonedDateTime.
     * 
     * @param metaObject the MetaObject containing the entity data
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updateTime", ZonedDateTime.now(), metaObject);
    }
}
