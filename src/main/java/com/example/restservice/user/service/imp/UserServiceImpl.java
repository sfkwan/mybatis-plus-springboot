package com.example.restservice.user.service.imp;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.restservice.exception.ApiException;
import com.example.restservice.user.entity.UserEntity;
import com.example.restservice.user.mapper.UserMapper;
import com.example.restservice.user.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementation of UserService interface.
 * Provides business logic for user management operations.
 * Extends MyBatis Plus ServiceImpl for inherited CRUD functionality.
 * 
 * @author Application Development Team
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements UserService {

    private final UserMapper userMapper;

    /**
     * Tests QueryWrapper functionality by retrieving users with age greater than or
     * equal to specified value.
     * 
     * @param age the minimum age to filter by
     * @return list of users matching the age criteria
     */
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

    @Override
    @Cacheable(value = "users", key = "#id")
    public UserEntity getUserById(String id) {
        try {
            // Simulate long database lookup
            log.info("Simulating long DB lookup for user id: {}", id);
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        LambdaQueryWrapper<UserEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(UserEntity::getId, id);

        UserEntity user = userMapper.selectOne(lambdaQueryWrapper);
        log.info("Fetched user from DB: {}", user);
        return user;
    }

    @Override
    public UserEntity createUser(UserEntity userParam) {
        UserEntity user = new UserEntity();
        user.setAge(userParam.getAge());
        user.setName(userParam.getName());
        user.setEmail(userParam.getEmail());
        user.setDepartmentId(userParam.getDepartmentId());

        this.save(user);
        log.info("Save user: {}", user);
        return user;
    }

    @Override
    @CacheEvict(value = "users", key = "#id")
    public UserEntity updateUser(String id, UserEntity userParam) {
        LambdaUpdateWrapper<UserEntity> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(UserEntity::getId, id)
                .set(userParam.getName() != null, UserEntity::getName, userParam.getName())
                .set(userParam.getAge() != null, UserEntity::getAge, userParam.getAge())
                .set(userParam.getEmail() != null, UserEntity::getEmail, userParam.getEmail())
                .set(userParam.getDepartmentId() != null, UserEntity::getDepartmentId,
                        userParam.getDepartmentId());

        boolean result = this.update(updateWrapper);
        if (!result) {
            throw new ApiException(HttpStatus.NOT_FOUND,
                    "USER_NOT_FOUND",
                    String.valueOf(HttpStatus.NOT_FOUND.value()));
        }
        UserEntity updatedUser = this.getById(id);
        log.info("Update user: {}", updatedUser);
        return updatedUser;
    }

    @Override
    @CacheEvict(value = "users", key = "#id")
    public boolean deleteUser(String id) {
        boolean result = this.removeById(id);
        if (result) {
            log.info("Delete user with id {}", id);
        } else {
            log.warn("Failed to delete user with id {}", id);
        }
        return result;
    }

    @Override
    public IPage<UserEntity> findPage(Integer pageNum, Integer pageSize, String name) {
        IPage<UserEntity> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<UserEntity> lambda = new LambdaQueryWrapper<>();
        if (name != null && !"".equals(name)) {
            lambda.like(UserEntity::getName, name);
        }
        IPage<UserEntity> records = this.page(page, lambda);
        log.atInfo().setMessage(
                "findPage")
                .addKeyValue("totalRecords", total)
                .addKeyValue("pageSize", records.size())
                .addKeyValue("totalPages", totalPages)
                .log();
        return records;
    }

    @Override
    public IPage<UserEntity> getAllUsersPage(Integer pageNum, Integer pageSize, Integer isDeleted) {
        long total = userMapper.countAll(isDeleted);
        long offset = (pageNum - 1L) * pageSize;
        List<UserEntity> records = userMapper.selectAll(isDeleted, pageSize, offset);
        long totalPages = (total + pageSize - 1) / pageSize;

        log.atInfo().setMessage(
                "getAllUsersPage")
                .addKeyValue("totalRecords", total)
                .addKeyValue("pageSize", records.size())
                .addKeyValue("totalPages", totalPages)
                .log();

        IPage<UserEntity> page = new Page<>(pageNum, pageSize);
        page.setTotal(total);
        page.setRecords(records);
        return page;
    }

}
