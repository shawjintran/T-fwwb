package com.t.logic.controller;

import com.t.logic.entity.Document;
import com.t.logic.entity.Vo.DocumentVo;
import com.t.logic.entity.Vo.ShareUserVo;
import com.t.logic.service.DocUserService;
import com.t.logic.service.DocumentService;
import com.t.logic.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/doc/")
@Api(tags = "用户文件夹的相关操作")
@CrossOrigin
public class DocController {
	@Autowired
	DocumentService documentService;
	@Autowired
	DocUserService docUserService;

	@GetMapping("search/{userId}")
	@ApiOperation("（未定）根据用户Id查询文件夹，以及查询可读的共享文件夹")
	public R searchReadDocById(@ApiParam(required = true) @PathVariable Long userId){
		if (userId==null)
			return R.fail().setMes("请先登录");
		List<Map<String, Object>> map = documentService.searchReadDocByUser(userId);
		//预期是装入 docid,docname,docsize,
		List<Long>docIds =docUserService.selectShareDoc(userId);
		if (docIds!=null&& docIds.size()>0){
			List<Document> documents = documentService.listByIds(docIds);
			documents.stream().forEach(item ->{
				Map<String, Object>map1= new HashMap<String, Object>();
				map1.put("name",item.getDocName());
				map1.put("size",item.getDocSize());
				map1.put("capacity",item.getDocCapacity());
				map1.put("docId",item.getDocId());
				map1.put("auth",item.getDocAuth());
				map.add(map1);
			});
		}
		Map<String,Object> data= new HashMap<>();
		data.put("data",map);
		data.put("size",map.size());
		return R.ok(data);
	}
	@GetMapping("sread/{userId}")
	@ApiOperation("（未定）根据用户Id查询文件夹，以及查询可写的共享文件夹名字")
	public R searchWriteDocNameById(@ApiParam(required = true) @PathVariable Long userId){
		if (userId==null)
			return R.fail().setMes("请先登录");
		List<Map<String, Object>> map = documentService.searchWriteDocNameByUser(userId);
		//预期是装入 docid,docname
		List<Long>docIds =docUserService.selectShareDoc(userId);
		if (docIds!=null&& docIds.size()>0){
			List<Document> documents = documentService.listByIds(docIds);
			documents.stream().forEach(item ->{
				Map<String, Object>map1= new HashMap<String, Object>();
				map1.put("name",item.getDocName());
				map1.put("size",item.getDocSize());
				map1.put("capacity",item.getDocCapacity());
				map1.put("docId",item.getDocId());
				map1.put("auth",item.getDocAuth());
				map.add(map1);
			});
		}
		Map<String,Object> data= new HashMap<>();
		data.put("data",map);
		data.put("size",map.size());
		return R.ok(data);
	}
	@PostMapping("add")
	@ApiOperation("（已定）添加文件夹")
	public R addDoc( DocumentVo doc){
		if(doc.getOwnId()==null)
			return R.fail().setMes("先登录");
		if (doc.getDocName()==null)
			return R.fail().setMes("请输入文件夹名字");
//		if (documentService.nameRepeat(doc.getDocName(),doc.getOwnId()))
//			return R.fail().setMes("文件夹名重复");
		boolean b = documentService.addDoc(doc);
		if (b)
			return R.ok().setMes("创建成功");
		return R.fail().setMes("未知错误,请等待");
	}
	@GetMapping("docUsers")
	@ApiOperation("未定，查找当前文件夹的共享成员")
	R selectDocUsers(Long docId){
		List<ShareUserVo> users=docUserService.selectDocUsers(docId);
		return R.ok().setData(users);
	}
	@DeleteMapping("delete/{userId}/{docId}")
	@ApiOperation("（已定）删除文件夹")
	public R deleteDoc(@ApiParam (required = true)
						   @PathVariable Long userId,
					   @ApiParam(required = true)
					   @PathVariable Long docId){
//		if (docId==0L)
//			return R.fail().setMes("默认文件夹不可删除");
		boolean b = documentService.removeByDocIdAndUserId(docId,userId);
		if (b)
			return R.ok().setMes("删除成功");
		return R.fail().setMes("删除失败");
	}
	@GetMapping("echo/{userId}/{docId}")
	@ApiOperation("文件夹信息回显")
	public R docEcho(@PathVariable Long docId, @PathVariable Long userId){
		if (docId==null||userId==null)
			return R.fail().setMes("错误");
		DocumentVo vo =documentService.docEcho(docId,userId);
		if(vo==null)
			return R.fail().setMes("错误");
		return R.ok(vo);
	}
	@PutMapping("update")
	@ApiOperation("更改文件夹的名字")
	public R updateDoc(DocumentVo doc){
//		boolean b = documentService.nameRepeat(doc.getDocName(), doc.getOwnId());
//		if (b)
//			return R.fail().setMes("文件夹名重复");
		boolean updateDoc = documentService.updateDoc(doc);
		if (updateDoc)
			return R.ok().setMes("更改成功");
		return R.fail().setMes("更改失败");
	}
//	Todo
	@ApiOperation("导出文献信息")
	R getDocRecord(){
		return null;
	}
}
