package com.t.logic.controller;

import com.t.logic.entity.Vo.EsSearchVo;
import com.t.logic.service.impl.SearchServiceImpl;
import com.t.logic.utils.R;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@Api(tags = "与检索信息的相关请求，检索文献等")
@RestController
@RequestMapping("/search/")
@CrossOrigin
public class SearchController {

	@Autowired
	private SearchServiceImpl searchService;


	/**
	 *  搜索
	 * @param searchString 搜索关键词
	 * @param pageNo 分页开始
	 * @param pageSize 分页结束
	 * @param userId 用户id,为0是测试摸索
	 * @param searchType 搜索类型,1:时间从近到远搜索,2:相关度从大到小搜索
	 * @return
	 * @throws IOException
	 */
	@GetMapping("/{searchString}/{pageNo}/{pageSize}/{userId}/{docId}/{searchType}")
	public R search(@PathVariable String searchString,
						 @PathVariable int pageNo,
						 @PathVariable int pageSize,
						 @PathVariable Long userId,
						 @PathVariable Long docId,
						 @PathVariable Integer searchType) throws IOException {
		List<EsSearchVo> list = null;
		if(searchType==1)//按照相关度搜索
			list= searchService.searchPageByScore(searchString, pageNo, pageSize,userId,docId);
		if(searchType==2)//按照创建时间搜索
			list=searchService.searchPageByTime(searchString, pageNo, pageSize,userId,docId);
		int count=searchService.searchPageCount(searchString, pageNo, pageSize,userId,docId);
// Todo: 性能优化

		HashMap map=new HashMap();

		map.put("count",count);
		map.put("list",list);

		System.out.println(map);
		return R.ok(map);
	}



	@GetMapping("/{pdfId}/{searchString}")
	public R getDoc(@PathVariable Long pdfId,@PathVariable String searchString){
		Object getdoc = null;
		try {
			getdoc = searchService.getdoc(pdfId, searchString);
		} catch (IOException e) {
			throw new RuntimeException();
		}
		return R.ok(getdoc);
	}


}
