package com.learn.common.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author zhang
 * @projectName spring-cloud-parent
 * @title ApplicationContestUtil
 * @package com.learn.common.util
 * @description 获取applicationContext, 用于获取spring 中的 bean
 * @date 2019/11/18 9:16 上午
 * ApplicationContextAware 在spring加载完成执行 setApplicationContext，并且提供 ApplicationContext参数对象。
 */
@Component
public class ApplicationContexttUtil implements ApplicationContextAware {
	
	 private static ApplicationContext applicationContext;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		ApplicationContexttUtil.applicationContext = applicationContext;
	}
	
	public static ApplicationContext getContext(){
		return ApplicationContexttUtil.applicationContext;
	}
}
