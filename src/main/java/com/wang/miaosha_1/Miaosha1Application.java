package com.wang.miaosha_1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class Miaosha1Application extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(Miaosha1Application.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(Miaosha1Application.class);
	}
}
