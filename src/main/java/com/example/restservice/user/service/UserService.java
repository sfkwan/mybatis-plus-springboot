package com.example.restservice.user.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.restservice.user.entity.User;

public interface UserService
        extends IService<User> {
    public List<User> testQueryWrapper(int age);

    /**
     * Retrieves a paginated list of users, optionally filtering by deleted status.
     * This method bypasses MyBatis Plus automatic logic delete filtering to allow
     * querying of deleted records when needed.
     *
     * @param isDeleted filter parameter: 0 for active users, 1 for deleted users,
     *                  null for all users
     * @param size      the maximum number of users to return (page size)
     * @param offset    the starting index for pagination (offset)
     * @return a list of User objects matching the criteria
     */
    List<User> selectAll(Integer isDeleted, long size, long offset);

    /**
     * Counts users optionally filtering by deleted status.
     * This method bypasses MyBatis Plus automatic logic delete filtering to allow
     * counting deleted records when needed.
     *
     * @param isDeleted filter parameter: 0 for active users, 1 for deleted users,
     *                  null for all users
     * @return the count of users matching the criteria
     */
    long countAll(Integer isDeleted);

}
