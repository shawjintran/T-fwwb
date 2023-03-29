package com.t.medicaldocument.utils;


import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.t.medicaldocument.entity.Bo.EsDocumentBo;
import com.t.medicaldocument.entity.EsNestedChild;
import jdk.nashorn.internal.scripts.JS;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class PdfDataUtils {
	/**
	 * PDF文件呈双列排布
	 * @param map
	 * @return
	 */
	private StringBuilder DoubleTextSort(HashMap<int[], StringBuilder> map){

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
		//再按y进行排序
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
	 HashMap<String, ArrayList> PdfStructure(JSONObject res)  {
		if(res==null)
			return null;
		JSONArray result = new JSONArray((List) res.get("regions"));
		//对当前pdf文档页的汉字ocr描述 进行重塑
		// if (ObjectUtils.isEmpty(result)) {
		// 	return null;
		// }
		HashMap<int[], StringBuilder> map = new HashMap<>();
		HashMap<String, ArrayList> pdf = new HashMap<>();

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
	 static public HashMap<String, ArrayList> PdfStructure2(String file_path)
			 throws IOException {

		byte[] bytes = Files.readAllBytes(Paths.get(file_path + File.separator + "res_0.txt"));
		// System.out.println(builder.toString());
		JSONArray result = JSONArray.parseArray(new String(bytes));
		HashMap<String, ArrayList> pdf = new HashMap<>();

		for (Object o : result) {
			JSONObject json =  new JSONObject((JSONObject)o);
			String type=(String)json.get("type");
			if ("table".equals(type)) {
				StringBuilder table = Tablefield(new JSONObject( (JSONObject)json.get("res")) );
				put(pdf,"table",table);
			}
			else {
				StringBuilder text = Textfield(new JSONArray((JSONArray) json.get("res")));
				put(pdf,type,text);
			}
		}

		return pdf;
	}
	/**
	 * 对 ocr识别 返回一般类型的 数据字段进行处理
	 * @param res
	 */
	static private StringBuilder Textfield(JSONArray res){
		if (res==null)
			return null;
		//长文本
		StringBuilder text=new StringBuilder();
		for (Object o : res) {
			JSONObject json = new JSONObject((JSONObject) o);
			text.append((json.get("text")));
			// (json).get("confidence");
			//	整合之后,对每一句进行数据结构存储(考虑)
		}
		return text;
	}
	/**
	 * 对 ocr识别 返回一般类型的 数据字段进行处理
	 * @param res
	 */
	static private StringBuilder Tablefield(JSONObject res){
		if (res==null)
			return null;
		StringBuilder table=new StringBuilder();
		String html = (String)res.get("html");
		html=html.replaceAll("<[^>]*>", ","); // 去掉所有HTML标签
		html = html.replaceAll("^,|,$", "");// 去掉开头或结尾的逗号
		table.append(html);
		return table;
	}

	/**
	 * 将多个相同类型值 放入map中同一个键的列表
	 * @param pdf
	 * @param key
	 * @param value
	 */
	static private void put(HashMap<String, ArrayList> pdf, String key, StringBuilder value){
		if (value==null)
			return;
		if(pdf.containsKey(key))
		{
			((ArrayList<String>) (pdf.get(key))).add(value.toString());
		}else
		{
			ArrayList<String> list = new ArrayList<>();
			list.add(value.toString());
			pdf.put(key,list);
		}
	}

	static public ArrayList<EsDocumentBo> parseList(ArrayList<HashMap<String,Object>> list,Long userId)
	{
		ArrayList<EsDocumentBo> esObjs = new ArrayList<>();
		for (HashMap<String, Object> map : list) {
			HashMap<String,ArrayList<String>> pdf =
					(HashMap<String,ArrayList<String>>) map.get("desc");
			//传入ES对象
			EsDocumentBo esObj = new EsDocumentBo();
			parseMapIntoEsNested(pdf, esObj);
			//设置 pdfId 和 page 页数
			esObj.setPdfId((Long) map.get("pdfId"));
			esObj.setPdfPage((Integer) map.get("page"));
			esObj.setUserId(userId);
			esObj.setDocId(0L);
			esObj.setCreatetime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")));
			esObjs.add(esObj);
		}
		return esObjs;
	}

	/**
	 * 初版解析字符串存储
	 * @param map
	 * @param esObject
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 */
	static void parseMapToEsObject(HashMap<String,ArrayList> map,Object esObject) throws NoSuchFieldException, IllegalAccessException {
		//通过Java 反射
		Class<?> cls = esObject.getClass();
		for (String s : map.keySet()) {
			Field field = cls.getDeclaredField(s);
			field.setAccessible(true);
			field.set(esObject, JSON.toJSONString(map.get(s)));
			//使用反射进行映射字段名
		}
	}

	/**
	 * 第二版解析字符串，存入类型为Nested类型
	 * @param map
	 * @param esObject
	 */
	static void parseMapIntoEsNested(HashMap<String,ArrayList<String>> map,EsDocumentBo esObject)
	{
		ArrayList<EsNestedChild> children = new ArrayList<>();
		ArrayList<ArrayList<String>> lists=new ArrayList<>();
		for (String s : map.keySet()) {
			ArrayList<String> arrayList = map.get(s);
			for (String o : arrayList) {
				if (!o.equals("")&&!o.equals(" "))
				{
					EsNestedChild child = new EsNestedChild();
					child.setEstype(s);
					child.setEsvalue(o);
					children.add(child);
				}
			}
			lists.add(arrayList);
		}
		esObject.setAll(lists.toString());
		if (children.size()!=0)
			esObject.setEsfathernested(children);
	}
}
