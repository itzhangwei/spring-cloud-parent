package com.learn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @author zhang
 * @projectName spring-cloud-parent
 * @title EurekaServiceApplication
 * @package com.learn
 * @description 启动类
 * @date 2019/8/29 9:45 上午
 */
@SpringBootApplication
@EnableEurekaServer
public class EurekaServiceApplication {
	
	public static void main(String[] args) {
		
		SpringApplication.run(EurekaServiceApplication.class, args);
	}
}
