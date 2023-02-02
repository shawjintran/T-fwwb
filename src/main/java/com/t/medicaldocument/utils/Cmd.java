package com.t.medicaldocument.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.File;

@Slf4j
public class Cmd {
	// python  predict_system.py
	// --det_model_dir=../inference/ch_PP-OCRv3_det_infer
	// --rec_model_dir=../inference/ch_PP-OCRv3_rec_infer
	// --table_model_dir=../inference/ch_ppstructure_mobile_v2.0_SLANet_infer
	// --layout_model_dir=../inference/picodet_lcnet_x1_0_fgd_layout_cdla_infer
	// --image_dir=../material
	// --rec_char_dict_path=../ppocr/utils/ppocr_keys_v1.txt
	// --layout_dict_path=../ppocr/utils/dict/layout_dict/layout_cdla_dict.txt
	// --table_char_dict_path=../ppocr/utils/dict/table_structure_dict_ch.txt
	// --output=../Ares
	// --vis_font_path=../doc/fonts/simfang.ttf
	StringBuilder call=new StringBuilder();
	String py_file="D:\\CodeOfPython\\PaddleOcr\\PaddleOCR\\ppstructure\\predict_system.py";
	String det_model="D:\\CodeOfPython\\PaddleOcr\\PaddleOCR\\inference\\ch_PP-OCRv3_det_infer\\";
	String rec_model="D:\\CodeOfPython\\PaddleOcr\\PaddleOCR\\inference\\ch_PP-OCRv3_rec_infer\\";
	String table_model="D:\\CodeOfPython\\PaddleOcr\\PaddleOCR\\inference\\ch_ppstructure_mobile_v2.0_SLANet_infer\\";
	String layout_model="D:\\CodeOfPython\\PaddleOcr\\PaddleOCR\\inference\\picodet_lcnet_x1_0_fgd_layout_cdla_infer\\";
	String rec_dict="D:\\CodeOfPython\\PaddleOcr\\PaddleOCR\\ppocr\\utils\\ppocr_keys_v1.txt";
	String layout_dict="D:\\CodeOfPython\\PaddleOcr\\PaddleOCR\\ppocr\\utils\\dict\\layout_dict\\layout_cdla_dict.txt";
	String table_dict="D:\\CodeOfPython\\PaddleOcr\\PaddleOCR\\ppocr\\utils\\dict\\table_structure_dict_ch.txt";
	String image_dir="D:\\CodeOfJava\\Medical-Document\\pic\\";
	String output="D:\\CodeOfJava\\Medical-Document\\res\\";
	static Cmd cmd=new Cmd();
	private Cmd(){
		call.append("python ");
		call.append(py_file+" ");
		call.append("--det_model_dir="+det_model+" ");
		call.append("--rec_model_dir="+rec_model+" ");
		call.append("--table_model_dir="+table_model+" ");
		call.append("--layout_model_dir="+layout_model+" ");
		call.append("--rec_char_dict_path="+rec_dict+" ");
		call.append("--layout_dict_path="+layout_dict+" ");
		call.append("--table_char_dict_path="+table_dict+" ");
		// call.append("--output="+output+" ");
		call.append("--vis_font_path="+"D:\\CodeOfPython\\PaddleOcr\\PaddleOCR\\doc\\fonts\\simfang.ttf");
	}

	public String toString(String image,int i) {
		// System.out.println(call.toString()+" --image_dir="+image);
		File file = new File(output + image);
		if(!file.exists())
			file.mkdirs();
		// log.info(call.toString()+" --output="+output+image+" "
		// 		+"--image_dir="+image_dir+image+File.separator+ i + ".jpg");
		return call.toString()+" --output="+output+image+" "
				+" --image_dir="+image_dir+image+File.separator+ i + ".jpg";
	}
	public static Cmd create(){
		return cmd;
	}
}
