package com.learn.common.share.table;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author zhang
 * @projectName spring-cloud-parent
 * @title StrategyImpl
 * @package com.learn.common.share.table
 * @description 分表策略实现类
 * @date 2020/4/16 6:28 下午
 */
@Service
@Slf4j(topic = "分表策略拼接表名类")
public class StrategyImpl implements Strategy {
	
	@Override
	public String getShareTableName(String tableName, StrategyParam strategyParam) {
		return tableName + strategyParam.tableSuffix();
	}
}
