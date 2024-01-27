package com.t.logic.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.t.logic.entity.Business;
import com.t.logic.service.BusinessService;
import com.t.logic.mapper.BusinessMapper;
import org.springframework.stereotype.Service;

/**
* @author sky
* @description 针对表【business】的数据库操作Service实现
* @createDate 2023-03-27 15:11:19
*/
@Service
public class BusinessServiceImpl extends ServiceImpl<BusinessMapper, Business>
    implements BusinessService{

}




