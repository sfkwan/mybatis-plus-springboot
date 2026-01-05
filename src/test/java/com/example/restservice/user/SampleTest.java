package com.example.restservice.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import org.springframework.util.Assert;

import com.example.restservice.RestserviceApplication;
import com.example.restservice.user.entity.User;
import com.example.restservice.user.mapper.UserMapper;

@SpringBootTest(classes = RestserviceApplication.class)
public class SampleTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testSelect() {

        List<User> userList = userMapper.selectList(null);

        Assert.isTrue(2 == userList.size(), " total users: " + userList.size());
        userList.forEach(user -> {
            System.out.println(user);
        });
    }

}
