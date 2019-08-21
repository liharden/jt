package com.jt;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
//springboot项目启动时去除数据源启动
@EnableHystrix
@SpringBootApplication(exclude=DataSourceAutoConfiguration.class)
@MapperScan("com.jt.mapper")
public class SpringBootRun {
	
	public static void main(String[] args) {
		
		SpringApplication.run(SpringBootRun.class,args);
	}
}
