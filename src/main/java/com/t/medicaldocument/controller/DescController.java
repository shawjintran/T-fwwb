package com.t.medicaldocument.controller;

import com.t.medicaldocument.entity.Vo.PdfDescVo;
import com.t.medicaldocument.service.PdfDescriptionService;
import com.t.medicaldocument.utils.R;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
	@PostMapping("predict")
	@ApiOperation("针对状态异常的Pdf文件进行重新预测")
	public R descPredict(Integer status,Long pdfId){
		//todo 未厘清函数逻辑
		return null;
	}

}
