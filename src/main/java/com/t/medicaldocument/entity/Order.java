package com.t.medicaldocument.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @TableName order
 */
@TableName(value ="user_order")
@Data
@Accessors(chain = true)
public class Order implements Serializable {
    /**
     * 订单编号ID
     */
    @TableId(type = IdType.AUTO)
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
    @TableField(fill = FieldFill.INSERT)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 订单更改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
