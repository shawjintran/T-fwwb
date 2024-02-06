package com.t.logic.service.impl;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.t.logic.entity.User;
import com.t.logic.entity.Vo.UserVo;
import com.t.logic.mapper.UserMapper;
import com.t.logic.service.DocumentService;
import com.t.logic.service.UserService;
import java.util.Collection;
import java.util.List;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

/**
* @author sky
* @description 针对表【user】的数据库操作Service实现
* @createDate 2023-01-24 12:40:55
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {


	@Autowired
	DocumentService documentService;

	@Override
	@Transactional
	public boolean deleteUser(Long userId) {
		int i = baseMapper.deleteById(userId);
		boolean b = documentService.removeByDocIdAndUserId(null, userId);
		if (i==1&&b)
			return true;
		TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		return false;
	}

	@Override
	public boolean generatePwd(Long userId, String oldPwd, String newPwd) {
		if (userId==null||oldPwd==null||newPwd==null)
			return false;
		boolean done=baseMapper.generatePwd(userId,oldPwd,newPwd);
		return done;
	}

	@Override
	public boolean updateInfo(UserVo vo) {
		User user = new User();
		vo.setUserPoints(null);
		BeanUtils.copyProperties(vo,user);
		int update = baseMapper.updateById(user);
		if (update==1)
			return true;
		return false;
	}

	@Override
	public boolean updatePoint(Integer mode, Integer point, Long userId) {
		Integer integer=0;
		if (mode==1)
		{
			 integer = baseMapper.subPoint(point, userId);

		}
		else if (mode==2)
		{
			integer = baseMapper.addPoint(point, userId);
		}
		if (integer==1)
			return true;
		return false;
	}
}




