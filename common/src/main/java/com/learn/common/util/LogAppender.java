package com.learn.common.util;

import ch.qos.logback.core.OutputStreamAppender;
import ch.qos.logback.core.encoder.Encoder;
import ch.qos.logback.core.joran.spi.ConsoleTarget;
import com.alibaba.fastjson.JSON;
import com.learn.common.entity.LogEntity;
import com.learn.common.mongodb.dao.LogRepository;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author zhang
 * @projectName spring-cloud-parent
 * @title LogAppender
 * @package com.learn.common.util
 * @description 日志输出
 * @date 2019/11/12 7:03 下午
 * ApplicationRunner 或者 CommandLineRunner 方法在spring加载完成后执行
 *
 * ApplicationContextAware 在类加载的时候给你一个spring 上下文对象 applicationContext
 */
@EqualsAndHashCode(callSuper = true)
@Slf4j
@Data
@Component
public class LogAppender extends OutputStreamAppender implements ApplicationRunner, ApplicationContextAware {
	
	private  LogRepository logRepository;
	
	private ConsoleTarget target = ConsoleTarget.SystemOut;
	
	private  ApplicationContext applicationContext;
	
	@Override
	protected void append(Object eventObject) {
		try {
			
			// log日志加载要早于项目 spring 类加载，导致项目没有加载完成的日志无法输入到mongodb
			if (this.logRepository == null) {
				return;
			}
			
			Encoder encoder = this.getEncoder();
			// 日志编码
			byte[] encode = encoder.encode(eventObject);
			
			// 转换字符串，json
			String s = new String(encode);
			
			LogEntity logEntity = JSON.parseObject(s, LogEntity.class);
			
			logRepository.insert(logEntity);
			
		} catch (Exception e) {
			
			log.error("日志插入mongodb失败", e);
			
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void start() {
		setOutputStream(this.target.getStream());
		super.start();
	}
	
	/**
	 * spirng boot项目加载完成后执行方法，这里为了在spirng加载完成后，把 LogRepository 注入
	 * @param args
	 * @throws Exception
	 */
	@Override
	public void run(ApplicationArguments args) throws Exception {
		System.out.println("加载完成");
		if (this.applicationContext == null) {
			return;
		}
		
		this.logRepository = this.applicationContext.getBean(LogRepository.class);
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
