package com.t.logic.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import io.swagger.annotations.ApiParam;
import java.time.LocalDateTime;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @TableName employee
 */
@TableName(value ="employee")
@Data
public class Employee implements Serializable {
    /**
     * 管理员ID
     */
    @TableId(type = IdType.AUTO)
    private Long employeeId;

    /**
     * 管理员手机号
     */
    @ApiParam(required = true)
    private String employeePhone;

    /**
     * 管理员密码
     */
    @ApiParam(required = true)
    private String employeePwd;

    @TableField(fill = FieldFill.INSERT)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     *
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    /**
     *
     */
    @TableLogic//用于逻辑删除
    @TableField(fill = FieldFill.INSERT)//添加这个注解是为了在后面设置初始值
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
