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
public class LogAppender extends OutputStreamAppender {
	
	private  LogRepository logRepository;
	
	private ConsoleTarget target = ConsoleTarget.SystemOut;
	
	@Override
	protected void append(Object eventObject) {
		try {
			
			// log日志加载要早于项目 spring 类加载，导致项目没有加载完成的日志无法输入到mongodb
			if (this.logRepository == null) {
				
				if (ApplicationContestUtil.getContext() != null) {
					
					// 当spring加载完成之后，ApplicationContestUtil.getContext()才会有值
					this.logRepository = ApplicationContestUtil.getContext().getBean(LogRepository.class);
				}
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
			// 当Spring关闭的时候，连接关闭，在入库会有相印的错误
			log.error("日志插入mongodb失败", e.getMessage());
		}
		
	}
	
	@Override
	public void start() {
		setOutputStream(this.target.getStream());
		super.start();
	}
	
}
