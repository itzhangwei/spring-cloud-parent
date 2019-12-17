package com.learn.common.controller;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhang
 * @projectName spring-cloud-parent
 * @title EurekaController
 * @package com.learn.common.controller
 * @description 获取eureka注册中心服务信息
 * @date 2019/10/18 5:03 下午
 *
 * 通过请求获取eureka 的注册信息，这里只获取输出服务的版本号。
 * 也可在注册中心上通过get请求获取注册信息查看：
 *  1. /eureka/apps 查看所有注册服务
 *  2。 /eureka/apps/${appName} 查看单个注册信息
 */
@Slf4j
@RestController
public class EurekaController {
	
	private final DiscoveryClient discoveryClient;
	
	public EurekaController(DiscoveryClient discoveryClient) {
		this.discoveryClient = discoveryClient;
	}
	
	@GetMapping("getEurekaService")
	public HashMap<String, List<Map<String, String>>> getEurekaService() {
		
		// 收集相同的服务名称的不同信息
		HashMap<String, List<Map<String, String>>> instancesMap = new HashMap<>(16);
		
		
		
		// 获取到所有注册服务的instancesId（实例ID）集合, 这里获得是的appname集合
		List<String> services = discoveryClient.getServices();
		log.debug("通过DiscoveryClient获取所有注册服务的实例ID集合：{}", JSON.toJSONString(services));
		
		
		// 通过实例ID，获取服务
		services.parallelStream().forEachOrdered(s -> {
			
			
			//收集不同服务实例
			List<Map<String, String>> instancesList =new ArrayList<>();
			
			// 通过appName获取服务集合，每一个不同名称下面可能会有高可用的集群，灰度发布的同一个服务的不同版本号的情况
			List<ServiceInstance> instances = discoveryClient.getInstances(s);
			
			log.debug("通过微服务实例ID获取到的集合数据:{}", JSON.toJSONString(instances));
			
			instances.forEach(i->{
				// 收集服务的 端口，ip，版本号
				Map<String, String> instanceInfo = new HashMap<>(16);
				
				log.debug("单个微服务实例信息：{}", JSON.toJSONString(i));
				
				// ip地址
				instanceInfo.put("host",i.getHost());
				
				// 端口
				instanceInfo.put("port",i.getPort()+"");
				
				// 我们是吧每一个服务的版本号注册在这个 metadata 数据里面的
				Map<String, String> metadata = i.getMetadata();
				
				String version = metadata.get("myself.eureka.version");
				
				instanceInfo.put("version",version);
				
				log.debug("version:{}",version);
				
				instancesList.add(instanceInfo);
			});
			
			instancesMap.put(s, instancesList);
		});
		return instancesMap;
	}
}
