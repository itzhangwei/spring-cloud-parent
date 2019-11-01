package com.learn.common.config;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.instrument.async.LazyTraceExecutor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * @author zhang
 * @projectName spring-cloud-parent
 * @title ThreadConfig
 * @package com.learn.common.config
 * @description 异步线程池配置
 * @date 2019/11/1 11:36 上午
 */
@EnableAsync
@Configuration
public class ThreadAsyncConfig extends AsyncConfigurerSupport {
	
	@Autowired
	private BeanFactory beanFactory;
	
	@Override
	public Executor getAsyncExecutor() {
		//创建线程池
		ThreadPoolTaskExecutor pool = new ThreadPoolTaskExecutor();
		// 最少数量
		pool.setCorePoolSize(10);
		// 最大数量
		pool.setMaxPoolSize(50);
		//线程池所使用的缓冲队列
		pool.setQueueCapacity(11);
		//线程池维护线程所允许的空闲时间
		pool.setKeepAliveSeconds(60);
		
		//设置线程名字前缀
		pool.setThreadNamePrefix("sleuth-thread-");
		
		pool.initialize();
		
		// sleuth 中延时跟踪执行
		return new LazyTraceExecutor(beanFactory,pool);
	}
}
