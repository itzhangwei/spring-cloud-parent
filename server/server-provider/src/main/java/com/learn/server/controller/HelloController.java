package com.learn.server.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhang
 * @projectName spring-cloud-parent
 * @title HelloController
 * @package com.learn.server.controller
 * @description hello测试
 * @date 2019/10/11 4:20 下午
 */
@RestController
@RefreshScope
public class HelloController {
	@Value("${properties.name}")
	private String propertiesName;
	
	@GetMapping("hello")
	public String hello(){
		return "hello, properties name is :" + propertiesName;
	}
}
