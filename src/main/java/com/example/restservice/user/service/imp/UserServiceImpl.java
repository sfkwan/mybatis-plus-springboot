package com.example.restservice.user.service.imp;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.restservice.user.entity.UserEntity;
import com.example.restservice.user.mapper.UserMapper;
import com.example.restservice.user.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements UserService {

    private final UserMapper userMapper;

    @Override
    public List<UserEntity> testQueryWrapper(int age) {
        QueryWrapper<UserEntity> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.ge("age", age);
        // trainList为空不报错
        return userMapper.selectList(userQueryWrapper);
    }

    @Override
    public List<UserEntity> selectAll(Integer isDeleted, long size, long offset) {
        return userMapper.selectAll(isDeleted, size, offset);
    }

    @Override
    public long countAll(Integer isDeleted) {
        return userMapper.countAll(isDeleted);
    }

}
