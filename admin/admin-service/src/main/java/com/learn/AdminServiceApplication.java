package com.learn;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author zhang
 * @projectName spring-cloud-parent
 * @title AdminServiceApplication
 * @package com.learn
 * @description
 * @date 2020/8/4 6:18 下午
 */
@SpringBootApplication
@EnableAdminServer
@EnableDiscoveryClient
public class AdminServiceApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(AdminServiceApplication.class, args);
	}
}
