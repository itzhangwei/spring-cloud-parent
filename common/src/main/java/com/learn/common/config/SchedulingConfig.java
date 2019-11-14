package com.learn.common.config;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;

/**
 * @author zhang
 * @projectName spring-cloud-parent
 * @title SchedingConfig
 * @package com.learn.common.config
 * @description 定时任务
 * @date 2019/11/1 5:09 下午
 */
@Configuration
@EnableAsync
@EnableScheduling
public class SchedulingConfig implements SchedulingConfigurer {
	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		taskRegistrar.setScheduler(schedulingExecutor());
	}
	
	@Bean
	public Executor schedulingExecutor() {
		// 生成一个线程池
		ThreadFactory build = new ThreadFactoryBuilder().setNameFormat("scheduled-pool-%d").build();
		/**
		 * 参数含义：
		 *      corePoolSize : 线程池中常驻的线程数量。核心线程数，默认情况下核心线程会一直存活，即使处于闲置状态也不会受存keepAliveTime限制。除非将allowCoreThreadTimeOut设置为true。
		 *      maximumPoolSize : 线程池所能容纳的最大线程数。超过这个数的线程将被阻塞。当任务队列为没有设置大小的LinkedBlockingDeque时，这个值无效。
		 *      keepAliveTime : 当线程数量多于 corePoolSize 时，空闲线程的存活时长，超过这个时间就会被回收
		 *      unit : keepAliveTime 的时间单位
		 *      workQueue : 存放待处理任务的队列，该队列只接收 Runnable 接口
		 *      threadFactory : 线程创建工厂
		 *      handler : 当线程池中的资源已经全部耗尽，添加新线程被拒绝时，会调用RejectedExecutionHandler的rejectedExecution方法，参考 ThreadPoolExecutor 类中的内部策略类
		 */
//		ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 200, 0L,
////				TimeUnit.MILLISECONDS,
////				new LinkedBlockingQueue<>(1024),
////				build,
////				new ThreadPoolExecutor.AbortPolicy());

//		Executors.newScheduledThreadPool()
		return new ScheduledThreadPoolExecutor(5, build);
	}
}
