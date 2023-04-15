package com.t.medicaldocument.controller;

import com.t.medicaldocument.entity.Vo.PdfDescVo;
import com.t.medicaldocument.service.PdfDescriptionService;
import com.t.medicaldocument.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/desc/")
@Api(tags = "与pdf文件预测后得到的描述有关")
@CrossOrigin
public class DescController {
	@Autowired
	PdfDescriptionService descriptionService;

	@GetMapping("search/{pdfId}")
	@ApiOperation("（已定）根据pdfId查询pdf经ocr后的字段")
	public R descSearchByPdfId(@ApiParam(required = true)
								   @PathVariable Long pdfId){
		List<PdfDescVo> pdfDescVos = descriptionService.descSearchByPdfId(pdfId);
		return R.ok(pdfDescVos);
	}


}
