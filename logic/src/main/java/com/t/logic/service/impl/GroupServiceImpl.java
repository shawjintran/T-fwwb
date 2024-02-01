package com.t.logic.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.t.logic.entity.Group;
import com.t.logic.service.GroupService;
import com.t.logic.mapper.GroupMapper;
import org.springframework.stereotype.Service;

/**
* @author sky
* @description 针对表【group】的数据库操作Service实现
* @createDate 2024-01-28 22:00:13
*/
@Service
public class GroupServiceImpl extends ServiceImpl<GroupMapper, Group>
    implements GroupService{

}




