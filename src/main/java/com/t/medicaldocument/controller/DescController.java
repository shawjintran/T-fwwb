package com.t.medicaldocument.controller;

import com.t.medicaldocument.entity.Vo.PdfDescVo;
import com.t.medicaldocument.service.PdfDescriptionService;
import com.t.medicaldocument.utils.R;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/desc/")
public class DescController {
	@Autowired
	PdfDescriptionService descriptionService;

	@GetMapping("search/{pdfId}")
	@ApiOperation("根据pdfId查询pdf经ocr后的字段")
	public R descSearchByPdfId(@PathVariable Long pdfId){
		List<PdfDescVo> pdfDescVos = descriptionService.descSearchByPdfId(pdfId);
		return R.ok(pdfDescVos);
	}

}
