package com.t.medicaldocument.Controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@Api("与文件的相关请求，包括上传文件，查看文献详情")
@RestController
@RequestMapping("/file")
public class FileController {
	@PostMapping("/upload")
	@ApiOperation("文献的上传")
	public String fileUpload(@RequestParam("file") MultipartFile file, HttpServletRequest req, Model model){
		return null;
	}
	@GetMapping("/download/{pdfId}")
	@ApiOperation("文献的下载")
	public void fileDownload(){

	}
}
