package com.t.medicaldocument.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.HashMap;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class MException extends Exception{
	HashMap<String,Object> desc ;
	Integer code;
	public MException put(String key,Object value){
		if (desc==null)
			desc=new HashMap<String,Object>();
		desc.put(key, value);
		return this;
	}
}
