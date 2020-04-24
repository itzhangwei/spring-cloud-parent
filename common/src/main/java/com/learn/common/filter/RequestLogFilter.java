package com.learn.common.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.learn.common.config.RabbitMqConfig;
import com.learn.common.db.dao.tools.RequestLogDao;
import com.learn.common.entity.BodyCachingHttpServletResponseWrapper;
import com.learn.common.entity.db.tools.RequestLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;

/**
 * @author zhang
 * @projectName spring-cloud-parent
 * @title RequestLogFilter
 * @package com.learn.common.filter
 * @description 拦截请求参数
 * @date 2020/4/16 3:41 下午
 */
@Slf4j(topic = "♦️♦️♦️♦️♦️♦️♦️♦️请求日志拦截类【RequestLogFilter】♦️♦️♦️♦️♦️♦️♦️♦️")
@Component
public class RequestLogFilter implements Filter {
	
	private final RequestLogDao requestLogDao;
	private final RabbitTemplate rabbitTemplate;
	@Value("${spring.application.name:unknown}")
	private  String  applicationName;
	
	public RequestLogFilter(RequestLogDao requestLogDao, RabbitTemplate rabbitTemplate) {
		this.requestLogDao = requestLogDao;
		this.rabbitTemplate = rabbitTemplate;
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
		final long start = System.currentTimeMillis();
		
		// 获取包装类
		final HttpServletRequestWrapper requestWrapper = new HttpServletRequestWrapper((HttpServletRequest) servletRequest);
		
		final BodyCachingHttpServletResponseWrapper responseWrapper = new BodyCachingHttpServletResponseWrapper((HttpServletResponse) servletResponse);
		
		try {
			// 放行
			filterChain.doFilter(requestWrapper, responseWrapper);
		} finally {
			//做成异步
			if (!requestWrapper.getRequestURI().contains("favicon.ico")) {
				final RequestLog requestLog = this.getRequestLog(start, requestWrapper, responseWrapper);
				rabbitTemplate.convertAndSend(RabbitMqConfig.REQUEST_LOG_QUEUE,requestLog);
			}
			
		}
	}
	
	/**
	 * 获取请求日志类 <BR>
	 * @param start 起始时间
	 * @param requestWrapper  请求包装类
	 * @param responseWrapper 返回包装类
	 * @return com.learn.common.entity.db.tools.RequestLog
	 * @throws IOException 异常
	 * @author zhangwei
	 * @createTime 2020/4/23 10:22 上午
	 */
	private RequestLog getRequestLog(long start, HttpServletRequestWrapper requestWrapper, BodyCachingHttpServletResponseWrapper responseWrapper) throws IOException {
		final long endTime = System.currentTimeMillis();
		final RequestLog requestLog = new RequestLog();
		requestLog.applicationName = applicationName;
		requestLog.contentType=requestWrapper.getContentType();
		requestLog.duration = endTime - start;
		requestLog.startTime = start;
		requestLog.endTime = endTime;
		requestLog.remoteHost = requestWrapper.getRemoteHost();
		requestLog.remotePort = requestWrapper.getRemotePort();
		requestLog.protocol = requestWrapper.getProtocol();
		requestLog.portNumber = requestWrapper.getLocalPort();
		requestLog.status = responseWrapper.getStatus();
		requestLog.method = requestWrapper.getMethod();
		requestLog.userAgent = requestWrapper.getHeader("user-agent");
		requestLog.reqId = requestWrapper.getHeader("reqId");
		requestLog.queryString = requestWrapper.getQueryString();
		requestLog.restUri=requestWrapper.getRequestURI();
		requestLog.restUrl=requestWrapper.getRequestURL().toString();
		requestLog.requestBody = this.getRequestBody(requestWrapper);
		requestLog.header = this.getHeader(requestWrapper);
		requestLog.responseBody=responseWrapper.getBodyString();
		log.info("请求参数：{}", JSON.toJSONString(requestLog));
		return requestLog;
	}
	
	/**
	 * 获取请求头
	 * @param requestWrapper 请求包装类
	 * @return json字符串
	 */
	private String getHeader(HttpServletRequestWrapper requestWrapper) {
		JSONObject headerJson = new JSONObject();
		final Enumeration<String> headerNames = requestWrapper.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			final String name = headerNames.nextElement();
			final String header = requestWrapper.getHeader(name);
			headerJson.put(name, header);
		}
		return headerJson.toJSONString();
	}
	
	/**
	 * servlet销毁方法
	 */
	@Override
	public void destroy() {
	}
	
	/**
	 * 获取请求体
	 * @param requestWrapper 请求参数
	 * @return 请求体string
	 * @throws IOException
	 */
	public String getRequestBody(HttpServletRequestWrapper requestWrapper) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(requestWrapper.getInputStream()));
		String line;
		StringBuilder inputBuffer = new StringBuilder();
		do {
			line = reader.readLine();
			if (null != line) {
				inputBuffer.append(line.trim());
			}
		} while (line != null);
		reader.close();
		return inputBuffer.toString().trim();
	}
	
}