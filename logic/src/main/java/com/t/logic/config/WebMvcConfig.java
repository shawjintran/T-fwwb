package com.t.logic.config;


import com.t.logic.interceptor.ServiceInterceptor;
import com.t.logic.utils.FileUtils;
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
			registry.addResourceHandler("/temp/**").addResourceLocations("file:"+FileUtils.temp_location);
			registry.addResourceHandler("/pic/**").addResourceLocations("file:"+FileUtils.pic_location);
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
	//  2023/3/31 Conditional注解失效  2023/11/24 @Condition 注解无法在此处生效
	public void addInterceptors(InterceptorRegistry registry) {

		registry.addInterceptor(new ServiceInterceptor())
//				.addPathPatterns("/file/upload/**")
//				.addPathPatterns("/file/analyze/structure/**")
				.addPathPatterns("/desc/**");

	}
}
