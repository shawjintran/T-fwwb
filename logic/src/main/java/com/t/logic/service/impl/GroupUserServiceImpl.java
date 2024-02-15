package com.t.logic.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.t.logic.entity.GroupUser;
import com.t.logic.entity.Vo.ShareUserVo;
import com.t.logic.mapper.UserMapper;
import com.t.logic.service.GroupUserService;
import com.t.logic.mapper.GroupUserMapper;
import com.t.logic.service.UserService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* @author sky
* @description 针对表【group_user】的数据库操作Service实现
* @createDate 2024-01-28 22:00:24
*/
@Service
public class GroupUserServiceImpl extends ServiceImpl<GroupUserMapper, GroupUser>
    implements GroupUserService{

  @Autowired
  UserService userService;
  @Override
  public List<Long> selectUserGroup(Long userId) {
    List<Long> longs = baseMapper.selectUserGroup(userId);
    return longs;
  }

  @Override
  public List<Long> selectShareDoc(Long userId) {
    List<Long> docs = baseMapper.selectShareDoc(userId);
    return docs;
  }

  @Override
  public List<ShareUserVo> selectGroupUsers(Long groupId) {
    return baseMapper.selectGroupUsers(groupId);
  }


  @Override
  public List<Long> selectGroupDoc(Long groupId) {
    return baseMapper.selectGroupDoc(groupId);
  }
}




