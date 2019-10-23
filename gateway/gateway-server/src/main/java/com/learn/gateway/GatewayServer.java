package com.learn.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;

/**
 * @author zhang
 * @projectName spring-cloud-parent
 * @title GatewayServer
 * @package com.learn.gateway
 * @description 网管服务启动类
 * @date 2019/10/23 3:11 下午
 */
@SpringCloudApplication
public class GatewayServer {
	
	public static void main(String[] args) {
		SpringApplication.run(GatewayServer.class, args);
	}
}
