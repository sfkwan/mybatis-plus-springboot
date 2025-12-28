package com.example.restservice.user.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.restservice.user.entity.User;

public interface UserService
        extends IService<User> {
    public List<User> testQueryWrapper(int age);

    /**
     * Returns users filtered by logical delete flag.
     * When isDeleted is null, returns all users (deleted and non-deleted).
     * When isDeleted is 0 or 1, returns only matching records.
     */
    List<User> listIncludingDeleted(Integer isDeleted);

}
