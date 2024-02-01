package com.t.logic.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.t.logic.entity.GroupUser;
import com.t.logic.entity.Vo.GroupUserVo;
import com.t.logic.mapper.UserMapper;
import com.t.logic.service.GroupUserService;
import com.t.logic.mapper.GroupUserMapper;
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
  UserMapper userMapper;
  @Override
  public List<Long> selectUserGroup(Long userId) {
    List<Long> longs = baseMapper.selectUserGroup(userId);
    return longs;
  }

  @Override
  public List<GroupUserVo> selectDocUsers(Long docId) {
    List<GroupUserVo> groupUserVos = baseMapper.selectDocUsers(docId);
    groupUserVos.stream().forEach(user->{
//  多表，联表查询
      Long userId = user.getUserId();
    });
    return groupUserVos;
  }

  @Override
  public List<Long> selectShareDoc(Long userId) {
    List<Long> docs = baseMapper.selectShareDoc(userId);
    return docs;
  }

  @Override
  public List<GroupUserVo> selectGroupUsers(Long groupId) {
    return baseMapper.selectGroupUsers(groupId);
  }

  @Override
  public List<Long> selectGroupDoc(Long groupId) {
    return baseMapper.selectGroupDoc(groupId);
  }
}




