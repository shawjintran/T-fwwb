package com.t.medicaldocument.controller;

import com.t.medicaldocument.utils.R;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/")
@Api(tags = "用户的登录登出")
public class UserController {

	@PostMapping("signup")
	public R signup(){
		return null;
	}
	@GetMapping("SMS")
	public R sendSMS(){
		return null;
	}
	@PostMapping("login")
	public R login(){
		return null;
	}
	@GetMapping("logout")
	public R logout(){
		return null;
	}
}
