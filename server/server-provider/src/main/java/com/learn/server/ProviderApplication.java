package com.learn.server;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;

/**
 * @author zack.zhang
 * @projectName spring-cloud-parent
 * @title ProviderApplication
 * @package com.learn.server
 * @description
 * @date 2019/9/4 11:21 上午
 */
@SpringCloudApplication
public class ProviderApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(ProviderApplication.class, args);
	}
}
