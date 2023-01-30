package com.t.medicaldocument.utils;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.t.medicaldocument.config.MException;
import org.springframework.util.ObjectUtils;


/**
 * 对文件进行相关操作处理
 */
public class FileUtils {
	/**
	 * 对Pdf版面分析 返回数据进行处理
	 * @param result
	 */
	void PdfStructure(JSONArray result) throws Exception {

		if(ObjectUtils.isEmpty(result) ||result.size()<=0)
			throw new MException("ocr识别返回出现阻碍,请联系工作人员",801);
		//对当前pdf文档页的汉字ocr描述 进行重塑
		for (Object o : result) {
			JSONObject json = (JSONObject) o;
			String type=(String)json.get("type");
			if("text".equals(type)) {
				//用数据结构存储,方便后继 根据阅读顺序进行重排

				JSONArray bbox = (JSONArray) json.get("bbox");

				JSONArray res=(JSONArray)json.get("res");
				//处理文档分析后的某一段的详细字段
				StringBuilder text = Textfield(res);
			}
			else if ("table".equals(type)) {
				;

			}
			else if ("table_caption".equals(type)) {
				;
			}
			else if ("figure".equals(type)) {
				;
			}
			else {
				;
			}
		}

		//Todo: 文档中的文字顺序的逻辑判定
		// 文字阅读顺序算法

	}

	/**
	 * 对 ocr识别 返回一般类型的 数据字段进行处理
	 * @param res
	 */
	StringBuilder Textfield(JSONArray res){
		//长文本
		StringBuilder text=new StringBuilder();
		for (Object o : res) {
			JSONObject json = (JSONObject) o;
			text.append((json.get("text")));
			(json).get("confidence");
		//	整合之后,对每一句进行数据结构存储

		}
		return text;
	}

}
