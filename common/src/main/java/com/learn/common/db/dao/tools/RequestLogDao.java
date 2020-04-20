package com.learn.common.db.dao.tools;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.learn.common.entity.db.tools.RequestLog;
import com.learn.common.share.table.ShareTableRule;
import com.learn.common.share.table.ShareTableTarget;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import static com.learn.common.share.table.StrategyParam.*;




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
@ShareTableTarget(rules = {@ShareTableRule(sourceTableName = "request_log", targetTableName = "request_log", strategyParamClass = YearAndMouth.class)} , getDataSource = "tools")
public interface  RequestLogDao extends BaseMapper<RequestLog> {
	
	/**
	 * count大于0就存在 <BR>
	 * @param tableName 表名称
	 * @return int
	 * @author zhangwei
	 * @createTime 2020/4/16 11:57 上午
	 */
	@Select("SELECT COUNT(1) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME='request_log'")
	int existTable(@Param("tableName") String tableName);
	
	/**
	 * 创建表 <BR>
	 * @param tableName 表名称
	 * @return int
	 * @author zhangwei
	 * @createTime 2020/4/16 2:37 下午
	 */
	int createTable(@Param("tableName") String tableName);
}
