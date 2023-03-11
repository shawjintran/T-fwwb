package com.t.medicaldocument.controller;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.t.medicaldocument.common.Job.AsyncTask;
import com.t.medicaldocument.entity.PdfFile;
import com.t.medicaldocument.entity.Vo.PdfFileVo;
import com.t.medicaldocument.entity.Vo.PdfFileVo2;
import com.t.medicaldocument.entity.Vo.PdfFileVo3;
import com.t.medicaldocument.service.PdfFileService;
import com.t.medicaldocument.utils.FileUtils;
import com.t.medicaldocument.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutionException;

@Api(tags = "与文件的相关请求，包括上传文件，查看文献详情,修改删除文献.")
@RestController
@RequestMapping("/file/")
@Slf4j
/**
 * 1.上传Pdf文件,并保存
 * 2.将Pdf文件,以每张图片形式输入到python服务端中
 * 3.图片以多线程传入python服务端进行处理
 * 4.python服务端将图片先进行排版分析
 * 5.再将图片进行ocr识别
 * 6.对Pdf文件,建立实体类信息
 * 6
 */
public class FileController {
	@Autowired
	AsyncTask task;
	@Autowired
	PdfFileService pdfFileService;

	@PostMapping("upload/")
	@ApiOperation("文献的上传,并转换为图片")
	public R fileUploadAndDivide(@RequestParam("file")
									 @RequestPart MultipartFile file,
								 PdfFile pdf) throws IOException {
		//接收上传文件
		//Receiving uploaded files
		if(pdf.getUserId()==null)
			return R.fail("请先登录");
		if (file.isEmpty())
			return R.fail().setMes("上传文件为空");
		if(pdf.getPdfTitle()==null)
			return R.fail().setMes("出现错误");
		HashMap<String, Object> map = pdfFileService.uploadPdfFile(file, pdf);
		if(map==null)
			return R.fail().setMes("文件上传出现错误");

		Integer count = pdfFileService.dividePDF((String) map.get("filename"));
		if (count==null)
			return R.fail("未知错误");
		//count 为 页数
		pdf.setPdfPagecount(count);
		boolean save = pdfFileService.save(pdf);
		map.put("count",count);
		map.put("id",pdf.getPdfId());
		if (save)
			return R.ok().setData(map);
		//Todo:不成功 需要将本地文件进行删除
		return R.fail("未知错误");
	}

	// @GetMapping("/analyze/structure")
	public R fileAnalyzeStructure(String filename, Integer count) throws Exception {
		String picUrl=System.getProperty("user.dir")+File.separator+"pic"+File.separator+filename+File.separator;
		HttpHeaders headers = new HttpHeaders();

		//对每页进行版面分析+ocr
		//设置请求头格式
		//Set the request header format
		headers.setContentType(MediaType.APPLICATION_JSON);
		//构建请求参数
		//Build request parameters

		ArrayList<HashMap<String, Object>> list = new ArrayList<>();
		for (int i = 0; i < count; i++) {

			//读入静态资源文件
			InputStream imagePath = new FileInputStream(picUrl+filename+"_"+i+".jpg");
			//添加请求参数images，并将Base64编码的图片传入
			MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
			map.add("images", FileUtils.ImageToBase64(imagePath));
			//构建请求
			//Build request
			HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
			RestTemplate restTemplate = new RestTemplate();
			//发送请求
			Map json = restTemplate.postForEntity("http://127.0.0.1:8868/predict/structure_system", request, Map.class).getBody();
			JSONObject o = new JSONObject(json);
			if (o.get("results")==null)
				return R.fail();
			JSONArray res = new JSONArray((List) o.get("results"));
			if (res.size()<=0)
				return R.fail();
			// HashMap<String, Object> results = PdfStructure(new JSONObject((Map) res.get(0)));
			//根据返回数据格式,用对应的数据结构 进行读取值
			// list.add(results);
		}
		return R.ok(list);
	}
	@GetMapping("analyze/structure")
	@ApiOperation("基于python对文献图片进行分析")
	public R fileAnalyzeStructure2(@ApiParam(name="pdf文件的id") Long pdfId,
								   String filename,
								   Integer count) throws IOException, InterruptedException, ExecutionException {
		task.predictByPython(pdfId, filename, count);
		return R.ok("系统将对文献进行分析");
	}
	@GetMapping(value = "download/{pdfId}", produces = "application/pdf")
	@ApiOperation("文献的下载")
	public void fileDownload(@PathVariable Long pdfId,
						  HttpServletResponse response) {
		PdfFile pdfFile = pdfFileService.getById(pdfId);

		String pdfFileName = pdfFile.getPdfFileName();
		//文件下载
		try{
			pdfFileService.downloadPdfFile(response,pdfFileName);
		}catch (Exception e){
			log.error(e.getMessage());
			return;
		}
		return ;
	}
	@PostMapping("update")
	@ApiOperation("文献信息的修改(可以实现文献的文件夹的变动)")
	public R fileUpdate( PdfFileVo2 pdf){
		boolean update=pdfFileService.fileUpdate(pdf);
		if (update)
			return R.ok("修改成功");
		return R.fail("修改失败");
	}
	@DeleteMapping("delete/{userId}/{docId}")
	@ApiOperation("同个文件夹中文献的删除")
	public R fileDelete( List<Long> ids,
						@ApiParam(name="当前同个文件夹的docId")
							@PathVariable Long docId,
						@PathVariable Long userId){
		boolean fileDelete=pdfFileService.fileDelete(ids,docId,userId);
		if (fileDelete)
			return R.ok("删除成功");
		return R.fail("删除失败");
	}
	@GetMapping("search/{page}/{size}")
	@ApiOperation("根据文件夹id和用户id 分页 查询文件夹中的文献")
	public R fileSearchByDoc(@PathVariable Integer page,
							   @PathVariable Integer size, PdfFileVo3 vo){
		if(vo==null)
			return R.fail();
		if (vo.getUserId()==null)
			return R.fail();
		Integer total = pdfFileService.fileCount(vo.getDocId(), vo.getUserId());
		HashMap<String, Object> map = new HashMap<>(2);
		map.put("all",total);
		map.put("data",null);
		if (total/size+1<page)
			return R.ok(map);
		List<PdfFileVo> list=pdfFileService.fileSearchPageById(page, size, total, vo);
		map.put("data",list);
		return R.ok(map);
	}
	@PutMapping("place/{userId}/{newDocId}")
	@ApiOperation("(归档)文献们从默认文件夹归入到同一个文档(非默认0)")
	public R filePlace( List<Long> ids,
					   @PathVariable Long userId,
					   @PathVariable Long newDocId){
		if(newDocId==0)
			log.error("系统接口暴露,逻辑失效");
		if (ids==null)
			return R.fail("请选择文献");
		if (ids.isEmpty())
			return R.fail("请选择文献");
		//重写方法
		boolean b = pdfFileService.placeFile(ids, newDocId, userId);
		if (b)
			return R.ok("归档成功");
		return R.fail("归档失败");
	}
	@DeleteMapping("remove/{userId}/{oldDocId}")
	@ApiOperation("移除同个文件夹中的文献(到默认文件夹中)")
	public R fileRemove( List<Long> ids,
						@PathVariable Long oldDocId,
						@PathVariable Long userId){
		if(oldDocId==0)
			log.error("系统接口暴露,逻辑失效");
		if (ids==null)
			return R.fail("请选择文献");
		if (ids.isEmpty())
			return R.fail("请选择文献");
		boolean b = pdfFileService.removeFile(ids, oldDocId, userId);
		if (b)
			return R.ok("移除文献成功");
		return R.fail("移除文献失败");
	}
	// @PutMapping("removeTo/{docId}/{mode}")
	// @ApiOperation("修改文献文件的信息")
	// public R removeFile(@PathVariable Long id,@PathVariable Long docId,@PathVariable Integer mode){
	// 	if (id==null)
	// 		return R.fail();
	// 	pdfFileService.
	// 	pdfFileService.removeFile(ids,mode);
	// 	return R.fail();
	// }
}
