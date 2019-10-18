package com.learn.config;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * @author zhang
 * @projectName spring-cloud-parent
 * @title ConfigServiceApplication
 * @package com.learn.config
 * @description 配置中心服务启动类
 * @date 2019/8/29 7:14 下午
 */
@SpringCloudApplication
@EnableConfigServer
public class ConfigServiceApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(ConfigServiceApplication.class, args);
	}
}
