package com.t.logic.service;

import com.t.logic.entity.GroupUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.t.logic.entity.Vo.GroupUserVo;
import java.util.List;

/**
* @author sky
* @description 针对表【group_user】的数据库操作Service
* @createDate 2024-01-28 22:00:24
*/
public interface GroupUserService extends IService<GroupUser> {

  List<Long> selectUserGroup(Long userId);

  List<GroupUserVo> selectDocUsers(Long docId);

  List<Long> selectShareDoc(Long userId);

  List<GroupUserVo> selectGroupUsers(Long groupId);

  List<Long> selectGroupDoc(Long groupId);
}
