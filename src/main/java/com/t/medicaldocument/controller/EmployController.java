package com.t.medicaldocument.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.t.medicaldocument.entity.Employee;
import com.t.medicaldocument.entity.User;
import com.t.medicaldocument.entity.Vo.DocumentVo;
import com.t.medicaldocument.service.DocumentService;
import com.t.medicaldocument.service.EmployeeService;
import com.t.medicaldocument.service.UserService;
import com.t.medicaldocument.utils.R;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/employ/")
public class EmployController {
	@Autowired
	EmployeeService employeeService;
	@Autowired
	UserService userService;
	@Autowired
	DocumentService documentService;
	@PostMapping("login")
	public R login(@ApiParam(required = true)
							   String phone,
				   @ApiParam(required = true)
						   String pwd){
		if (phone==null||pwd==null||pwd.equals("")||phone.equals(""))
			return R.fail().setMes("请输入密码或手机号");
		QueryWrapper<Employee> wrapper = new QueryWrapper<>();
		// employee_phone,
		// 		employee_pwd,employee_status
		wrapper.eq("employee_phone",phone).eq("employee_pwd",pwd).eq("employee_status",1);
		Employee one = employeeService.getOne(wrapper);
		if (one==null)
			return R.fail().setMes("用户不存在");
		return R.ok(one);
	}
	@PostMapping("generate")
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
	@PostMapping
	@Transactional(rollbackFor = Exception.class)
	public R addUser(User user){
		user.setUserCapacity(Integer.MAX_VALUE);
		boolean save = userService.save(user);
		boolean doc = documentService.addDoc(new DocumentVo(0L, user.getUserId(), "默认"));
		if (save&&doc)
			return R.ok().setMes("创建超级用户成功");
		TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		return R.fail().setMes("创建失败");
	};
	//员工只能后端成员手工添加，不设置api
	//2023/3/27 员工登录
	//2023/3/27 员工更新密码
	//2023/3/27 员工添加企业账号
}
