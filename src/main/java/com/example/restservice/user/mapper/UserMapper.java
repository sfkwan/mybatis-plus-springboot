package com.example.restservice.user.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.restservice.user.entity.UserEntity;

/**
 * MyBatis mapper interface for User entity.
 * Provides custom SQL queries for user data access operations.
 * Extends BaseMapper to inherit standard CRUD operations from MyBatis Plus.
 * 
 * @author Application Development Team
 * @since 1.0
 */
public interface UserMapper extends BaseMapper<UserEntity> {
        /**
         * Selects all users with optional filtering by deleted status and pagination.
         * 
         * @param isDeleted filter parameter: 0 for active, 1 for deleted, null for all
         * @param size      the page size (maximum number of records to return)
         * @param offset    the offset for pagination
         * @return list of users matching the criteria
         */
        @Select("SELECT id, name, age, email, department_id, deleted, create_time, update_time FROM `user` "
                        + "WHERE (#{isDeleted} IS NULL OR deleted = #{isDeleted}) LIMIT #{size} OFFSET #{offset}")
        List<UserEntity> selectAll(@Param("isDeleted") Integer isDeleted, @Param("size") long size,
                        @Param("offset") long offset);

        /**
         * Counts the number of users with optional filtering by deleted status.
         * 
         * @param isDeleted filter parameter: 0 for active, 1 for deleted, null for all
         * @return the count of users matching the criteria
         */
        @Select("SELECT COUNT(*) FROM `user` WHERE (#{isDeleted} IS NULL OR deleted = #{isDeleted})")
        long countAll(@Param("isDeleted") Integer isDeleted);
}
