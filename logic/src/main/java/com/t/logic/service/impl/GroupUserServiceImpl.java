package com.t.logic.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.t.logic.entity.GroupUser;
import com.t.logic.entity.User;
import com.t.logic.entity.Vo.GroupUserVo;
import com.t.logic.mapper.UserMapper;
import com.t.logic.service.GroupUserService;
import com.t.logic.mapper.GroupUserMapper;
import com.t.logic.service.UserService;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.ibatis.session.AutoMappingBehavior;
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
  public List<GroupUserVo> selectDocUsers(Long docId) {
    List<GroupUserVo> groupUserVos = baseMapper.selectDocUsers(docId);
    if (groupUserVos==null|| groupUserVos.size()<1)
      return groupUserVos;
    Set<Long> userIds = groupUserVos.stream().map(GroupUserVo::getUserId)
        .collect(Collectors.toSet());
//    lambda表达式
//    不能非空
    List<User> users = userService.list(
        Wrappers.lambdaQuery(User.class)
            .select(User::getUserId,User::getUserName,User::getUserPhone)
            .in(User::getUserId, userIds));
//    若参数单一，可以直接转换map 进行
    Map<Long, User> userMap = users.stream()
        .collect(Collectors.toMap(User::getUserId, user -> user));
//    Map<Long, String> userNames = users.stream()
//        .collect(Collectors.toMap(User::getUserId, User::getUserName));
//    Map<Long, String> userPhones = users.stream()
//        .collect(Collectors.toMap(User::getUserId, User::getUserPhone));
    groupUserVos.stream().forEach(user ->{
      user.setUserName(userMap.get(user.getUserId()).getUserName());
      user.setUserPhone(userMap.get(user.getUserId()).getUserPhone());
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




