package com.t.medicaldocument.mapper;

import com.t.medicaldocument.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

/**
* @author sky
* @description 针对表【user】的数据库操作Mapper
* @createDate 2023-01-24 12:40:55
* @Entity com/t/medicaldocument.entity.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {
	@Update("update user set user_pwd=#{newPwd} where user_pwd=#{oldPwd} and user_id=#{userId}")
	boolean generatePwd(Long userId, String oldPwd, String newPwd);
	@Update("update user set user_points = user_points+${size} ,update_time =now() where user_id=#{userId}")
	Integer addPoint(Integer size, Long userId);
	@Update("update user set user_points = user_points-${size} ,update_time =now() where user_id=#{userId}")
	Integer subPoint( Integer size, Long userId);
}




