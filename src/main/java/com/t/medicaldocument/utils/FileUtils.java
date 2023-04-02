package com.t.medicaldocument.utils;


import java.util.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartFile;
import java.util.Base64.Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

import java.util.Objects;


/**
 * 对文件进行相关操作处理
 */

@Configuration
public class FileUtils {

//	public static String pdf_location= System.getProperty("user.dir")+File.separator+"pdf"+File.separator;

	public static String pdf_location;
//	static String pic_location= System.getProperty("user.dir")+File.separator+"pic"+File.separator;

	public static String pic_location;
//	static String res_location= System.getProperty("user.dir")+File.separator+"res"+File.separator;

	public static String res_location;



	public static String Server;

	static public void savePDF(MultipartFile file,String filename)
			throws IOException {
		String end = pdf_location+
				filename;
		File destFile = new File(end);
		destFile.getParentFile().mkdirs();
		//将文件保存
		file.transferTo(destFile);
	}
	static public void deletePDF(String filename){
		File pdf = new File(pdf_location + filename + ".pdf");
		File pic = new File(pic_location + filename);
		File res = new File(res_location + filename);
		if (pdf.exists())
			pdf.delete();
		if (pic.exists())
			pdf.delete();
		if (res.exists())
			res.delete();
	}
	static public Boolean downloadPDF(OutputStream out, String filename)
			throws IOException {
		File file = new File(pdf_location + filename + ".pdf");
		if (!file.exists())
			return false;
		IOUtils.copy(new FileInputStream(file),out);
		out.flush();
		return true;
	}
	static public int dividePDF(String filename)
			throws IOException {
		String pdfUrl=pdf_location+filename+".pdf";
		String picUrl=pic_location+filename;
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
			for (int i = 0; i < pages.getCount(); i++) {
				BufferedImage bim = pdfRenderer.renderImageWithDPI(i, 300);
				os = new ByteArrayOutputStream();
				ImageIO.write(bim, "jpg", os);
				byte[] dataList = os.toByteArray();
				// jpg文件转出路径
				File file = new File(picUrl+"\\" +i + ".jpg");

				if (!file.getParentFile().exists()) {
					// 不存在则创建父目录及子文件
					file.getParentFile().mkdirs();
					file.createNewFile();
				}
				out = new FileOutputStream(file);
				out.write(dataList);
			}
			pageCount = pages.getCount();
		} finally {
			if (doc != null) doc.close();
			if (os != null) os.close();
			if (stream != null) stream.close();
			if (out != null) out.close();
		}
		return pageCount;
	}


	static public byte[] ImageToBase64(InputStream imgPath) {
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
		Encoder encoder = Base64.getEncoder();
		// 返回Base64编码过的字节数组字符串
		//Returns a Base64 encoded byte array string
		//System.out.println("图片转换Base64:" + encoder.encode(Objects.requireNonNull(data)));
		return encoder.encode(Objects.requireNonNull(data));
	}

	// TODO: 2023/3/31 静态属性注入
	@Value("${pdf.location}")
	public  void setPdf_location(String pdf_location) {
		FileUtils.pdf_location = pdf_location;
	}
	@Value("${pic.location}")
	public  void setPic_location(String pic_location) {
		FileUtils.pic_location = pic_location;
	}
	@Value("${res.location}")
	public void setRes_location(String res_location) {
		FileUtils.res_location = res_location;
	}
	@Value("${self.server}")
	public void setServer(String server) {
		Server = server;
	}
}
