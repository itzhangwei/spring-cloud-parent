package com.learn.common.share.table;

import com.alibaba.druid.proxy.jdbc.ConnectionProxyImpl;
import com.gymbomate.base.util.StringUtil;
import io.seata.rm.datasource.ConnectionProxy;
import io.seata.rm.datasource.DataSourceProxy;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.logging.jdbc.ConnectionLogger;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.springframework.stereotype.Component;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.util.LinkedHashMap;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicReference;
/**
 * @author zhang
 * @projectName spring-cloud-parent
 * @title ShareTableInterceptor
 * @package com.learn.common.share.table
 * @description 分表sql拦截器
 * @date 2020/4/16 6:48 下午
 */
@Slf4j(topic = "分表策略拦截器【ShareTableInterceptor】")
@Component
@Intercepts({ @Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class,Integer.class }) })
public class ShareTableInterceptor implements Interceptor {
	
	final Strategy strategy;
	
	public ShareTableInterceptor(Strategy strategy){
		
		this.strategy = strategy;
	}
	/**
	 * 方法就是要进行拦截的时候要执行的方法
	 * @param invocation
	 * @return
	 * @throws Throwable
	 */
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		log.info("♠️♠️♠️♠️♠️♠️♠️♠️进入sql拦截器");
		this.doShareTableSql(invocation);
		return invocation.proceed();
	}
	
	/**
	 * 分表实现方法<BR>
	 * @param invocation
	 * @return
	 * @throws Exception 异常
	 * @author zhangwei
	 * @createTime 2020/4/17 6:17 下午
	 */
	private void doShareTableSql(Invocation invocation) throws Exception {
		//获取被代理的对象
		final RoutingStatementHandler target = (RoutingStatementHandler) invocation.getTarget();
		// 获取sql
		final String sql = target.getBoundSql().getSql();
		
		//获取连接数据源
		final DataSourceProxy directDataSource = getCurrentDataSource(invocation);
		
		//类似与一个map，获取属性只需要getValue，比操作反射一个层一层获取私有对象简单方便
		final MetaObject metaObject = MetaObject.forObject(target, SystemMetaObject.DEFAULT_OBJECT_FACTORY, SystemMetaObject.DEFAULT_OBJECT_WRAPPER_FACTORY, new DefaultReflectorFactory());
		
		//获取分表注解
		final ShareTableTarget shareTableTarget = getShareTableTargetAnnotation(metaObject);
		
		if (shareTableTarget == null) {
			//没加注解，部分表不拦截
			return;
		}
		// 获取分表指定数据源
		final String dataSource = shareTableTarget.getDataSource();
		
		//获取当前数据源的key名称
		AtomicReference<String> key = getDataSourceKey(directDataSource, metaObject);
		if (!dataSource.equals(key.get())) {
			//数据源不匹配，不需要分表
			return;
		}
		
		// 实现分表规则
		String newSql = this.doShareRules(sql, shareTableTarget);
		
		log.info("❤❤❤❤❤❤分表前sql语句为：{}",sql.replace("\n",""));
		log.info("☆☆☆☆☆分表后sql语句为：{}",newSql.replace("\n",""));
		
		//设置新的sql语句
		metaObject.setValue("delegate.boundSql.sql",newSql);
	}
	
	/**
	 * 执行分表规则，生产新的SQL语句 <BR>
	 * @param sql 原先的sql语句
	 * @param shareTableTarget 分表注解
	 * @return java.lang.String
	 * @throws Exception 异常
	 * @author zhangwei
	 * @createTime 2020/4/17 6:27 下午
	 */
	private String doShareRules(String sql, ShareTableTarget shareTableTarget) throws Exception {
		//获取分表规则
		final ShareTableRule[] rules = shareTableTarget.rules();
		String newSql = sql;
		for (ShareTableRule r:rules) {
			// 需要分表的表名称
			final String sourceTableName = r.sourceTableName();
			final String targetTableName = r.targetTableName();
			if (StringUtil.isBlank(sourceTableName) || StringUtil.isBlank(targetTableName)) {
				continue;
			}
			//获取表后缀的class，获取分表后的表名称
			final Class<? extends StrategyParam> strategyParamClass = r.strategyParamClass();
			final String shareTableName = this.strategy.getShareTableName(targetTableName, strategyParamClass.newInstance());
			
			//SQL表名称替换
			newSql = newSql.replaceAll(sourceTableName,shareTableName);
		}
		return newSql;
	}
	
	private AtomicReference<String> getDataSourceKey(DataSourceProxy dataSourceProxy, MetaObject metaObject) {
		//获取到多数据源配置
		final LinkedHashMap<String, DataSourceProxy> dataSourceMap = (LinkedHashMap<String, DataSourceProxy>) metaObject.getValue("delegate.configuration.environment.dataSource.dataSourceMap");
		// 拿到本次数据源的key
		AtomicReference<String> key = new AtomicReference<>("") ;
		dataSourceMap.forEach((k, v) ->{
			if (v == dataSourceProxy) {
				key.set(k);
			}
		});
		return key;
	}
	
	/**
	 * 获取mapper接口上的分表注解 <BR>
	 * @param metaObject
	 * @return com.learn.common.share.table.ShareTableTarget
	 * @author zhangwei
	 * @createTime 2020/4/17 6:26 下午
	 */
	private ShareTableTarget getShareTableTargetAnnotation(MetaObject metaObject) throws ClassNotFoundException {
		//获取 mappedStatement
		final MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
		//ID是类名.方法名称
		final String id = mappedStatement.getId();
		// 获取到类名
		final String className = id.substring(0, id.lastIndexOf("."));
		//获取到class对象
		final Class<?> mapperClass = Class.forName(className);
		//获取class头上的分表注解
		return mapperClass.getAnnotation(ShareTableTarget.class);
	}
	
	/**
	 * 获取当前连接的数据源对象 <BR>
	 * @param invocation
	 * @return com.alibaba.druid.proxy.jdbc.DataSourceProxy
	 * @author zhangwei
	 * @createTime 2020/4/17 6:25 下午
	 */
	private DataSourceProxy getCurrentDataSource(Invocation invocation) {
		//获取到本次操作的数据源
		final Proxy arg = (Proxy)invocation.getArgs()[0];
		//获取到连接
		final ConnectionLogger con = (ConnectionLogger) Proxy.getInvocationHandler(arg);
		final ConnectionProxy conConnection = (ConnectionProxy) con.getConnection();
		//获取数据源
		return conConnection.getDataSourceProxy();
	}
	
	/**
	 * 方法是插件用于封装目标对象的，通过该方法我们可以返回目标对象本身,
	 * 也可以返回一个它的代理，可以决定是否要进行拦截进而决定要返回一个什么样的目标对象,
	 * 官方提供了示例：return Plugin.wrap(target, this);
	 *
	 * @param target
	 * @return
	 */
	@Override
	public Object plugin(Object target) {
		return  Plugin.wrap(target, this);
	}
	
	/**
	 * setProperties 方法是在Mybatis进行配置插件的时候可以配置自定义相关属性，即：接口实现对象的参数配置
	 * @param properties 配置参数
	 */
	@Override
	public void setProperties(Properties properties) {
	}
}
