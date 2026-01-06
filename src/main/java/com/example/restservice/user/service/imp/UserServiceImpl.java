package com.example.restservice.user.service.imp;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.restservice.user.entity.User;
import com.example.restservice.user.mapper.UserMapper;
import com.example.restservice.user.service.UserService;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private UserMapper userMapper;

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public List<User> testQueryWrapper(int age) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.ge("age", age);
        // trainList为空不报错
        return userMapper.selectList(userQueryWrapper);
    }

    @Override
    public List<User> listIncludingDeleted(Integer isDeleted) {
        return userMapper.selectAllIncludeDeleted(isDeleted);
    }

}
