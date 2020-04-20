package com.learn.common.share.table;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zhang
 * @projectName spring-cloud-parent
 * @title shareTableRule
 * @package com.learn.common.share.table
 * @description 分表规则
 * @date 2020/4/16 5:47 下午
 */
@Retention(RetentionPolicy.RUNTIME)//存活阶段，RUNTIME：运行期
@Target({ElementType.TYPE})//注解可用范围，类头
public @interface ShareTableRule {
	
	/**
	 * 原来表名称，需要拦截分的表名
	 * @return
	 */
	public String sourceTableName();
	
	/**
	 * 目标表名称，将 sourceTableName 换成 targetTableName
	 * @return
	 */
	public String targetTableName();
	
	
	/**
	 * 分表策略类
	 * @return
	 */
	public Class<? extends StrategyParam> strategyParamClass();
}
