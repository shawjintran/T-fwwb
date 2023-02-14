package com.t.medicaldocument.controller;

import com.t.medicaldocument.entity.Document;
import com.t.medicaldocument.entity.Vo.DocumentVo;
import com.t.medicaldocument.service.DocumentService;
import com.t.medicaldocument.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/doc/")
@Api(tags = "用户文件夹归档的相关操作")
public class DocController {
	@Autowired
	DocumentService documentService;
	@GetMapping("search/{uId}")
	@ApiOperation("根据用户Id查询文件夹")
	public R searchDocById(@PathVariable Long uId){
		if (uId==null)
			return R.fail();
		List<Map<String, Object>> map = documentService.searchDocById(uId);
		Map<String,Object> data= new HashMap<>();
		data.put("data",map);
		data.put("size",map.size());
		return R.ok(data);
	}
	@PostMapping("add")
	@ApiOperation("添加文件夹")
	public R addDoc(@RequestBody DocumentVo doc){
		if(doc.getUserId()==null)
			return R.fail("先登录");
		if (doc.getDocName()==null)
			return R.fail("请输入文件夹名字");
		if (documentService.nameRepeat(doc.getDocName()))
			return R.fail("文件夹名重复");

		boolean b = documentService.addDoc(doc);
		if (b)
			return R.ok("创建成功");
		return R.fail("未知错误,请等待");
	}
	@DeleteMapping("delete/{docId}")
	@ApiOperation("删除文件夹")
	public R deleteDoc(@PathVariable Long docId){
		boolean b = documentService.removeById(docId);
		if (b)
			return R.ok();
		return R.fail();
	}
	@PutMapping("update/name")
	@ApiOperation("更改文件夹的名字")
	public R updateDoc(@RequestBody DocumentVo doc ){
		boolean updateDoc = documentService.updateDoc(doc);
		if (updateDoc)
			return R.ok();
		return R.fail();
	}
}
