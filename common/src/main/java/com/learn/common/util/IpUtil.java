package com.learn.common.util;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * @author zhang
 * @projectName spring-cloud-parent
 * @title IpUtil
 * @package com.learn.common.util
 * @description 获取ip地址
 * @date 2019/12/16 2:19 下午
 */
public class IpUtil {
	
	/**
	 * get请求获取外网ip地址
	 */
	private static final String url = "http://www.ifconfig.io/ip";
	
	/**
	 * 获取本机器的外网ip地址 <BR>
	 *
	 * @return java.lang.String
	 * @author zhangwei
	 * @createTime 2019/12/16 2:39 下午
	 */
	public static String getLocalIp() {
		RestTemplate rest = new RestTemplate();
		
		//请求获取ip
		ResponseEntity<String> forEntity = rest.getForEntity(url, String.class);
		String ip = forEntity.getBody();
		ip = ip.replace("\n", "");
		return ip;
	}
	
}
