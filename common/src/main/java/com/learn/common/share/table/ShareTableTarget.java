package com.learn.common.share.table;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zhang
 * @projectName spring-cloud-parent
 * @title ShareTableTarget
 * @package com.learn.common.share.table
 * @description 分表拦截策略
 * @date 2020/4/16 5:58 下午
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ShareTableTarget {
	//分表规则
	public ShareTableRule[] rules();
	
	//分表规则
	public String getDataSource();
}
