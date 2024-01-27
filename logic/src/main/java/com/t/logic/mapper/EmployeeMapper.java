package com.t.logic.mapper;

import com.t.logic.entity.Employee;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author sky
* @description 针对表【employee】的数据库操作Mapper
* @createDate 2023-03-27 19:17:21
* @Entity com.t.medicaldocument.entity.Employee
*/
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {

}




