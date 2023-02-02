package com.t.medicaldocument.utils;


import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

import java.util.Objects;

/**
 * 对文件进行相关操作处理
 */
public class FileUtils {

	static public void savePDF(MultipartFile file,String filename) throws IOException {
		String end = System.getProperty("user.dir")+File.separator+"pdf"+File.separator+filename;
		File destFile = new File(end);
		destFile.getParentFile().mkdirs();
		//将文件保存
		file.transferTo(destFile);
	}

	static public int dividePDF(String filename) throws IOException {
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

	void downloadPDF(){

	}
	static public String ImageToBase64(InputStream imgPath) {
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
