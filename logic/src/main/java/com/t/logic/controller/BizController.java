package com.t.logic.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.t.logic.entity.Business;
import com.t.logic.service.BusinessService;
import com.t.logic.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/biz/")
@Api(tags = "业务相关操作")
@CrossOrigin
public class BizController {
	@Autowired
	BusinessService businessService;
	@PostMapping("add")
	@ApiOperation("(已定)添加业务信息。只能由employee操作")
	public R addBiz(Business biz){
		if (biz.getBizPoint()==null||biz.getBizPrice()==null)
			return R.fail().setMes("请输入价格或者积分数");
		boolean save = businessService.save(biz);
		if (!save)
			return R.fail().setMes("保存出错");
		return R.ok().setMes("添加业务成功");
	};
	@GetMapping("list/{page}/{size}")
	@ApiOperation("(已定)分页列出所有的业务")
	public R listBiz(@PathVariable int page, @PathVariable int size){
		Page<Business> businessPage = businessService.page(new Page<Business>(page, size), new QueryWrapper<Business>().eq("biz_status", 1));
		long total = businessPage.getTotal();
		List<Business> records = businessPage.getRecords();
		HashMap<String, Object> map = new HashMap<>();
		map.put("total",total);
		map.put("data",records);
		return R.ok(map);
	};
	@PostMapping("update")
	@ApiOperation("(已定)更新业务信息")
	public R updateBiz(Business biz){
		if (biz.getBizId()==null||biz.getBizPoint()==null||biz.getBizPrice()==null)
			return R.fail().setMes("输入有误");
		boolean update = businessService.updateById(biz);
		if (!update)
			return R.fail().setMes("更新失败");
		return R.ok().setMes("更新成功");
	};
	@GetMapping("echo/{bizId}")
	@ApiOperation("(已定)业务信息的回显")
	public R echoBiz(@PathVariable Long bizId){
		if (bizId==null)
			return R.fail().setMes("业务ID为空");
		Business byId = businessService.getById(bizId);
		if (byId==null)
			return R.fail().setMes("业务不存在");
		return R.ok(byId);
	};
	//  2023/3/27 添加业务
	// 2023/3/27 列出所有状态在的业务
	//  2023/3/27 业务的信息，状态更改
	// 2023/3/27 业务信息回显
}
