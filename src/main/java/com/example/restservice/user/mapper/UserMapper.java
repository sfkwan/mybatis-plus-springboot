package com.example.restservice.user.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.restservice.user.entity.User;

public interface UserMapper extends BaseMapper<User> {
    @Select("SELECT id, name, age, email, department_id, deleted, create_time, update_time FROM `user` "
            + "WHERE (#{isDeleted} IS NULL OR deleted = #{isDeleted})")
    List<User> selectAllIncludeDeleted(@Param("isDeleted") Integer isDeleted);
}
