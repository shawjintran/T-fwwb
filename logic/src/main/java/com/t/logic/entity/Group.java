package com.t.logic.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName group
 */
@TableName(value ="group")
@Data
public class Group implements Serializable {
    /**
     * 
     */
    @TableId
    private Long groupId;

    /**
     * 
     */
    private Integer groupSize;

    /**
     * 
     */
    private Long groupCreatorId;

    /**
     * 
     */
    private Integer groupStatus;

    /**
     * 
     */
    private Date createTime;

    /**
     * 
     */
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}