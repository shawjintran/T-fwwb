package com.t.medicaldocument.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.AssertFalse;

/**
 *
 * @TableName user
 */
@TableName(value ="user")
@Data
@Accessors(chain = true)
public class User implements Serializable {
    /**
     * 用户id
     */
    @TableId
    private Long userId;

    /**
     * 用户电话
     */
    private String userPhone;

    /**
     * 用户密码
     */
    private String userPwd;

    /**
     * 用户头像链接
     */
    private String userUrl;

    /**
     * 用户积分
     */
    private Integer userPoints;

    /**
     *
     */
    private Date createTime;

    /**
     *
     */
    private Date updateTime;

    /**
     *
     */
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
