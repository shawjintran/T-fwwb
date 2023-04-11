package com.t.medicaldocument.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import io.swagger.annotations.ApiParam;
import lombok.Data;

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
     * 管理员职位

     */
    private Integer employeeStation;

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

    /**
     * 账号状态
     */
    private String employeeStatus;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
