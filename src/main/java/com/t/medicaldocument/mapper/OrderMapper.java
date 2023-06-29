package com.t.medicaldocument.mapper;

import com.t.medicaldocument.entity.Order;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author sky
* @description 针对表【order】的数据库操作Mapper
* @createDate 2023-03-27 15:14:25
* @Entity com.t.medicaldocument.entity.Order
*/
@Mapper
public interface OrderMapper extends BaseMapper<Order> {

}




