package com.t.logic.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.t.logic.entity.Employee;
import com.t.logic.entity.User;
import com.t.logic.entity.Vo.DocumentVo;
import com.t.logic.service.DocumentService;
import com.t.logic.service.EmployeeService;
import com.t.logic.service.UserService;
import com.t.logic.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/employ/")
@Api(tags = "内部员工的相关操作")
@CrossOrigin
public class EmployController {
	@Autowired
	EmployeeService employeeService;
	@Autowired
	UserService userService;
	@Autowired
	DocumentService documentService;
	@PostMapping("login")
	@ApiOperation("(已定)员工登录")
	public R login(@ApiParam(required = true)
							   String phone,
				   @ApiParam(required = true)
						   String pwd){
		if (phone==null||pwd==null||pwd.equals("")||phone.equals(""))
			return R.fail().setMes("请输入密码或手机号");
		QueryWrapper<Employee> wrapper = new QueryWrapper<>();
		// employee_phone,
		// 		employee_pwd,employee_status
		wrapper.eq("employee_phone",phone)
				.eq("employee_pwd",pwd)
				.eq("employee_status",0);
		Employee one = employeeService.getOne(wrapper);
		if (one==null)
			return R.fail().setMes("用户不存在");
		return R.ok(one);
	}
	@PostMapping("generate")
	@ApiOperation("(已定)员工更新密码")
	public R generatePwd(Long empId,String oldPwd,String newPwd){
		QueryWrapper<Employee> wrapper = new QueryWrapper<>();
		wrapper.eq("employee_id",empId);
		Employee one = employeeService.getOne(wrapper);
		if (one==null)
			return R.fail().setMes("用户不存在");
		if (!one.getEmployeePwd().equals(oldPwd))
			return R.fail().setMes("旧密码错误");
		if (one.getEmployeePwd().equals(newPwd))
			return R.fail().setMes("新旧密码一致");
		one.setEmployeePwd(newPwd);
		boolean update = employeeService.updateById(one);
		if (!update)
			return R.fail().setMes("修改密码失败");
		return R.ok().setMes("修改密码成功");
	}
	@PostMapping("custom")
	@Transactional(rollbackFor = Exception.class)
	@ApiOperation("(已定)添加超级用户")
	public R addUser(User user){
		if (user.getUserPhone()==null||user.getUserPwd()==null)
			return R.fail().setMes("用户的电话或者密码未填写");
		user.setUserCapacity(Integer.MAX_VALUE);
		boolean save = userService.save(user);
		boolean doc = documentService.addDoc(new DocumentVo(0L, user.getUserId(), "默认"));
		if (save&&doc)
			return R.ok().setMes("创建超级用户成功");
		TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		return R.fail().setMes("创建失败");
	};
	//添加员工只能后端成员手工添加，不设置api
	//2023/3/27 员工登录
	//2023/3/27 员工更新密码
	//2023/3/27 员工添加企业账号
}
