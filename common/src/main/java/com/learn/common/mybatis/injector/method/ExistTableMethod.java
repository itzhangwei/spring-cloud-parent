package com.learn.common.mybatis.injector.method;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

/**
 * @author zhang
 * @projectName spring-cloud-parent
 * @title ExistTableMethod
 * @package com.learn.common.mybatis.injector
 * @description 验证方法是否存在
 * @date 2020/5/19 10:10 上午
 */
public class ExistTableMethod extends AbstractMethod {
	
	@Override
	public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
		final String sql = "SELECT COUNT(1) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME='%s'";
		final String sqlResult = String.format(sql, tableInfo.getTableName());
		final SqlSource sqlSource = languageDriver.createSqlSource(configuration, sqlResult, modelClass);
		return this.addSelectMappedStatementForOther(mapperClass, "existTable", sqlSource,Integer.class);
	}
}
