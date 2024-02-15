package com.t.logic.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.t.logic.entity.DocUser;
import com.t.logic.entity.User;
import com.t.logic.entity.Vo.ShareUserVo;
import com.t.logic.service.DocUserService;
import com.t.logic.mapper.DocUserMapper;
import com.t.logic.service.UserService;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* @author sky
* @description 针对表【doc_user】的数据库操作Service实现
* @createDate 2024-02-15 21:46:13
*/
@Service
public class DocUserServiceImpl extends ServiceImpl<DocUserMapper, DocUser>
    implements DocUserService{
  @Autowired
  UserService userService;

  @Override
  public List<Long> selectShareDoc(Long userId) {
    return null;
  }

  @Override
  public List<ShareUserVo> selectDocUsers(Long docId) {
    List<ShareUserVo> shareUserVos = baseMapper.selectDocUsers(docId);
    if (shareUserVos==null|| shareUserVos.size()<1)
      return shareUserVos;
    Set<Long> userIds = shareUserVos.stream().map(ShareUserVo::getUserId)
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
    shareUserVos.stream().forEach(user ->{
      user.setUserName(userMap.get(user.getUserId()).getUserName());
      user.setUserPhone(userMap.get(user.getUserId()).getUserPhone());
    });
    return shareUserVos;
  }
}




