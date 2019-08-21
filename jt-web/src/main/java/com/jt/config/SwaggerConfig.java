package com.jt.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by liweihao on 2019/08/12.
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

//    @Value("${swagger.switch}")
    private boolean swaggerSwitch = true;

    @Bean
    public Docket api() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2);
        docket.enable(swaggerSwitch);
        docket.apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.jt.controller"))
                .paths(PathSelectors.any()).build();
        return docket;
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Spring boot flash sale")
                .description("秒杀")
                .contact(new Contact("liweihao","https://github.com/liharden","2628065231@qq.com"))
                .termsOfServiceUrl("https://github.com/liharden")
                .version("0.6.4")
                .build();
    }
}
