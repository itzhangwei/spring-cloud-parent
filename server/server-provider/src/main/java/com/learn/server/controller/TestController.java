package com.learn.server.controller;

import com.alibaba.fastjson.JSON;
import com.learn.common.entity.LogEntity;
import com.learn.common.mongodb.dao.LogRepository;
import com.learn.server.config.properties.MySelfProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhang
 * @projectName spring-cloud-parent
 * @title TestController
 * @package com.learn.server.controller
 * @description
 * @date 2019/10/17 4:21 下午
 */
@RestController
public class TestController {
	
	@Autowired
	private MySelfProperties mySelfProperties;
	
	@Autowired
	private LogRepository logRepository;
	
	@GetMapping
	public String test() {
		insertLog();
		return "title:"+this.mySelfProperties.getTitle()+"\n well-known Saying:"+this.mySelfProperties.getArticle().getWellKnownSaying();
	}
	
	private void insertLog(){
		String json = "{\"@timestamp\":\"2019-11-14T10:26:48.966+08:00\",\"severity\":\"INFO\",\"service\":\"server-provider\",\"trace\":\"\",\"span\":\"\",\"exportable\":\"\",\"pid\":\"16298\",\"thread\":\"main\",\"class\":\"com.netflix.discovery.DiscoveryClient\",\"rest\":\"Completed shut down of DiscoveryClient\"}\n";
		LogEntity logEntity = JSON.parseObject(json, LogEntity.class);
		this.logRepository.insert(logEntity);
	}
}
