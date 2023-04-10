package com.t.medicaldocument.config;


import com.t.medicaldocument.interceptor.OcrInterceptor;
import com.t.medicaldocument.utils.FileUtils;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		//把本地静态资源映射到项目
		try {
			registry.addResourceHandler("/file/**").addResourceLocations("file:"+FileUtils.pdf_location);
			// 也可以映射项目里的static路径
			/*
				classpath:/resources/
				classpath:/static/
				classpath:/public/
				classpath:/META-INF/resources/
		*/
		} catch (Exception e) {
			e.printStackTrace();
			System.out.print("错误映射:"+e);
		}
	}

	@Override
//	@ConditionalOnProperty(value = "spring.profiles.active",havingValue = "test")
	// TODO: 2023/3/31 Conditional注解失效
	public void addInterceptors(InterceptorRegistry registry) {

		registry.addInterceptor(new OcrInterceptor())
//				.addPathPatterns("/file/upload/**")
				.addPathPatterns("/file/analyze/structure/**")
				.addPathPatterns("/desc/**");

	}
}
