package com.t.medicaldocument.mapper;

import com.t.medicaldocument.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author sky
* @description 针对表【user】的数据库操作Mapper
* @createDate 2023-01-24 12:40:55
* @Entity com/t/medicaldocument.entity.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {

}




