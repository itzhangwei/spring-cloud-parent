package com.learn.common.config;

import com.learn.common.util.IpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.stereotype.Component;

/**
 * @author zhang
 * @projectName spring-cloud-parent
 * @title IpEureka
 * @package com.learn.common.config
 * @description 拦截eureka的 instanceId,把外网 ip写入
 * @date 2019/12/16 3:43 下午
 * EurekaInstanceConfigBean 还是优先加载，项目加载玩了才扫描到 Component，
 * 从而达到覆盖以一次加载的配置
 */
//@Component
//@Primary
@Slf4j
//@ConfigurationProperties("eureka.instance")
public class IpEureka extends EurekaInstanceConfigBean {
	
	private final ConfigurableEnvironment env;
	
	/**
	 * 是否显示外网ip
	 */
	private boolean extranetIpShow = false;
	
	public IpEureka(InetUtils inetUtils, ConfigurableEnvironment env ) {
		super(inetUtils);
		this.env = env;
	}
	
	@Override
	public void setInstanceId(String instanceId) {
		if (!extranetIpShow) {
			return;
		}
		
		//获取本机器ip
		String localIp = IpUtil.getLocalIp();
		
		// 获取ip，可能是内网ip或者外网ip
		String ipAddress = this.getIpAddress();
		
		if (StringUtils.isNotBlank(ipAddress) && StringUtils.isNotBlank(localIp)) {
			// 替换ip
			instanceId = instanceId.replace(ipAddress,localIp);
			
			log.debug("eureka 的 instanceId 将内网IP替换成外网ip， {} ==> {} ", ipAddress, localIp);
			
			// 设置 ip 地址
			setIpAddress(localIp);
			
			// 设置端口
			int serverPort = Integer
					.parseInt(env.getProperty("server.port", env.getProperty("port", "8080")));
			
			super.setNonSecurePort(serverPort);
		}
		
		super.setInstanceId(instanceId);
	}
	
	@Override
	public void setIpAddress(String ipAddress) {
		if (!extranetIpShow) {
			return;
		}
		//获取本机器ip
		String localIp = IpUtil.getLocalIp();
		
		if (!ipAddress.equals(localIp) && StringUtils.isNotBlank(localIp)) {
			
			ipAddress = localIp;
			log.debug("ip地址替换，{} ===> {}", ipAddress, localIp);
		}
		super.setIpAddress(ipAddress);
	}
	
	public boolean getExtranetIpShow() {
		return extranetIpShow;
	}
	
	public void setExtranetIpShow(boolean extranetIpShow) {
		this.extranetIpShow = extranetIpShow;
	}
}
