package com.t.medicaldocument.utils;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.t.medicaldocument.config.MException;
import org.springframework.util.ObjectUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 对文件进行相关操作处理
 */
public class FileUtils {

	/**
	 * PDF文件呈双列排布
	 * @param map
	 * @return
	 */

	StringBuilder DoubleTextSort(HashMap<int[], StringBuilder> map){

		int side3=Integer.MIN_VALUE;
		HashMap<Integer, StringBuilder> left=new HashMap<>();
		HashMap<Integer, StringBuilder> right=new HashMap<>();

		for (int[] ints : map.keySet()) {
			if (side3<ints[0]&&Math.abs(side3-ints[0])>30)
				side3=ints[0];
		}
		for (int[] ints : map.keySet()) {
			if (ints[2]<=side3)
			{
			// 左边
				left.put(ints[1],map.get(ints));
			}else
			{
			//	右边
				right.put(ints[1],map.get(ints));
			}
		}
		//按y进行排序
		StringBuilder text = new StringBuilder();
		for (Integer integer : left.keySet()) {
			text.append(left.get(integer));
		}
		for (Integer integer :right.keySet()) {
			text.append(right.get(integer));
		}
		return text;
	}
	/**
	 * 对pdf版面分析 通过 hubserving服务 返回数据 需要进行基于阅读顺序的排序时
	 * @param res
	 */
	public HashMap<String, Object> PdfStructure(JSONObject res)  {
		if(res==null)
			return null;
		JSONArray result = new JSONArray((List) res.get("regions"));
		//对当前pdf文档页的汉字ocr描述 进行重塑
		// if (ObjectUtils.isEmpty(result)) {
		// 	return null;
		// }
		HashMap<int[], StringBuilder> map = new HashMap<>();
		HashMap<String, Object> pdf = new HashMap<>();

		for (Object o : result) {
			JSONObject json =  new JSONObject((HashMap)o);
			String type=(String)json.get("type");
			if("text".equals(type)) {
				//用数据结构存储,方便后继 根据阅读顺序进行重排

				JSONArray bbox = new JSONArray((List) json.get("bbox"));
				int[]ints = new int[bbox.size()];
				for (int i = 0; i < bbox.size(); i++) {
					ints[i]=(int)bbox.get(i);
				}
				//处理文档分析后的某一段的详细字段
				StringBuilder text = Textfield(new JSONArray((List) json.get("res")));
				map.put(ints,text);
			}
			else if ("table".equals(type)) {
				StringBuilder table = Tablefield(new JSONObject((Map<String, Object>) json.get("res")) );
				put(pdf,"table",table);
			}
			else {
				StringBuilder table = Textfield(new JSONArray((List) json.get("res")));
				put(pdf,type,table);
			}
		}
		//文档中的文字顺序的逻辑判定
		//文字阅读顺序算法
		StringBuilder textSort = DoubleTextSort(map);
		put(pdf,"text",textSort);
		return pdf;
	}

	/**
	 * 对pdf版面分析 通过python 返回数据 从TXT文件中读取
	 * @param file_path
	 * @return
	 */
	public HashMap<String, Object> PdfStructure2(String file_path) throws IOException {
		StringBuilder builder = new StringBuilder();
		BufferedInputStream buf = null;
		try {
			int len = 1024*5;
			byte[] bytes = new byte[len];
			buf = new BufferedInputStream(new FileInputStream(file_path+ File.separator+"res_0.txt"));
			while ((len = buf.read(bytes, 0, len)) != -1) {
				builder.append(bytes);
			}
		}finally {
			if (buf!=null)
				buf.close();
		}

		JSONArray result = JSONArray.parseArray(builder.toString());

		HashMap<String, Object> pdf = new HashMap<>();

		for (Object o : result) {
			JSONObject json =  new JSONObject((HashMap)o);
			String type=(String)json.get("type");
			 if ("table".equals(type)) {
				StringBuilder table = Tablefield(new JSONObject((Map<String, Object>) json.get("res")) );
				put(pdf,"table",table);
			}
			else {
				StringBuilder table = Textfield(new JSONArray((List) json.get("res")));
				put(pdf,type,table);
			}
		}

		return pdf;
	}
	/**
	 * 对 ocr识别 返回一般类型的 数据字段进行处理
	 * @param res
	 */
	StringBuilder Textfield(JSONArray res){
		if (res==null)
			return null;
		// if (ObjectUtils.isEmpty(res))
		// 	return null;
		//长文本
		StringBuilder text=new StringBuilder();
		for (Object o : res) {
			JSONObject json = new JSONObject((Map<String, Object>) o);
			text.append((json.get("text")));
			// (json).get("confidence");
		//	整合之后,对每一句进行数据结构存储
		}
		return text;
	}
	StringBuilder Tablefield(JSONObject res){
		if (res==null)
			return null;
		// if (ObjectUtils.isEmpty(res))
		// 	return null;
		StringBuilder table=new StringBuilder();
		table.append(res.get("html"));
		return table;
	}
	void put(HashMap<String, Object> pdf,String key,StringBuilder value){
		if (value==null)
			return;
		// if (ObjectUtils.isEmpty(value)) {
		// 	return;
		// }
	if(pdf.containsKey(key))
		{
			((ArrayList<StringBuilder>) (pdf.get(key))).add(value);
		}else
		{
			ArrayList<StringBuilder> list = new ArrayList<>();
			list.add(value);
			pdf.put(key,list);
		}
	}
}
