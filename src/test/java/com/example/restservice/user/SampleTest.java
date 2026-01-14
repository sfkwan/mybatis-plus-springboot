package com.example.restservice.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import org.springframework.util.Assert;

import com.example.restservice.RestserviceApplication;
import com.example.restservice.user.entity.UserEntity;
import com.example.restservice.user.mapper.UserMapper;

@SpringBootTest(classes = RestserviceApplication.class)
class SampleTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    void testSelect() {

        List<UserEntity> userList = userMapper.selectList(null);

        Assert.isTrue(userList.size() > 0, " total users: " + userList.size());
        userList.forEach(System.out::println);
    }

}
