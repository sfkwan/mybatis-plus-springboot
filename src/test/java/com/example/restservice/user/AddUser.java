package com.example.restservice.user;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.restservice.RestserviceApplication;
import com.example.restservice.user.entity.User;
import com.example.restservice.user.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootTest(classes = RestserviceApplication.class)
public class AddUser {
    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserMapper userMapper;

    @BeforeEach
    public void before() {
        log.info("init some data");
    }

    @AfterEach
    public void after() {
        log.info("clean some data");
    }

    @Test
    public void saveUser() {
        User user = new User();
        user.setAge(10);
        user.setName("testuser");
        user.setEmail("testuser@hktd.com");

        userMapper.insert(user);
        log.info("Save user: " + user.toString());
        assertNotNull(user.getId());

    }

}
