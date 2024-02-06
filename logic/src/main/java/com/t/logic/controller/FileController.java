package com.t.logic.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.t.logic.common.Job.AsyncTask;
import com.t.logic.config.MException;
import com.t.logic.entity.PdfFile;
import com.t.logic.entity.Vo.PdfFileVo;
import com.t.logic.entity.Vo.PdfFileVo2;
import com.t.logic.entity.Vo.PdfFileVo3;
import com.t.logic.service.DocumentService;
import com.t.logic.service.PdfFileService;
import com.t.logic.utils.FileUtils;
import com.t.logic.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

@Api(tags = "与文件的相关请求，包括上传文件，查看文献详情,修改删除文献.")
@RestController
@RequestMapping("/file/")
@CrossOrigin
// @Slf4j
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
	@Autowired
	DocumentService documentService;
	@Autowired
	private KafkaTemplate<String, Object> kafkaTemplate;

//	@PostMapping("upload/")
//	@ApiOperation("（已定2.0）文献的上传,并转换为图片")
//	@Transactional(rollbackFor = Exception.class)
//	@CacheEvict(cacheNames = "PdfFile+'_'+#pdf.getUserId()",
//			condition = "#result.getCode()==200",
//			allEntries = true)
	public R fileUploadAndDivide(@RequestParam(value = "file",required = true)
									 @RequestPart
									 MultipartFile file,
								 PdfFile pdf) throws IOException, MException {
		//接收上传文件
		if(pdf.getUserId()==null)
			return R.fail().setMes("请先登录");
		if (file.isEmpty())
			return R.fail().setMes("上传文件为空");
		String filename ="";
//				= pdfFileService.uploadPdfFile(file, pdf);
		if(filename==null)
			return R.fail().setMes("上传文件失败");
		//不成功就删除
		Integer count = pdfFileService.dividePDF(filename);
		if (count==null)
			throw new MException().put("filename",filename).setCode(301);
		//count 为页数
		pdf.setPdfPagecount(count);

		boolean save = pdfFileService.save(pdf);
		boolean updateSize = documentService.updateSize(1, 0L, 1, pdf.getUserId());
		if (!save||!updateSize)
			throw new MException().put("filename", filename).setCode(301);
		HashMap<String, Object> map = new HashMap<>();
		map.put("title",pdf.getPdfTitle());
		map.put("pdfId",pdf.getPdfId());
		map.put("status",pdf.getPdfStatus());
		map.put("createTime",pdf.getCreateTime());
		return R.ok(map);
	}
	@PostMapping("upload/")
	@ApiOperation("（已定3.0）文献的上传,并转换为图片,需要UserId以及PdfTitle")
	@Transactional(rollbackFor = Exception.class)
	@CacheEvict(cacheNames = "PdfFile+'_'+#pdf.getUserId()",
			condition = "#result.getCode()==200",
			allEntries = true)
	public R fileUploadToDivide(
								 PdfFile pdf) throws IOException, MException {
		//接收上传文件
		if(pdf.getUserId()==null||pdf.getPdfTitle()==null)
			return R.fail().setMes("请登录或上传文件");
		//从temp中寻找文件
		if (FileUtils.isExist(pdf.getPdfFileName()))
			return R.fail().setMes("文件不存在");
		String filename = pdfFileService.uploadPdfFile(pdf);
		if(filename==null)
			return R.fail().setMes("文件异常");
		//不成功就删除
		Integer count = pdfFileService.dividePDF(filename);
		if (count==null)
			throw new MException().put("filename",filename).setCode(301);
		//count 为页数
		pdf.setPdfPagecount(count);
		boolean save = pdfFileService.save(pdf);
		boolean updateSize = documentService.updateSize(1, 0L, 1, pdf.getUserId());
		if (!save||!updateSize)
			throw new MException().put("filename", filename).setCode(301);
		HashMap<String, Object> map = new HashMap<>();
		map.put("title",pdf.getPdfTitle());
		map.put("pdfId",pdf.getPdfId());
		map.put("status",pdf.getPdfStatus());
		map.put("createTime",pdf.getCreateTime());
		return R.ok(map);
	}
	@PostMapping("temp")
	@ApiOperation("上传文件，返回pdf文件名字")
	public R fileUpload(@RequestParam(value = "file",required = true)
			@RequestPart
			MultipartFile file){
		//接收上传文件
		if (file.isEmpty())
			return R.fail().setMes("上传文件为空");
		String filename = pdfFileService.upload(file);
		if(filename==null)
			return R.fail().setMes("上传文件失败");
		return R.ok(filename);
	}

	// @GetMapping("/analyze/structure")
	public R fileAnalyzeStructure(String filename, Integer count)
			throws Exception {
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
//			map.add("images", FileUtils.ImageToBase64(imagePath));
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
	@ApiOperation("（已定2.0）基于python对文献图片进行分析")
	@CacheEvict(cacheNames = "PdfFile+'_'+#pdf.getUserId()",
			condition = "#result.getCode()==200",
			allEntries = true)
	public R fileAnalyzeStructure2(@ApiParam(value="pdf文件的id", required = true)
											   Long pdfId,
								   @ApiParam(required = true)
										   Long userId)
			throws Exception {
		PdfFileVo vo = pdfFileService.fileExist(userId, pdfId);
		if (vo==null)
			return R.fail().setMes("没有对应文件,请删除当前文献信息,请重新上传文件");
		if (vo.getPdfStatus()==1)
			return R.fail().setMes("文件已经预测");
//
//		kafka 生产者
		if (false){
			return R.fail().setCode(400).setMes("服务繁忙，稍后再试");
		}
//	map设置参数
		HashMap<String, Object> map = new HashMap<>();
		map.put("pdfId",pdfId);
		map.put("userId",userId);
		map.put("file_name",vo.getPdfFileName());
		map.put("page_count",vo.getPdfPagecount());
		String s = JSON.toJSONString(map);
		kafkaTemplate.send("predict-task",s);
//		task.predictByPython(pdfId,userId, vo.getPdfFileName(), vo.getPdfPagecount());
		return R.ok().setMes("系统将对文献进行分析");
	}
	@GetMapping(value = "download/{pdfId}", produces = "application/pdf")
	@ApiOperation("（已定）文献的下载")
	public void fileDownload(@ApiParam(required = true)
								 @PathVariable Long pdfId,
						  HttpServletResponse response) {
		PdfFile pdfFile = pdfFileService.getById(pdfId);

		String pdfFileName = pdfFile.getPdfFileName();
		//文件下载
		try{
			pdfFileService.downloadPdfFile(response,pdfFileName);
		}catch (Exception e){
			// log.error(e.getMessage());
		}
	}
	@GetMapping("view/{userId}/{pdfId}")
	@ApiOperation("文献pdf的展示")
	@Cacheable(cacheNames = "PdfFile+'_'+#userId",
			key = "#root.methodName+'_'+#pdfId")
	public R fileView(@ApiParam(required = true)
						  @PathVariable Long pdfId,
					  @ApiParam(required = true)
						  @PathVariable Long userId){
		PdfFileVo vo = pdfFileService.fileExist(userId, pdfId);
		if (vo==null)
			return R.fail().setMes("没有对应文件,请删除当前文献信息,重新上传文件");
		return R.ok("/file/"+vo.getPdfFileName()+".pdf");
	}
	@GetMapping("view2/{userId}/{pdfId}")
	@ApiOperation("文献pdf图片的展示")
	public R fileView2(@ApiParam(required = true)
						  @PathVariable Long pdfId,
					  @ApiParam(required = true)
						  @PathVariable Long userId){
		PdfFileVo vo = pdfFileService.fileExist(userId, pdfId);
		if (vo==null)
			return R.fail().setMes("没有对应文件,请删除当前文献信息,重新上传文件");
		ArrayList<String> picList = new ArrayList<>();
		for (Integer integer = 0; integer < vo.getPdfPagecount(); integer++) {
//			Todo: 应动态的添加参数
//			picList.add("http://192.168.43.61:8081/pic/"+vo.getPdfFileName()+"/"+integer+".jpg");
			picList.add("http://localhost:8081/pic/"+vo.getPdfFileName()+"/"+integer+".jpg");
		}
		return R.ok(picList);
	}

	@GetMapping("echo/{userId}/{pdfId}")
	@ApiOperation("文献信息的回显")
	@Cacheable(cacheNames = "PdfFile+'_'+#userId",
			key = "#root.methodName+'_'+#pdfId")
	public R fileEcho(
			@ApiParam(required = true)
			@PathVariable Long pdfId,
			@ApiParam(required = true)
			@PathVariable Long userId)
	{
		if (pdfId==null||userId==null)
			return R.fail().setMes("错误");
		PdfFileVo vo=pdfFileService.fileEcho(userId,pdfId);
		if (vo==null)
			return R.fail().setMes("错误");
		return R.ok(vo);
	}
	@PostMapping("update")
	@ApiOperation("（已定）单个文献信息的修改(可以实现文献的文件夹的变动)")
	@CacheEvict(cacheNames = "PdfFile+'_'+#pdf.getUserId()",
			condition = "#result.getCode()==200",
			allEntries = true)
	public R fileUpdate( PdfFileVo2 pdf){
		boolean update=pdfFileService.fileUpdate(pdf);
		if (update)
			return R.ok().setMes("修改成功");
		return R.fail().setMes("修改失败");
	}
	@DeleteMapping("delete/{userId}/{docId}")
	@ApiOperation("（已定）同个文件夹中文献的删除")
	@CacheEvict(cacheNames = "PdfFile+'_'+#userId",
			condition = "#result.getCode()==200",
			allEntries = true)
	public R fileDelete( @ApiParam(required = true)
								@RequestBody List<Long> ids,
						@ApiParam(value="当前同个文件夹的docId")
							@PathVariable Long docId,
						 @ApiParam(required = true)
						@PathVariable Long userId){
		boolean fileDelete=pdfFileService.fileDelete(ids,docId,userId);
		if (fileDelete)
			return R.ok().setMes("删除成功");
		return R.fail().setMes("删除失败");
	}
	@GetMapping("search/{page}/{size}")
	@ApiOperation("（已定）根据文件夹id和用户id 分页 查询文件夹中的文献")
	@Cacheable(cacheNames = "PdfFile+'_'+#vo.getUserId()",
			key = "#root.methodName+'_'+#page+'_'+#size")
	public R fileSearchByDoc(@ApiParam(required = true)
								 @PathVariable Integer page,
							 @ApiParam(required = true)
							 @PathVariable Integer size,
							 @ApiParam(required = true)PdfFileVo3 vo){
		if(vo==null)
			return R.fail().setMes("错误");
		if (vo.getUserId()==null)
			return R.fail().setMes("请登录");
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
	@GetMapping("fetch/{userId}/{status}")
	@ApiOperation("（已定）根据用户id和状态 查询时间前10的文献")
	public R fileFetchByStatus(@PathVariable Long userId,@PathVariable Integer status){
		if(userId==null)
			return R.fail().setMes("请登录");
		if(status==null)
			return R.fail().setMes("错误");
		List<PdfFileVo> list=pdfFileService.fileGetLats10(userId,status);
		HashMap<String, Object> map = new HashMap<>();
		map.put("data",list);
		return R.ok(map);
	}
	@PutMapping("place/{userId}/{newDocId}")
	@ApiOperation("(归档)将多个文献从同一个文件夹归入到另一个文档")
	@CacheEvict(cacheNames = "PdfFile+'_'+#userId",
			condition = "#result.getCode()==200",
			allEntries = true)
	public R filePlace(@ApiParam(required = true) @RequestBody List<Long> ids,
					   @ApiParam(required = true) @PathVariable Long userId,
					   @ApiParam(required = true) @PathVariable Long newDocId){
		if(newDocId==null)
			return R.fail().setMes("错误");
		if (ids==null)
			return R.fail().setMes("请选择文献");
		if (ids.isEmpty())
			return R.fail().setMes("请选择文献");
		boolean moveFile = pdfFileService.fileMove(ids, userId, newDocId);
		if (moveFile)
			return R.ok().setMes("归档成功");
		return R.fail().setMes("归档失败,请检查目标文件夹容量大小与权限");
	}
//	Todo
	@GetMapping("/doi")
	@ApiOperation("根据DOI获取PDF文件")
	R fileGetByDOI(){
		return null;
	}
	@PutMapping("/parse")
	@ApiOperation("根据DOI Excel文件获取PDF文件")
	R fileGetByFileDOI(){
		return null;
	}

	// @DeleteMapping("remove/{userId}/{oldDocId}")
	// @ApiOperation("移除同个文件夹中的文献(到默认文件夹中)")
	// public R fileRemove( @ApiParam(required = true)@RequestBody List<Long> ids,
	// 					 @ApiParam(required = true)@PathVariable Long oldDocId,
	// 					 @ApiParam(required = true)@PathVariable Long userId){
	// 	if(oldDocId==0)
	// 		log.error("系统接口暴露,逻辑失效");
	// 	if (ids==null)
	// 		return R.fail().setMes("请选择文献");
	// 	if (ids.isEmpty())
	// 		return R.fail().setMes("请选择文献");
	// 	boolean b = pdfFileService.removeFile(ids, oldDocId, userId);
	// 	if (b)
	// 		return R.ok().setMes("移除文献成功");
	// 	return R.fail().setMes("移除文献失败");
	// }

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
