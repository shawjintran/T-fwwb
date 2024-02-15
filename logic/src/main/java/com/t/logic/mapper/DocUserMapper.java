package com.t.logic.mapper;

import com.t.logic.entity.DocUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.t.logic.entity.Vo.ShareUserVo;
import java.util.List;
import org.apache.ibatis.annotations.Select;

/**
* @author sky
* @description 针对表【doc_user】的数据库操作Mapper
* @createDate 2024-02-15 21:46:13
* @Entity com.t.logic.entity.DocUser
*/
public interface DocUserMapper extends BaseMapper<DocUser> {
  @Select("select user_id,user_role from medical.doc_user where doc_id=#{docId}")
  List<ShareUserVo> selectDocUsers(Long docId);
  @Select("select doc_id from medical.doc_user where user_id=#{userId}")
  List<Long> selectShareDoc(Long userId);
  @Select("select doc_id from medical.doc_user where user_id=#{userId} and user_role= 1")
  List<Long> selectWriteDoc(Long userId);
}




