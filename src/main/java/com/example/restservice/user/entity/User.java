package com.example.restservice.user.entity;

import java.time.ZonedDateTime;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
@TableName("`user`")
@Schema(description = "User entity representing a system user with profile and metadata information")
public class User {
    @Min(0)
    @Schema(description = "User age", example = "30")
    private Integer age;

    @TableField(fill = FieldFill.INSERT, value = "create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss z")
    @Schema(description = "User creation timestamp", example = "2024-01-11 10:30:00 UTC")
    private ZonedDateTime createTime;

    @TableLogic
    @Schema(description = "Soft delete flag (0=not deleted, 1=deleted)", example = "0")
    private Integer deleted;

    @Schema(description = "Department ID", example = "DEPT001")
    private String departmentId;

    @Email
    @Schema(description = "User email address", example = "john@example.com")
    private String email;

    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "User ID", example = "1234567890")
    private String id;

    @Size(max = 10)
    @Schema(description = "User name", example = "John Doe")
    private String name;

    @TableField(fill = FieldFill.INSERT_UPDATE, value = "update_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss z")
    @Schema(description = "User last update timestamp", example = "2024-01-11 14:45:00 UTC")
    private ZonedDateTime updateTime;

}