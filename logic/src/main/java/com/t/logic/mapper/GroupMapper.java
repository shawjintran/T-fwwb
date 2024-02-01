package com.t.logic.mapper;

import com.t.logic.entity.Group;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author sky
* @description 针对表【group】的数据库操作Mapper
* @createDate 2024-01-28 22:00:13
* @Entity com.t.logic.entity.Group
*/
@Mapper
public interface GroupMapper extends BaseMapper<Group> {

}




