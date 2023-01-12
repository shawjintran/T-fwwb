package com.t.medicaldocument.Controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "与检索信息的相关请求，检索文献等")
@RestController
@RequestMapping("/search")
public class SearchController {
	@GetMapping("/{key}")
	@ApiOperation("输入检索的关键信息key，进行搜索")
	public void searchBy(@PathVariable String key){

	}
}
