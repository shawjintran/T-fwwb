package com.t.medicaldocument.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.t.medicaldocument.entity.Document;
import com.t.medicaldocument.entity.User;
import com.t.medicaldocument.entity.Vo.DocumentVo;
import com.t.medicaldocument.entity.Vo.UserVo;
import com.t.medicaldocument.service.DocumentService;
import com.t.medicaldocument.service.UserService;
import com.t.medicaldocument.utils.R;
import io.swagger.annotations.Api;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/user/")
@Api(tags = "用户的登录登出")
public class UserController {

	@Autowired
	UserService userService;

	@Autowired
	DocumentService documentService;

	@PostMapping("signup")
	@ApiOperation("注册用户")
	public R signup(String phone, String pwd, Integer code){
		if(ObjectUtils.isEmpty(phone)||ObjectUtils.isEmpty(pwd)||ObjectUtils.isEmpty(code))
		{
			return R.fail().setMes("手机号,密码或验证码为空");
		}
		QueryWrapper<User> wrapper = new QueryWrapper<>();
		wrapper.eq("user_phone",phone);
		User one = userService.getOne(wrapper);
		if(!ObjectUtils.isEmpty(one)) {
			return R.fail().setMes("手机号已被注册");
		}
		//验证code
		if(code!=1234)
		{
			return R.fail().setMes("验证码已失效,请重新发送");
		}
		User user = new User()
				.setUserPhone(phone)
				.setUserPwd(pwd);
		boolean save = userService.save(user);
		if(!save)
			return R.fail().setMes("系统错误");
		//生成默认文件夹
		DocumentVo documentVo = new DocumentVo();
		documentVo.setDocId(0L);
		documentVo.setUserId(user.getUserId());
		documentVo.setDocName("默认文件夹");
		documentService.addDoc(documentVo);
		return R.ok().setMes("注册成功,请登录");

	}
	@GetMapping("SMS")
	@ApiOperation("发送验证码")
	public R sendSMS(String phone){
		//通过电话号通过相关服务,发送验证码
		return null;
	}
	@PostMapping("login")
	@ApiOperation("登录用户")
	public R login(String phone,String pwd){
		if(ObjectUtils.isEmpty(phone)||ObjectUtils.isEmpty(pwd))
			return R.fail().setMes("手机号或密码为空");
		QueryWrapper<User> wrapper = new QueryWrapper<>();
		wrapper.eq("user_phone",phone);
		wrapper.eq("user_pwd",pwd);
		User one = userService.getOne(wrapper);
		if(ObjectUtils.isEmpty(one))
			return R.fail().setMes("");
		one.setUserPwd("");
		return R.ok().setData(one);
	}
	@GetMapping("logout")
	@ApiOperation("登出账户")
	public R logout(){

		return null;
	}
	@PostMapping("update")
	@ApiOperation("更新账户信息")
	public R update(UserVo vo){
		User user = new User();
		BeanUtils.copyProperties(vo,user);
		boolean update = userService.updateById(user);
		if(update)
			return R.ok().setMes("修改成功");
		return R.fail().setMes("未知原因:修改失败");
	}
	@PostMapping("generate")
	public R generatePwd(@RequestBody Long userId,
						 @RequestBody String pwd){
		return null;
	}
	@DeleteMapping("delete/{id}")
	@ApiOperation("注销账户")
	public R delete(@PathVariable Long id){
		boolean delete=userService.deleteUser(id);
		boolean remove = userService.removeById(id);
		if(remove)
			return R.ok().setMes("删除成功");
		return R.fail("未知原因:删除失败");
	}
}
