package com.learn.server.config.properties;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 自定义配置
 *
 * @author zhang
 * @projectName spring-cloud-parent
 * @title MySelfProperties
 * @package com.learn.server.config.properties
 * @description 自定义配置
 * @date 2019/10/17 2:14 下午
 */
@Data
@Component
@ConfigurationProperties(prefix = "myself.config")
public class MySelfProperties {
	
	/**
	 * 标题
	 */
	private String title;
	
	private MySelfProperties.Article article = new MySelfProperties.Article();
	
	@Getter
	@Setter
	public static class Article {
		
		/**
		 * 名言
		 */
		private String WellKnownSaying;
	}
}
