package com.t.medicaldocument.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.t.medicaldocument.entity.User;
import com.t.medicaldocument.mapper.UserMapper;
import com.t.medicaldocument.service.UserService;
import org.springframework.stereotype.Service;

/**
* @author sky
* @description 针对表【user】的数据库操作Service实现
* @createDate 2023-01-24 12:40:55
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {

	@Override
	public boolean deleteUser(Long id) {
		// TODO: 2023/3/15 删除用户
		return false;
	}
}




