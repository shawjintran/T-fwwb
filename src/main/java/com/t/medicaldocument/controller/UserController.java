package com.t.medicaldocument.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.t.medicaldocument.entity.User;
import com.t.medicaldocument.service.UserService;
import com.t.medicaldocument.utils.R;
import io.swagger.annotations.Api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/")
@Api(tags = "用户的登录登出")
public class UserController {

	@Autowired
	UserService userService;

	@PostMapping("signup")
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
		boolean save = userService.save(new User()
				.setUserPhone(phone)
				.setUserPwd(pwd));
		if(save)
			return R.ok().setMes("注册成功,请登录");
		else
			return R.fail().setMes("系统错误");

	}
	@GetMapping("SMS")
	public R sendSMS(String phone){
		//通过电话号通过相关服务,发送验证码
		return null;
	}
	@PostMapping("login")
	public R login(String phone,String pwd){

		if(ObjectUtils.isEmpty(phone)||ObjectUtils.isEmpty(pwd))
		{
			return R.fail().setMes("手机或密码为空");
		}
		QueryWrapper<User> wrapper = new QueryWrapper<>();
		wrapper.eq("user_phone",phone);
		wrapper.eq("user_pwd",pwd);
		User one = userService.getOne(wrapper);
		if(ObjectUtils.isEmpty(one))
			return R.fail().setMes("");
		return R.ok().setData(one);
	}
	@GetMapping("logout")
	public R logout(){

		return null;
	}
	@PostMapping("update")
	public R update(@RequestBody User user){
		boolean update = userService.updateById(user);
		if(update)
			return R.ok().setMes("修改成功");
		return R.fail().setMes("未知原因:修改失败");
	}
	@DeleteMapping("delete/{id}")
	public R delete(@PathVariable Long id){
		boolean remove = userService.removeById(id);
		if(remove)
			return R.ok().setMes("删除成功");
		return R.fail("未知原因:删除失败");
	}
}
