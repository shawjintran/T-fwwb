package com.t.medicaldocument.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.t.medicaldocument.entity.Order;
import com.t.medicaldocument.service.OrderService;
import com.t.medicaldocument.mapper.OrderMapper;
import org.springframework.stereotype.Service;

/**
* @author sky
* @description 针对表【order】的数据库操作Service实现
* @createDate 2023-03-27 15:14:25
*/
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order>
    implements OrderService{

}




