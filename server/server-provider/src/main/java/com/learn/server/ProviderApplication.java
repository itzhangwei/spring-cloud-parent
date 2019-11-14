package com.learn.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * @author zhang
 * @projectName spring-cloud-parent
 * @title ProviderApplication
 * @package com.learn.server
 * @description
 * @date 2019/9/4 11:21 上午
 * <p>
 * RefreshScope 注解如果添加在启动类头上，可以实现自定义配置类刷新，使用@Value的需要在使用的地方添加注解
 *
 * EnableMongoRepositories spring data 的repository扫描 ，使用springboot扫描时候需要指定全路径才能扫描到repository
 * 但是springboot 扫描 com.learn.* 会覆盖 com.learn.common.mongodb.dao，会导致一样扫描不到repository
 */
@RefreshScope
@SpringBootApplication(scanBasePackages = "com.learn.*")
@SpringCloudApplication
@EnableHystrixDashboard
@EnableMongoRepositories(basePackages = "com.learn.common.mongodb.dao")
public class ProviderApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(ProviderApplication.class, args);
	}
}
