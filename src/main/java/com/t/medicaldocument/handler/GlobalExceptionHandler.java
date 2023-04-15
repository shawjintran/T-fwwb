package com.t.medicaldocument.handler;

import com.t.medicaldocument.config.MException;
import com.t.medicaldocument.utils.FileUtils;
import com.t.medicaldocument.utils.R;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(MException.class)
	public R custom(MException m){

		Integer code = m.getCode();
		if (code==null)
			return R.fail().setMes("异常错误");
		switch (code){
			case 302:{
				//	文件上传错误
				HashMap<String, Object> desc = m.getDesc();
				String filename = (String) desc.get("filename");
				FileUtils.deletePDF(filename);
				return R.fail().setMes("文件上传出现错误,请重新上传");
			}
			case 301:{
				//订单业务错误
				HashMap<String, Object> desc = m.getDesc();
				String descString = (String) desc.get("desc");
				return R.fail().setMes(descString);
			}
		}
		return R.fail().setMes("wrong");
	}
	@ExceptionHandler(Exception.class)
	public R error(Exception e){
		System.out.println("global exception");
		e.printStackTrace();
		return R.fail();
	}

}
