package com.t.medicaldocument.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.t.medicaldocument.entity.Employee;
import com.t.medicaldocument.service.EmployeeService;
import com.t.medicaldocument.mapper.EmployeeMapper;
import org.springframework.stereotype.Service;

/**
* @author sky
* @description 针对表【employee】的数据库操作Service实现
* @createDate 2023-03-27 19:17:21
*/
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee>
    implements EmployeeService{

}




