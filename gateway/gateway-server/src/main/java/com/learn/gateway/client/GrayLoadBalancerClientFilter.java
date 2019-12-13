package com.learn.gateway.client;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.gateway.config.LoadBalancerProperties;
import org.springframework.cloud.gateway.filter.LoadBalancerClientFilter;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhang
 * @projectName spring-cloud-parent
 * @title GrayLoadBalancerClientFilter
 * @package com.learn.gateway.client
 * @description 灰度负载均衡
 * @date 2019/11/18 7:08 下午
 */
@Slf4j
@Component
public class GrayLoadBalancerClientFilter extends LoadBalancerClientFilter {
	
	@Autowired
	private DiscoveryClient discoveryClient;
	
	private static final String VERSION_KEY = "myself.eureka.version";
	
	public GrayLoadBalancerClientFilter(LoadBalancerClient loadBalancer, LoadBalancerProperties properties) {
		super(loadBalancer, properties);
	}
	
	/**
	 * 选择服务
	 * @param exchange
	 * @return
	 */
	@Override
	protected ServiceInstance choose(ServerWebExchange exchange) {
		log.info("服务分发操作");
		
		// 获得请求头
		HttpHeaders headers = exchange.getRequest().getHeaders();
		
		// 获取请求同的token
		String token = headers.getFirst("token");
		
		
		// 获取用户携带的版本号
		String version = headers.getFirst("version");
		
		if (StringUtils.isEmpty(version)) {
			super.choose(exchange);
		}
		
		log.info("从请求头中获取token：{},\n从请求头中获取到的 version 版本号：{}",token,version);
		
		// 获取用户获取的项目serviceId
		URI uri = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR);
		
		assert uri != null;
		String instancesId = uri.getHost();
		
		log.info("请求的服务的instancesId:{}",instancesId);
		
		// 获取服务的信息
		List<ServiceInstance> instances = discoveryClient.getInstances(instancesId);
		log.info("从eureka中获取的服务instances为:{}", instances);
		
		// 通过version查找到服务
		if (instances != null && instances.size()>0) {
			List<ServiceInstance> versionSer = instances.stream().filter(i -> {
				assert version != null;
				return version.equals(i.getMetadata().get(VERSION_KEY));
			}).collect(Collectors.toList());
			
			// 没有找到对应版本号的服务，
			if (versionSer.size() == 0) {
				super.choose(exchange);
			}
			
			// 找到对应版本好的服务。。。
			return  versionSer.get(0);
		}
		return super.choose(exchange);
	}
}
