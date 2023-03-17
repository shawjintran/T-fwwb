package com.t.medicaldocument.config;


import com.t.medicaldocument.utils.FileUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		//把本地静态资源映射到项目
		try {
			registry.addResourceHandler("/file/**").addResourceLocations("file:"+ FileUtils.pdf_location);
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
}
