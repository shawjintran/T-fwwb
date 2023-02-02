package com.t.medicaldocument.config;

import com.t.medicaldocument.utils.R;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(Exception.class)
	public R error(Exception e){
		System.out.println("global exception");
		e.printStackTrace();
		return R.fail(e);
	}

}
