package com.t.medicaldocument.controller;

import ch.qos.logback.core.util.FileUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson2.JSON;
import com.t.medicaldocument.utils.FileUtils;
import com.t.medicaldocument.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

@Api(tags = "与文件的相关请求，包括上传文件，查看文献详情")
@RestController
@RequestMapping("/file")
@Slf4j
public class FileController {
	FileUtils utils=new FileUtils();
	@PostMapping("/upload/")
	@ApiOperation("文献的上传")
	/**
	 * 1.上传Pdf文件,并保存
	 * 2.将Pdf文件,以每张图片形式输入到python服务端中
	 * 3.图片以多线程传入python服务端进行处理
	 * 4.python服务端将图片先进行排版分析
	 * 5.再将图片进行ocr识别
	 * 6.对Pdf文件,建立实体类信息
	 * 6
	 */

	public R fileUpload(@RequestParam("file") @RequestPart MultipartFile file) throws IOException {
		//接收上传文件
		//Receiving uploaded files
		String fileName = UUID.randomUUID()
				.toString()
				.replace("-","")
				+".pdf";
		String end = System.getProperty("user.dir")+File.separator+"pdf"+File.separator+fileName;
		File destFile = new File(end);
		destFile.getParentFile().mkdirs();
		//将文件保存
		file.transferTo(destFile);
		String[] split = fileName.split("\\.");
		return R.ok().setData(split[0]);
	}
	@GetMapping("/divide/{filename}")
	@ApiOperation("将pdf文件转换为图片")
	public R fileToDivided(@PathVariable String filename) throws IOException {
		String pdfUrl=System.getProperty("user.dir")+File.separator+"pdf"+File.separator+filename+".pdf";
		String picUrl=System.getProperty("user.dir")+File.separator+"pic"+File.separator+filename;
		PDDocument doc = null;
		ByteArrayOutputStream os = null;
		InputStream stream = null;
		OutputStream out = null;
		int pageCount;
		try {
			// pdf路径
			stream = new FileInputStream(pdfUrl);
			// 加载解析PDF文件
			doc = PDDocument.load(stream);
			PDFRenderer pdfRenderer = new PDFRenderer(doc);
			PDPageTree pages = doc.getPages();
			pageCount = pages.getCount();
			log.info(String.valueOf(pages.get(0).getMediaBox().getWidth()));
			for (int i = 0; i < pageCount; i++) {
				BufferedImage bim = pdfRenderer.renderImageWithDPI(i, 300);
				os = new ByteArrayOutputStream();
				ImageIO.write(bim, "jpg", os);
				byte[] dataList = os.toByteArray();
				// jpg文件转出路径
				File file = new File(picUrl+"\\" + i + ".jpg");

				if (!file.getParentFile().exists()) {
					// 不存在则创建父目录及子文件
					file.getParentFile().mkdirs();
					file.createNewFile();
				}
				out = new FileOutputStream(file);
				out.write(dataList);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (doc != null) doc.close();
			if (os != null) os.close();
			if (stream != null) stream.close();
			if (out != null) out.close();
		}
		HashMap<String, Object> map = new HashMap<>();
		map.put("filename",filename);
		map.put("count",pageCount);
		return R.ok().setData(map);
	}
	@GetMapping("/analyze/structure")
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
			//Read the static resource file
			InputStream imagePath = new FileInputStream(picUrl+i+".jpg");
			//添加请求参数images，并将Base64编码的图片传入
			//Add the request parameter Images and pass in the Base64 encoded image
			MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
			map.add("images", ImageToBase64(imagePath));
			//构建请求
			//Build request
			HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
			RestTemplate restTemplate = new RestTemplate();
			//发送请求
			//Send the request
			Map json = restTemplate.postForEntity("http://127.0.0.1:8868/predict/structure_system", request, Map.class).getBody();
			JSONObject o = new JSONObject(json);
			if (o.get("results")==null)
				return R.fail();
			JSONArray res = new JSONArray((List) o.get("results"));
			if (res.size()<=0)
				return R.fail();
			HashMap<String, Object> results = utils.PdfStructure(new JSONObject((Map) res.get(0)));

			//根据返回数据格式,用对应的数据结构 进行读取值
			list.add(results);

			// System.out.println(json);
			// //解析Json返回值
			// //Parse the Json return value
			// List<List<Map>> json1 = (List<List<Map>>) json.get("results");

		}
		return R.ok(list);
	}
	@GetMapping("/analyze/ocr")
	public R fileAnalyzeOcr(String filename, Integer count){
		String picUrl=System.getProperty("user.dir")+File.separator+"pic"+File.separator+filename+File.separator;
		for (int i = 0; i < count; i++) {

		}
		return null;
	}
	@GetMapping("download/{pdfId}")
	@ApiOperation("文献的下载")
	public R fileDownload(@PathVariable Long pdfId,HttpServletRequest hsr){
		return null;
	}
	private String ImageToBase64(InputStream imgPath) {
		byte[] data = null;
		// 读取图片字节数组
		//Read the image byte array
		try {
			InputStream in = imgPath;
			System.out.println(imgPath);
			data = new byte[in.available()];
			in.read(data);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 对字节数组Base64编码
		//Base64 encoding of byte array
		BASE64Encoder encoder = new BASE64Encoder();
		// 返回Base64编码过的字节数组字符串
		//Returns a Base64 encoded byte array string
		//System.out.println("图片转换Base64:" + encoder.encode(Objects.requireNonNull(data)));
		return encoder.encode(Objects.requireNonNull(data));
	}
}
