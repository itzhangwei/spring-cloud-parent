package com.learn.server.controller;

import com.learn.server.service.LogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

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
@Slf4j
public class HelloController {
	@Value("${properties.name}")
	private String propertiesName;
	
	private final LogService logService;
	
	public HelloController(LogService logService) {
		this.logService = logService;
	}
	
	@GetMapping("hello")
	public String hello() throws InterruptedException {
		return "hello, properties name is :" + propertiesName + "我是灰度服务，哈哈哈哈哈";
	}
	
	
	@GetMapping("schedule")
	public Date scheduleTest() throws InterruptedException {
		log.info("》》》》》schedule");
		logService.infoLog();
		log.info("》》》》schedule-end");
		return new Date();
	}
}
