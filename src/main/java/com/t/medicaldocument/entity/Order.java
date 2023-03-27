package com.t.medicaldocument.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 *
 * @TableName order
 */
@TableName(value ="order")
@Data
@Accessors(chain = true)
public class Order implements Serializable {
    /**
     * 订单编号ID
     */
    @TableId
    private Long orderId;

    /**
     * 创建订单用户ID
     */
    private Long userId;

    /**
     * 订单业务ID
     */
    private Long bizId;

    /**
     * 订单价格
     */
    private BigDecimal orderPrice;

    /**
     * 订单附带积分
     */
    private Long orderPoint;

    /**
     * 订单状态
     */
    private Integer orderStatus;

    /**
     * 订单创建时间
     */
    private Date createTime;

    /**
     * 订单更改时间
     */
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
