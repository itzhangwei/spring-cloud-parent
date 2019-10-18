package com.learn.server;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * @author zhang
 * @projectName spring-cloud-parent
 * @title ProviderApplication
 * @package com.learn.server
 * @description
 * @date 2019/9/4 11:21 上午
 *
 * RefreshScope 注解如果添加在启动类头上，可以实现自定义配置类刷新，使用@Value的需要在使用的地方添加注解
 */
@RefreshScope
@SpringCloudApplication
public class ProviderApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(ProviderApplication.class, args);
	}
}
