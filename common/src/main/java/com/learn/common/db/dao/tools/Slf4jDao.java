package com.learn.common.db.dao.tools;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.learn.common.entity.LogEntity;
import com.learn.common.mybatis.injector.MyBaseMapper;
import com.learn.common.share.table.ShareTableRule;
import com.learn.common.share.table.ShareTableTarget;
import com.learn.common.share.table.StrategyParam;
import org.springframework.stereotype.Repository;

/**
 * @author zhang
 * @projectName spring-cloud-parent
 * @title Slf4jDao
 * @package com.learn.common.db.dao.tools
 * @description 日志dao
 * @date 2020/5/19 9:39 上午
 */
@DS("tools")
@Repository
@ShareTableTarget(rules = {@ShareTableRule(sourceTableName = "t_slf4j", targetTableName = "t_slf4j", strategyParamClass = StrategyParam.YearAndMouth.class)} , getDataSource = "tools")
public interface Slf4jDao extends MyBaseMapper<LogEntity> {
	/**
	 * 创建表
	 * @return int
	 */
	int createTable();
	
}
