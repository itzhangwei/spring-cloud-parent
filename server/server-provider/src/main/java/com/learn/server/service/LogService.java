package com.learn.server.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author zhang
 * @projectName spring-cloud-parent
 * @title LogService
 * @package com.learn.common.async.service
 * @description 日志
 * @date 2019/11/1 4:38 下午
 */
@Slf4j
@Service
public class LogService {
	
	@Async
	public void infoLog() throws InterruptedException {
		log.info("{}-进行日志输出--开始", this.getClass().getName());
		TimeUnit.SECONDS.sleep(5);
		log.info("{}-进行日志输出--结束", this.getClass().getName());
		
	}
	
	@Scheduled(fixedDelay = 3000)
	public void scheduledWork() throws InterruptedException {
		log.info("{}=====计划任务进行日志输出--开始=====", this.getClass().getName());
		TimeUnit.SECONDS.sleep(5);
		log.info("{}=====计划任务进行日志输出--结束=====", this.getClass().getName());
	}
}
