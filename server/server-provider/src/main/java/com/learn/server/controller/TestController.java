package com.learn.server.controller;

import com.learn.server.config.properties.MySelfProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zack.zhang
 * @projectName spring-cloud-parent
 * @title TestController
 * @package com.learn.server.controller
 * @description
 * @date 2019/10/17 4:21 下午
 */
@RestController
public class TestController {
	
	@Autowired
	public MySelfProperties mySelfProperties;
	
	@GetMapping
	public String test() {

		return "title:"+this.mySelfProperties.getTitle()+"\n well-known Saying:"+this.mySelfProperties.getArticle().getWellKnownSaying();
	}
}
