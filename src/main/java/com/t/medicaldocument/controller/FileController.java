package com.t.medicaldocument.controller;

import com.t.medicaldocument.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Map;
import java.util.UUID;

@Api(tags = "与文件的相关请求，包括上传文件，查看文献详情")
@RestController
@RequestMapping("/file")

public class FileController {
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
	public R fileUpload(@RequestParam("file") MultipartFile file,
						HttpServletRequest hsq) throws IOException {
		//接收上传文件
		//Receiving uploaded files
		String fileName = UUID.randomUUID().toString().replace("-","")
				+file.getOriginalFilename();
		String end=System.getProperty("user.dir")+fileName;
		String destFileName=hsq.getServletContext()
				.getRealPath("")+"uploaded"
				+ File.separator+fileName;
		File destFile = new File(end);

		// destFile.getParentFile().mkdirs();

		System.out.println(destFile);
		//将文件保存
		file.transferTo(destFile);

		//向前端模板引擎传入上传文件的地址
		//The address of the uploaded file is passed in to the front-end template engine
		// model.addAttribute("fileName","uploaded\\"+fileName);
		// model.addAttribute("path",destFile);
		//开始准备请求API
		//Start preparing the request API
		//创建请求头
		//Create request header
		HttpHeaders headers = new HttpHeaders();

		//设置请求头格式
		//Set the request header format
		headers.setContentType(MediaType.APPLICATION_JSON);
		//构建请求参数
		//Build request parameters
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		//读入静态资源文件
		//Read the static resource file
		InputStream imagePath = new FileInputStream(destFile);
		//添加请求参数images，并将Base64编码的图片传入
		//Add the request parameter Images and pass in the Base64 encoded image
		// map.add("images", ImageToBase64(imagePath));
		//构建请求
		//Build request
		HttpEntity<MultiValueMap<String, String>> request =
				new HttpEntity<MultiValueMap<String, String>>(map, headers);
		RestTemplate restTemplate = new RestTemplate();
		//发送请求
		//Send the request
		Map json = restTemplate.postForEntity("http://127.0.0.1:8866/predict/ocr_system", request, Map.class)
				.getBody();
		System.out.println(json);
		//解析Json返回值
		//Parse the Json return value


		// List<List<Map>> json1 = (List<List<Map>>) json.get("results");
		// //获取文件目录为后面画图做准备
		// //Get the file directory to prepare for later drawing
		// String tarImgPath = destFile.toString();
		// File srcImgFile = new File(tarImgPath);
		// System.out.println(srcImgFile);
		// //文件流转化为图片
		// //The file flows into images
		// Image srcImg = ImageIO.read(srcImgFile);
		// if (null == srcImg){
		// 	return R.fail().setMes("什么也没有,结束!");
		// }
		// //获取图片的宽
		// //Gets the width of the image
		// int srcImgWidth = srcImg.getWidth(null);
		// //获取图片的高
		// //Get the height of the image
		// int srcImgHeight = srcImg.getHeight(null);
		// //开始绘图主流程，创建画板设置画笔颜色等
		// //Start drawing main flow, create artboard, set brush color, etc
		// BufferedImage bufImg = new BufferedImage(srcImgWidth, srcImgHeight, BufferedImage.TYPE_INT_RGB);
		// Graphics2D g = bufImg.createGraphics();
		// g.setColor(Color.red);
		// g.drawImage(srcImg, 0, 0, srcImgWidth, srcImgHeight, null);
		// //循环遍历出所有内容
		// //Loop through everything
		// for (int i = 0; i < json1.get(0).size(); i++) {
		// 	System.out.println("当前的文字是：" + json1.get(0).get(i).get("text"));
		// 	System.out.println("可能的概率为：" + json1.get(0).get(i).get("confidence"));
		// 	List<List<Integer>> json2 = (List<List<Integer>>) json1.get(0).get(i).get("text_region");
		// 	System.out.println("文字的坐标" + json2);
		// 	int x = json2.get(0).get(0);
		// 	int y = json2.get(0).get(1);
		// 	int w = json2.get(1).get(0)-json2.get(0).get(0);
		// 	int h = json2.get(2).get(1)-json2.get(0).get(1);
		// 	g.drawRect(x,y,w,h);  //画出水印   Draw the watermark
		// }
		// //将内容提交到前端模板引擎
		// //Submit the content to the front-end template engine
		// model.addAttribute("z",json1.get(0));
		// g.dispose();
		// // 输出图片
		// //The output image
		// FileOutputStream outImgStream = new FileOutputStream(tarImgPath);
		// ImageIO.write(bufImg, "png", outImgStream);
		// System.out.println("画图完毕");
		// outImgStream.flush();
		// outImgStream.close();
		return null;
	}
	@GetMapping("download/{pdfId}")
	@ApiOperation("文献的下载")
	public R fileDownload(@PathVariable Long pdfId,HttpServletRequest hsr){
		return null;
	}
}
