package com.t.logic.utils;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
public class R<T> implements Serializable {
	private int code;
	private String mes;
	private T data;
	public static R ok(){
		return new R().setCode(200);
	}
	public static <T> R<T> ok(T data){
		return R.ok().setData(data).setMes("成功");
	}
	public static R fail(){
		return new R().setCode(201);
	}
	public static <T> R<T> fail(T data){
		return R.fail().setData(data);
	}
}
