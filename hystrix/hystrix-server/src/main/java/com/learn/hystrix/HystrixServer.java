package com.learn.hystrix;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.netflix.turbine.EnableTurbine;

/**
 * @author zhang
 * @projectName spring-cloud-parent
 * @title HystrixServer
 * @package com.learn.hystrix
 * @description 熔断器
 * @date 2019/10/22 4:15 下午
 */
@EnableHystrixDashboard
@EnableTurbine
@SpringCloudApplication
public class HystrixServer {
	
	public static void main(String[] args) {
		SpringApplication.run(HystrixServer.class, args);
	}
}
