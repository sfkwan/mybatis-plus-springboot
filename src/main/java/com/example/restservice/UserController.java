package com.example.restservice;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Size;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.restservice.user.entity.UserEntity;
import com.example.restservice.user.service.UserService;
import com.example.restservice.exception.ApiException;
import com.example.restservice.exception.ErrorResponse;
import com.example.restservice.genericresponse.ApiResult;
import com.example.restservice.genericresponse.PagedApiResult;
import com.example.restservice.genericresponse.DeleteResult;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Slf4j
@Validated
@Tag(name = "User Management", description = "APIs for managing users")
public class UserController {
        private final UserService userService;

        @GetMapping("")
        @Operation(summary = "Get all users", description = "Retrieves a list of all users, optionally filtered by deleted status")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Successfully retrieved users"),
                        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
        })
        public PagedApiResult<List<UserEntity>> getAllUsers(
                        @Parameter(description = "Page number (default: 1, max: 10)", schema = @Schema(type = "integer", maximum = "10", example = "1")) @RequestParam(defaultValue = "1") @Max(10) Integer pageNum,
                        @Parameter(description = "Page size (default: 10, max: 50)", schema = @Schema(type = "integer", maximum = "50", example = "10")) @RequestParam(defaultValue = "10") @Max(50) Integer pageSize,
                        @Parameter(description = "Filter by deleted status (0 for not deleted, 1 for deleted)") @RequestParam(required = false) Integer isDeleted) {

                long total = userService.countAll(isDeleted);
                long offset = (pageNum - 1L) * pageSize;
                List<UserEntity> records = userService.selectAll(isDeleted, pageSize, offset);
                long totalPages = (total + pageSize - 1) / pageSize;
                log.info("Paginated users: total {} records, current page {} records, total pages {}",
                                total, records.size(), totalPages);
                return new PagedApiResult<>(total, totalPages, records.size(), records);
        }

        /**
         * Retrieves a user by their ID.
         * 
         * @param id the user ID
         * @return the user if found
         * @apiNote Returns ErrorResponse with status 404 if user is not found,
         *          or status 500 for internal server errors
         */
        @GetMapping("/{id}")
        @Operation(summary = "Get user by ID", description = "Retrieves a specific user by their ID")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Successfully retrieved user"),
                        @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
        })
        public ApiResult<UserEntity> getUser(
                        @Parameter(description = "User ID") @PathVariable("id") @Size(max = 20, message = "User ID must not exceed 20 characters") String id) {
                LambdaQueryWrapper<UserEntity> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                lambdaQueryWrapper.eq(UserEntity::getId, id);
                UserEntity user = userService.getBaseMapper().selectOne(lambdaQueryWrapper);
                log.info("User with id {}: {}", id, user);
                return new ApiResult<>(user);
        }

        @DeleteMapping("/{id}")
        @Operation(summary = "Delete user by ID", description = "Deletes a user by their ID")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Successfully deleted user"),
                        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
        })
        public ApiResult<DeleteResult> deleteUser(
                        @Parameter(description = "User ID") @PathVariable("id") @Size(max = 20, message = "User ID must not exceed 20 characters") String id) {
                boolean result = userService.removeById(id);

                if (result) {
                        log.info("Delete user with id {}", id);
                        return new ApiResult<>(new DeleteResult(true, "Deleted user with id " + id, id));
                }

                log.warn("Failed to delete user with id {}", id);
                return new ApiResult<>(new DeleteResult(false, "Failed to delete user with id " + id, id));
        }

        /**
         * Updates an existing user by their ID.
         * 
         * @param userParam the user data to update
         * @param id        the user ID
         * @return the updated user
         * @throws ApiException with status 404 and code "USER_NOT_FOUND" if the user
         *                      doesn't exist
         * @apiNote Returns ErrorResponse with status 404 when update fails (user not
         *          found),
         *          status 400 for validation errors with field-specific error details,
         *          or status 500 for internal server errors
         */
        @PutMapping("/{id}")
        @Operation(summary = "Update user by ID", description = "Updates an existing user by their ID")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Successfully updated user"),
                        @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                        @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
        })
        public ApiResult<UserEntity> putUser(@Valid @RequestBody UserEntity userParam,
                        @Parameter(description = "User ID") @PathVariable("id") @Size(max = 20, message = "User ID must not exceed 20 characters") String id) {

                LambdaUpdateWrapper<UserEntity> updateWrapper = new LambdaUpdateWrapper<>();
                updateWrapper.eq(UserEntity::getId, id)
                                .set(userParam.getName() != null, UserEntity::getName, userParam.getName())
                                .set(userParam.getAge() != null, UserEntity::getAge, userParam.getAge())
                                .set(userParam.getEmail() != null, UserEntity::getEmail, userParam.getEmail())
                                .set(userParam.getDepartmentId() != null, UserEntity::getDepartmentId,
                                                userParam.getDepartmentId());

                boolean result = userService.update(updateWrapper);
                if (!result) {
                        throw new ApiException(org.springframework.http.HttpStatus.NOT_FOUND,
                                        "USER_NOT_FOUND",
                                        String.valueOf(HttpStatus.NOT_FOUND.value()));
                }
                log.info("Update user: {}", userService.getById(id));

                return new ApiResult<>(userService.getById(id));

        }

        /**
         * Creates a new user.
         * 
         * @param userParam the user data to create
         * @return the created user with generated ID and timestamps
         * @apiNote Returns ErrorResponse with status 400 for validation errors with
         *          field-specific error details,
         *          or status 500 for internal server errors
         */
        @PostMapping("")
        @ResponseStatus(HttpStatus.CREATED)
        @Operation(summary = "Create new user", description = "Creates a new user")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Successfully created user"),
                        @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
        })
        public ApiResult<UserEntity> saveUser(@Valid @RequestBody UserEntity userParam) {
                UserEntity user = new UserEntity();
                user.setAge(userParam.getAge());
                user.setName(userParam.getName());
                user.setEmail(userParam.getEmail());
                user.setDepartmentId(userParam.getDepartmentId());

                userService.save(user);
                log.info("Save user: {}", user);

                return new ApiResult<>(user);

        }

        // 分页查询
        @GetMapping("/page")
        @Operation(summary = "Get users with pagination", description = "Retrieves users with pagination and optional name filtering")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Successfully retrieved paginated users"),
                        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
        })
        public PagedApiResult<List<UserEntity>> findPage(
                        @Parameter(description = "Page number (default: 1, max=10)", schema = @Schema(type = "integer", maximum = "10", example = "1")) @RequestParam(defaultValue = "1") @Max(10) Integer pageNum,
                        @Parameter(description = "Page size (default: 10, max=50)", schema = @Schema(type = "integer", maximum = "50", example = "10")) @RequestParam(defaultValue = "10") @Max(50) Integer pageSize,
                        @Parameter(description = "Filter by name") @RequestParam(required = false) String name) {
                IPage<UserEntity> page = new Page<>(pageNum, pageSize);
                LambdaQueryWrapper<UserEntity> lambda = new LambdaQueryWrapper<>();
                if (name != null && !"".equals(name)) {
                        lambda.like(UserEntity::getName, name);
                }
                IPage<UserEntity> userPage = userService.page(page, lambda);
                log.info("Paginated users: total {} records, current page {} records, total pages {}",
                                userPage.getTotal(), userPage.getRecords().size(), userPage.getPages());
                return new PagedApiResult<>(userPage.getTotal(), userPage.getPages(), userPage.getRecords().size(),
                                userPage.getRecords());
        }
}
