package com.t.logic.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig  {
	@Bean
	public Docket createRestApi() {
		//文档类型
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo())
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.t.logic.controller"))
				// .paths(PathSelectors.any())
				.build();
	}
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("文献")
				.version("1.0")
				.description("文献检索接口文档")
				.build();
	}
}
