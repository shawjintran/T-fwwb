package com.t.medicaldocument.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import com.t.medicaldocument.entity.Document;
import com.t.medicaldocument.entity.User;
import com.t.medicaldocument.entity.Vo.DocumentVo;
import com.t.medicaldocument.entity.Vo.UserVo;
import com.t.medicaldocument.service.DocumentService;
import com.t.medicaldocument.service.UserService;
import com.t.medicaldocument.utils.R;
import io.swagger.annotations.Api;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;

@RestController
@RequestMapping("/user/")
@Api(tags = "用户的登录登出")
@Slf4j
@CrossOrigin
public class UserController {

	@Autowired
	UserService userService;
	@Autowired
	Producer producer;
	@Autowired
	DocumentService documentService;

	@PostMapping("signup")
	@ApiOperation("（已定）注册用户")
	public R signup(
			@ApiParam(required = true)
					String phone,
			@ApiParam(required = true)
					String pwd,
			@ApiParam(required = true)
					Integer code){
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
//	@GetMapping("SMS")
	@ApiOperation("(遗弃)发送验证码")
	public R sendSMS(@ApiParam(required = true)
								 String phone){
		//通过电话号通过相关服务,发送验证码
		return null;
	}
	@PostMapping("login")
	@ApiOperation("（已定）登录用户")
	public R login(@ApiParam(required = true)
							   String phone,
				   @ApiParam(required = true)
						   String pwd,
				   HttpSession session){
		if(ObjectUtils.isEmpty(phone)||ObjectUtils.isEmpty(pwd))
			return R.fail().setMes("手机号或密码为空");
		QueryWrapper<User> wrapper = new QueryWrapper<>();
		wrapper.eq("user_phone",phone);
		User one = userService.getOne(wrapper);
		if(ObjectUtils.isEmpty(one))
			return R.fail().setMes("用户未注册");
		if (pwd!=one.getUserPwd())
			return R.fail().setMes("密码错误");
		UserVo userVo = new UserVo();
		BeanUtils.copyProperties(one,userVo);
		session.setAttribute("user",one.getUserId());
		return R.ok(userVo);
	}
	@GetMapping("logout")
	@ApiOperation("登出账户")
	public R logout(HttpSession session){
		if (session.getAttribute("user")!=null)
			session.removeAttribute("user");
		return R.ok().setMes("成功");
	}
	@GetMapping("echo/{userId}")
	@ApiOperation("用户信息的回显")
	public R userEcho(@PathVariable Long userId){
		User byId = userService.getById(userId);
		if (byId==null)
			return R.fail().setMes("错误");
		UserVo vo = new UserVo();
		BeanUtils.copyProperties(byId,vo);
		return R.ok(vo);
	}
	@PostMapping("update")
	@ApiOperation("（已定）更新账户信息")
	public R update(UserVo vo){
		boolean update =userService.updateInfo(vo);
		if(update)
			return R.ok().setMes("修改成功");
		return R.fail().setMes("未知原因:修改失败");
	}
	@PostMapping("generate")
	@ApiOperation("（已定）更新用户密码")
	public R modifyPwd( Long userId,
						  String oldPwd,
						 String newPwd){
		if(oldPwd.equals(newPwd))
			return R.fail().setMes("新旧密码一致");
		boolean done=userService.generatePwd(userId,oldPwd,newPwd);
		if (done)
			return R.ok().setMes("修改密码成功");
		return R.fail().setMes("旧密码输入有误");
	}
	@DeleteMapping("delete/{id}")
	@ApiOperation("（已定）注销账户")
	public R delete(@ApiParam(required = true)
						@PathVariable Long id){
		boolean delete=userService.deleteUser(id);
		if(delete)
			return R.ok().setMes("删除成功");
		return R.fail().setMes("未知原因:删除失败");
	}

	@GetMapping(value = "pic", produces = "image/jpeg")
	@ApiOperation("生成验证码图片（前端可通过session中KAPTCHA_SESSION_KEY键取值先进行页面验证）")
	public void generatePic(HttpServletResponse response, HttpServletRequest request) throws IOException {
		response.setHeader("Cache-Control", "no-store,no-cache");
		response.setContentType("image/jpeg");
		//生成文字验证码
		// String text=producer.createText();
		String text="1234";
		//生成图片验证码
		BufferedImage image=producer.createImage(text);
		//保存验证码到session
		request.getSession().setAttribute(Constants.KAPTCHA_SESSION_KEY, text);
		ServletOutputStream out= null;
		out = response.getOutputStream();
		ImageIO.write(image, "jpg", out);
		try {
				out.close();
		} catch (IOException var2) {
			log.error("生成验证码出错");
		}
	}
}
