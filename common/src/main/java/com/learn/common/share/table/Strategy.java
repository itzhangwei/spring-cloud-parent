package com.learn.common.share.table;

/**
 * @author zhang
 * @projectName spring-cloud-parent
 * @title Strategy
 * @package com.learn.common.share.table
 * @description 策略抽象类
 * @date 2020/4/16 6:12 下午
 */
public interface Strategy {
	
	/**
	 * 获取分表表名称 <BR>
	 * @param tableName 分表的表名称
	 * @param strategyParam 分表参数
	 * @return java.lang.String 按照规则分表后的表名称
	 * @author zhangwei
	 * @createTime 2020/4/16 6:14 下午
	 */
	String getShareTableName(String tableName, StrategyParam strategyParam);
}
