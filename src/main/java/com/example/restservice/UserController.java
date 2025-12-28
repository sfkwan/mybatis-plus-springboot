package com.example.restservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.restservice.user.entity.User;
import com.example.restservice.user.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {
    private Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private UserService userService;

    public static final class DeleteResult {
        private final boolean success;
        private final String message;
        private final String id;

        public DeleteResult(boolean success, String message, String id) {
            this.success = success;
            this.message = message;
            this.id = id;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }

        public String getId() {
            return id;
        }
    }

    @GetMapping("")
    public User[] getAllUsers(@RequestParam(required = false) Integer isDeleted) {
        User[] users = userService.listIncludingDeleted(isDeleted).toArray(new User[0]);
        return users;
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable("id") String id) {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getId, id);
        User user = userService.getBaseMapper().selectOne(lambdaQueryWrapper);
        log.info("User with id " + id + ": " + (user != null ? user.toString() : "null"));
        return user;
    }

    @DeleteMapping("/{id}")
    public DeleteResult deleteUser(@PathVariable("id") String id) {
        boolean result = userService.removeById(id);

        if (result) {
            log.info("Delete user with id " + id);
            return new DeleteResult(true, "Deleted user with id " + id, id);
        }

        log.warn("Failed to delete user with id " + id);
        return new DeleteResult(false, "Failed to delete user with id " + id, id);
    }

    @PutMapping("/{id}")
    public User putUser(@RequestBody User userParam, @PathVariable("id") String id) {
        User user = userService.getById(id);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(User::getId, id)
                .set(userParam.getName() != null, User::getName, userParam.getName())
                .set(userParam.getAge() != null, User::getAge, userParam.getAge())
                .set(userParam.getEmail() != null, User::getEmail, userParam.getEmail())
                .set(userParam.getDepartmentId() != null, User::getDepartmentId, userParam.getDepartmentId());

        userService.update(updateWrapper);
        log.info("Update user: " + userService.getById(id));

        return userService.getById(id);

    }

    @PostMapping("")
    public User saveUser(@RequestBody User userParam) {
        User user = new User();
        user.setAge(userParam.getAge());
        user.setName(userParam.getName());
        user.setEmail(userParam.getEmail());
        user.setDepartmentId(userParam.getDepartmentId());

        userService.save(user);
        log.info("Save user: " + user);

        return user;

    }

    // 分页查询
    @GetMapping("/page")
    public IPage<User> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String name) {
        IPage<User> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<User> lambda = new LambdaQueryWrapper<>();
        if (name != null && !"".equals(name)) {
            lambda.like(User::getName, name);
        }
        return userService.page(page, lambda);
    }
}
