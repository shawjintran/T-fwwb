package com.t.medicaldocument.controller;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.t.medicaldocument.config.AsyncTask;
import com.t.medicaldocument.entity.PdfFile;
import com.t.medicaldocument.entity.Vo.PdfFileVo;
import com.t.medicaldocument.entity.Vo.PdfFileVo2;
import com.t.medicaldocument.service.PdfDescriptionService;
import com.t.medicaldocument.service.PdfFileService;
import com.t.medicaldocument.utils.FileUtils;
import com.t.medicaldocument.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
import javax.servlet.http.HttpServletRequest;
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
	public R fileUploadAndDivide(@RequestParam("file") @RequestPart MultipartFile file, PdfFile pdf) throws IOException {
		//Todo:缺少用户id
		//接收上传文件
		//Receiving uploaded files
		if (file.isEmpty())
			return R.fail().setMes("上传文件为空");
		if(pdf.getPdfAuthor()==null||pdf.getPdfTitle()==null)
			return R.fail().setMes("请输入pdf文献的作者与题目");
		HashMap<String, Object> map = pdfFileService.uploadPdfFile(file, pdf);
		//Todo:添加到用户的默认文件夹中
		if(map==null)
			return R.fail().setMes("上传文件失败");
		Integer count = pdfFileService.dividePDF((String) map.get("filename"));
		if (count==null)
			return R.fail("未知错误");
		map.put("count",count);
		return R.ok().setData(map);
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
	public R fileAnalyzeStructure2(Long id,String filename, Integer count) throws IOException, InterruptedException, ExecutionException {
		task.predictByPython(id, filename, count);
		return R.ok("系统将对文献进行分析");
	}
	@GetMapping("download/{pdfId}")
	@ApiOperation("文献的下载")
	public R fileDownload(@PathVariable Long pdfId,HttpServletRequest hsr){
		return null;
	}
	@PostMapping("update")
	@ApiOperation("文献信息的修改")
	public R fileUpdate(@RequestBody PdfFileVo2 pdf){
		boolean update=pdfFileService.fileUpdate(pdf);
		if (update)
			return R.ok("修改成功");
		return R.fail("修改失败");
	}
	@DeleteMapping("delete/{docId}")
	@ApiOperation("同个文件夹中文献的删除")
	public R fileDelete(@RequestBody List<Long> ids,@PathVariable Long docId){
		//Todo:注释 这是旧还是新docId
		boolean fileDelete=pdfFileService.fileDelete(ids,docId);
		if (fileDelete)
			return R.ok("删除成功");
		return R.fail("删除失败");
	}
	@GetMapping("search/{docId}")
	@ApiOperation("根据文件夹id查询文献")
	public R fileSearchByDocId(@PathVariable Long docId){
		List<PdfFileVo> list=pdfFileService.fileSearchByDocId(docId);
		return R.ok(list);
	}
	@PutMapping("place/{docId}")
	@ApiOperation("文献的归档")
	public R filePlace(@RequestBody List<Long> ids,@PathVariable Long docId){
		//Todo:缺少用户id
		if (ids==null)
			return R.fail("");
		if (ids.isEmpty())
			return R.fail("");
		boolean b = pdfFileService.removeFile(ids, docId, 1);
		if (b)
			return R.ok("归档成功");
		return R.fail("归档失败");
	}
	@PutMapping("remove")
	@ApiOperation("移除文件夹中的文献")
	public R fileRemove(@RequestBody List<Long> ids){
		//Todo: 缺少旧的docId 修改原文件夹大小
		if (ids==null)
			return R.fail("请选择文献");
		if (ids.isEmpty())
			return R.fail("请选择文献");
		pdfFileService.removeFile(ids,0L,2);
		return R.ok("移除文献成功");
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
