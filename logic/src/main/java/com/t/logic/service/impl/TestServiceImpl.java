package com.t.logic.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.t.logic.entity.Test;
import com.t.logic.service.TestService;
import com.t.logic.mapper.TestMapper;
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




