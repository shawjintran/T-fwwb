package com.t.medicaldocument.controller;

import com.t.medicaldocument.entity.Bo.DocumentBo;
import com.t.medicaldocument.entity.PdfDescription;
import com.t.medicaldocument.entity.Vo.SearchShow;
import com.t.medicaldocument.service.impl.SearchServiceImpl;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Api(tags = "与检索信息的相关请求，检索文献等")
@RestController
@RequestMapping("/search/")
public class SearchController {

	@Autowired
	private SearchServiceImpl searchService;


	@GetMapping("/{keyword}/{pageNo}/{pageSize}")
	public Object search(@PathVariable String keyword, @PathVariable int pageNo, @PathVariable int pageSize) throws IOException {
		List<SearchShow> maps = searchService.searchPage(keyword, pageNo, pageSize);

		///TODO 需要实现文献合并,map里是页数据条,需要转成文献数据条
		return maps;
	}




}