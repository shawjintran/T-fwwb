package com.t.medicaldocument.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.t.medicaldocument.entity.Test;
import com.t.medicaldocument.service.TestService;
import com.t.medicaldocument.mapper.TestMapper;
import org.springframework.stereotype.Service;

/**
* @author sky
* @description 针对表【test】的数据库操作Service实现
* @createDate 2023-08-09 10:42:36
*/
@Service
public class TestServiceImpl extends ServiceImpl<TestMapper, Test>
    implements TestService{

}




