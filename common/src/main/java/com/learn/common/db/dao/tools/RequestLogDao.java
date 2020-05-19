package com.learn.common.db.dao.tools;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.learn.common.entity.db.tools.RequestLog;
import com.learn.common.mybatis.injector.MyBaseMapper;
import com.learn.common.share.table.ShareTableRule;
import com.learn.common.share.table.ShareTableTarget;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import static com.learn.common.share.table.StrategyParam.YearAndMouth;




/**
 * @author zhang
 * @projectName spring-cloud-parent
 * @title RequestLogDao
 * @package com.learn.common.db.dao.tools
 * @description 请求日志dao
 * @date 2020/4/16 10:11 上午
 */
@DS("tools")
@Repository
@ShareTableTarget(rules = {@ShareTableRule(sourceTableName = "t_request_log", targetTableName = "t_request_log", strategyParamClass = YearAndMouth.class)} , getDataSource = "tools")
public interface  RequestLogDao extends MyBaseMapper<RequestLog> {
	
	
	/**
	 * 创建表 <BR>
	 * @param tableName 表名称
	 * @return int
	 * @author zhangwei
	 * @createTime 2020/4/16 2:37 下午
	 */
	int createTable(@Param("tableName") String tableName);
}
