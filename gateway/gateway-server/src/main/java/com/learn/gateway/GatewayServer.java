package com.learn.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * @author zhang
 * @projectName spring-cloud-parent
 * @title GatewayServer
 * @package com.learn.gateway
 * @description 网管服务启动类
 * @date 2019/10/23 3:11 下午
 */
@SpringCloudApplication
@ComponentScan(basePackages = "com.learn.*")
@EnableMongoRepositories(basePackages = "com.learn.common.mongodb.dao")
public class GatewayServer {
	
	public static void main(String[] args) {
		SpringApplication.run(GatewayServer.class, args);
	}
}
