package com.learn.common.filter;

import com.learn.common.db.dao.tools.RequestLogDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;

/**
 * @author zhang
 * @projectName spring-cloud-parent
 * @title RequestLogFilter
 * @package com.learn.common.filter
 * @description 拦截请求参数
 * @date 2020/4/16 3:41 下午
 */
@Slf4j
@Component
public class RequestLogFilter implements Filter {
	
	private final RequestLogDao requestLogDao;
	
	public RequestLogFilter(RequestLogDao requestLogDao) {
		this.requestLogDao = requestLogDao;
	}
	/**
	 * 初始化方法
	 * @param filterConfig
	 * @throws ServletException
	 */
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}
	
	/**
	 * 拦截方法
	 * @param servletRequest 请求
	 * @param servletResponse 响应
	 * @param filterChain 拦截
	 * @throws IOException io异常
	 * @throws ServletException 异常
	 */
	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		try {
			// 放行
			filterChain.doFilter(servletRequest, servletResponse);
		} finally {
			final int requestLog = this.requestLogDao.existTable("request_log");
			log.error("{}",requestLog);
			if (requestLog == 0) {
				log.info("创建表结构");
				this.requestLogDao.createTable("request_log");
			}
		}
	}
	
	/**
	 * servlet销毁方法
	 */
	@Override
	public void destroy() {
	}
}
