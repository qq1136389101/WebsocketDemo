package com.chun;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class WebsocketApplication extends SpringBootServletInitializer{

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		// source启动类 告知一些静态资源
		builder.sources(WebsocketApplication.class);
		return builder;
	}

	public static void main(String[] args) {
		SpringApplication.run(WebsocketApplication.class, args);
	}

}
