package com.t.logic.service;

import com.t.logic.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.t.logic.entity.Vo.UserVo;

/**
* @author sky
* @description 针对表【user】的数据库操作Service
* @createDate 2023-01-24 12:40:55
*/
public interface UserService extends IService<User> {
	boolean deleteUser(Long id);

	boolean generatePwd(Long userId, String oldPwd, String newPwd);

	boolean updateInfo(UserVo vo);

	boolean updatePoint(Integer mode, Integer point,Long userId);
}
