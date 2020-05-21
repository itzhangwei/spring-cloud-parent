package com.learn.common.filter;

import brave.Tracer;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.learn.common.config.RabbitMqConfig;
import com.learn.common.entity.BodyCachingHttpServletResponseWrapper;
import com.learn.common.entity.BufferedRequestWrapper;
import com.learn.common.entity.db.tools.RequestLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Enumeration;

/**
 * @author zhang
 * @projectName spring-cloud-parent
 * @title RequestLogFilter
 * @package com.learn.common.filter
 * @description 拦截请求参数
 * @date 2020/4/16 3:41 下午
 */
@Slf4j(topic = "♦️♦️♦️♦️️请求日志拦截类【RequestLogFilter】♦️♦️♦️♦️")
@Component
public class RequestLogFilter implements Filter {
	
	private final RabbitTemplate rabbitTemplate;
	private final Tracer tracer;
	@Value("${spring.application.name:unknown}")
	private  String  applicationName;
	
	public RequestLogFilter(RabbitTemplate rabbitTemplate, Tracer tracer) {
		this.rabbitTemplate = rabbitTemplate;
		this.tracer = tracer;
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
		final BufferedRequestWrapper requestWrapper = new BufferedRequestWrapper((HttpServletRequest) servletRequest);

		final BodyCachingHttpServletResponseWrapper responseWrapper = new BodyCachingHttpServletResponseWrapper((HttpServletResponse) servletResponse);
		try {
			// 放行
			filterChain.doFilter(requestWrapper, responseWrapper);
		} finally {
			//做成异步
			if (!requestWrapper.getRequestURI().contains("favicon.ico")) {
				this.async(start, requestWrapper, responseWrapper);
				
			}
			
		}
	}
	
	/**
	 * 异步方法，投递消息到mq <BR>
	 * @param start 请求开始时间
	 * @param requestWrapper 请求包装类
	 * @param responseWrapper 响应包装类
	 * @author zhangwei
	 * @createTime 2020/4/30 9:44 上午
	 */
	@Async
	void async(long start, BufferedRequestWrapper requestWrapper, BodyCachingHttpServletResponseWrapper responseWrapper) {
		try {
			this.sendRequestLogToMq(start, requestWrapper, responseWrapper);
			
		} catch (IOException e) {
			log.error("异步消息错误", e);
		}
	}
	
	/**
	 * 异步发送请求日志到mq中 <BR>
	 * @param start 开始时间
	 * @param requestWrapper 请求包装类
	 * @param responseWrapper 结果包装类
	 * @throws IOException 异常
	 * @author zhangwei
	 * @createTime 2020/4/26 11:28 上午
	 */
	private void sendRequestLogToMq(long start, BufferedRequestWrapper requestWrapper,
	                        BodyCachingHttpServletResponseWrapper responseWrapper) throws IOException {
		final RequestLog requestLog = this.getRequestLog(start, requestWrapper, responseWrapper);
		final Message message = MessageBuilder.withBody(JSON.toJSONString(requestLog).getBytes()).setContentType(MessageProperties.CONTENT_TYPE_JSON).build();
		rabbitTemplate.convertAndSend(RabbitMqConfig.REQUEST_LOG_QUEUE,message);
		log.info("日志投递完成！");
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
	private RequestLog getRequestLog(long start, BufferedRequestWrapper requestWrapper,
	                                 BodyCachingHttpServletResponseWrapper responseWrapper) throws IOException {
		final long endTime = System.currentTimeMillis();
		final RequestLog requestLog = new RequestLog();
		requestLog.applicationName = applicationName;
		requestLog.contentType=requestWrapper.getContentType();
		requestLog.duration = endTime - start;
		requestLog.startTime = new Date(start);
		requestLog.endTime = new Date(endTime);
		requestLog.remoteHost = requestWrapper.getRemoteHost();
		requestLog.remotePort = requestWrapper.getRemotePort();
		requestLog.protocol = requestWrapper.getProtocol();
		requestLog.portNumber = requestWrapper.getLocalPort();
		requestLog.status = responseWrapper.getStatus();
		requestLog.method = requestWrapper.getMethod();
		requestLog.userAgent = requestWrapper.getHeader("user-agent");
		requestLog.queryString = requestWrapper.getQueryString();
		requestLog.restUri=requestWrapper.getRequestURI();
		requestLog.restUrl=requestWrapper.getRequestURL().toString();
		requestLog.requestBody = requestWrapper.getRequestBody();
		requestLog.header = this.getHeader(requestWrapper);
		requestLog.responseBody=responseWrapper.getBodyString();
		requestLog.traceId = this.tracer.currentSpan() == null ? "" : this.tracer.currentSpan().context().traceId() + "";
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
