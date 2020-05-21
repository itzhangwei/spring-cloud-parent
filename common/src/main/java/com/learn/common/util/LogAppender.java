package com.learn.common.util;

import brave.Tracer;
import brave.propagation.TraceContext;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import ch.qos.logback.core.encoder.Encoder;
import com.alibaba.fastjson.JSON;
import com.learn.common.db.dao.tools.Slf4jDao;
import com.learn.common.entity.LogEntity;
import com.learn.common.mongodb.dao.LogRepository;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.scheduling.annotation.Async;

import java.lang.reflect.Field;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

/**
 * @author zhang
 * @projectName spring-cloud-parent
 * @title LogAppender
 * @package com.learn.common.util
 * @description 日志输出
 * @date 2019/11/12 7:03 下午
 * ApplicationRunner 或者 CommandLineRunner 方法在spring加载完成后执行,这里不受spring 管理，这两个方法不行
 *
 * ApplicationContextAware 在类加载的时候给你一个spring 上下文对象 applicationContext
 */
@EqualsAndHashCode(callSuper = true)
@Slf4j
@Data
public class LogAppender<E> extends UnsynchronizedAppenderBase<E> {
	
	private  LogRepository logRepository;
	private Slf4jDao slf4jDao;
	
	protected Encoder<E> encoder;
	@Override
	protected void append(E eventObject) {
		try {
			// log日志加载要早于项目 spring 类加载，导致项目没有加载完成,无法拿到mybatis的动态代理类。
			
			if (this.slf4jDao == null) {
				return;
			}
			
			byte[] encode = this.encoder.encode(eventObject);
			// 转换字符串，json
			String s = new String(encode);
			LogEntity logEntity = JSON.parseObject(s, LogEntity.class);
			if (logEntity == null || StringUtil.isBlank(logEntity.getTraceId())) {
				return;
			}
			
			CompletableFuture.supplyAsync(()->{this.doInsertDb(logEntity); return true;});
		
		} catch (Exception e) {
			// 当Spring关闭的时候，连接关闭，在入库会有相印的错误
			log.error("日志插入mongodb失败,{}", e.getMessage());
		}
		
	}
	
	void doInsertDb(LogEntity logEntity)  {
		try {
			// 日志入库
			if (this.slf4jDao.existTable() == 0) {
				this.slf4jDao.createTable();
			}
			this.slf4jDao.insert(logEntity);
			
		} catch (Exception e) {
			log.error("",e);
		}
	}
	
	@Override
	public void start() {
		if (this.slf4jDao == null && ApplicationContextUtil.getContext() != null
				&& ApplicationContextUtil.getContext().getBean(Slf4jDao.class) != null) {
			
			this.slf4jDao = ApplicationContextUtil.getContext().getBean(Slf4jDao.class);
		}
		
		// 判断时数据库是否存在
		if (this.slf4jDao != null && this.slf4jDao.existTable() == 0) {
			this.slf4jDao.createTable();
		}
		super.start();
	}
	
}
