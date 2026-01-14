package com.example.restservice.user.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.restservice.user.entity.UserEntity;

public interface UserMapper extends BaseMapper<UserEntity> {
        @Select("SELECT id, name, age, email, department_id, deleted, create_time, update_time FROM `user` "
                        + "WHERE (#{isDeleted} IS NULL OR deleted = #{isDeleted}) LIMIT #{size} OFFSET #{offset}")
        List<UserEntity> selectAll(@Param("isDeleted") Integer isDeleted, @Param("size") long size,
                        @Param("offset") long offset);

        @Select("SELECT COUNT(*) FROM `user` WHERE (#{isDeleted} IS NULL OR deleted = #{isDeleted})")
        long countAll(@Param("isDeleted") Integer isDeleted);
}
