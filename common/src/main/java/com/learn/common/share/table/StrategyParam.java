package com.learn.common.share.table;

import java.time.LocalDate;

/**
 * @author zhang
 * @projectName spring-cloud-parent
 * @title StrategyParam
 * @package com.learn.common.share.table
 * @description 分表策略，获取表后缀名称
 * @date 2020/4/16 6:34 下午
 */
@FunctionalInterface
public interface StrategyParam {
	/**
	 * 获取表面后缀 <BR>
	 * @return java.lang.String 获取表面后缀
	 * @author zhangwei
	 * @createTime 2020/4/16 6:35 下午
	 */
	String tableSuffix();
	
	class YearAndMouth implements StrategyParam{
		
		@Override
		public String tableSuffix() {
			final int year = LocalDate.now().getYear();
			final int mouth = LocalDate.now().getMonthValue();
			final StringBuilder result = new StringBuilder("_");
			result.append(year).append("_");
			if (mouth > 9) {
				result.append("0");
			}
			result.append(mouth);
			return result.toString();
		}
	}
}
