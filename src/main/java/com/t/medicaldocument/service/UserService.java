package com.t.medicaldocument.service;

import com.t.medicaldocument.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author sky
* @description 针对表【user】的数据库操作Service
* @createDate 2023-01-24 12:40:55
*/
public interface UserService extends IService<User> {

	boolean deleteUser(Long id);

}
