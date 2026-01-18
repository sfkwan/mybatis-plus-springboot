package com.example.restservice.user.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.restservice.user.entity.UserEntity;
import com.baomidou.mybatisplus.core.metadata.IPage;

public interface UserService
        extends IService<UserEntity> {
    public List<UserEntity> testQueryWrapper(int age);

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
    List<UserEntity> selectAll(Integer isDeleted, long size, long offset);

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

    /**
     * Retrieves a user by their ID.
     *
     * @param id the user ID
     * @return the user entity if found, null otherwise
     */
    UserEntity getUserById(String id);

    /**
     * Creates a new user.
     *
     * @param userParam the user data to create
     * @return the created user entity
     */
    UserEntity createUser(UserEntity userParam);

    /**
     * Updates an existing user.
     *
     * @param id        the user ID to update
     * @param userParam the updated user data
     * @return the updated user entity
     */
    UserEntity updateUser(String id, UserEntity userParam);

    /**
     * Deletes a user by their ID.
     *
     * @param id the user ID to delete
     * @return true if deletion was successful, false otherwise
     */
    boolean deleteUser(String id);

    /**
     * Retrieves a paginated list of users with optional name filtering.
     *
     * @param pageNum  the page number (1-based)
     * @param pageSize the number of records per page
     * @param name     optional filter by user name
     * @return a paged result containing the users and pagination metadata
     */
    IPage<UserEntity> findPage(Integer pageNum, Integer pageSize, String name);

    /**
     * Retrieves all users with optional deleted status filtering and offset-based pagination.
     *
     * @param pageNum   the page number (1-based)
     * @param pageSize  the page size
     * @param isDeleted filter parameter: 0 for active users, 1 for deleted users, null for all users
     * @return a paged result containing the users and pagination metadata
     */
    IPage<UserEntity> getAllUsersPage(Integer pageNum, Integer pageSize, Integer isDeleted);

}
