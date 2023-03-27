package com.t.medicaldocument.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.t.medicaldocument.entity.Business;
import com.t.medicaldocument.service.BusinessService;
import com.t.medicaldocument.mapper.BusinessMapper;
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




