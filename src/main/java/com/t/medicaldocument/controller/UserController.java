package com.t.medicaldocument.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/")
public class UserController {
	@PostMapping("login")
	public void login(){

	}
	@GetMapping("logout")
	public void logout(){

	}
}
