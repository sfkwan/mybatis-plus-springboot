package com.example.restservice.user.entity;

import java.time.ZonedDateTime;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Data;

/**
 * User entity class representing a system user with profile and metadata
 * information.
 * Uses MyBatis Plus annotations for ORM mapping and soft delete functionality.
 * Includes validation annotations and Swagger documentation for API
 * documentation.
 * 
 * @author Application Development Team
 * @since 1.0
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@TableName("`user`")
@Schema(description = "User entity representing a system user with profile and metadata information")
public class UserEntity {
    @TableId(type = IdType.ASSIGN_ID)
    @Size(max = 20)
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "User ID", type = "string", example = "755637593648250880", accessMode = Schema.AccessMode.READ_ONLY)
    private String id;

    @Min(0)
    @Schema(description = "User age", example = "30")
    private Integer age;

    @TableField(fill = FieldFill.INSERT, value = "create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss z")
    @Schema(description = "User creation timestamp", example = "2024-01-11 10:30:00 UTC", accessMode = Schema.AccessMode.READ_ONLY)
    private ZonedDateTime createTime;

    @TableLogic
    @Schema(description = "Soft delete flag (0=not deleted, 1=deleted)", example = "0")
    private Integer deleted;

    @Schema(description = "Department ID", example = "DEPT001")
    @Size(max = 5)
    private String departmentId;

    @NotBlank
    @Email
    @Schema(description = "User email address", example = "john@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @Size(max = 10)
    // @NotBlank
    @Schema(description = "User name", example = "John Doe", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @TableField(fill = FieldFill.INSERT_UPDATE, value = "update_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss z")
    @Schema(description = "User last update timestamp", example = "2024-01-11 14:45:00 UTC", accessMode = Schema.AccessMode.READ_ONLY)
    private ZonedDateTime updateTime;

}