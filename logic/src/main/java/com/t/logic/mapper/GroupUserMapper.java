package com.t.logic.mapper;

import com.t.logic.entity.Group;
import com.t.logic.entity.GroupUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.t.logic.entity.Vo.GroupUserVo;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
* @author sky
* @description 针对表【group_user】的数据库操作Mapper
* @createDate 2024-01-28 22:00:24
* @Entity com.t.logic.entity.GroupUser
*/
@Mapper
public interface GroupUserMapper extends BaseMapper<GroupUser> {
  @Select("select doc_id from medical.group_user where user_id=#{userId}")
  List<Long> selectShareDoc(Long userId);
  @Select("select user_id,user_role from medical.group_user where doc_id=#{docId}")
  List<GroupUserVo> selectDocUsers(Long docId);
  @Select("select group_id from medical.group_user where user_id=#{userId}")
  List<Long> selectUserGroup(Long userId);
  @Select("select user_id,user_role from medical.group_user where group_id=#{groupId}")
  List<GroupUserVo> selectGroupUsers(Long groupId);
  @Select("select doc_id from medical.group_user where group_id=#{groupId} group by doc_id")
  List<Long> selectGroupDoc(Long groupId);
}




