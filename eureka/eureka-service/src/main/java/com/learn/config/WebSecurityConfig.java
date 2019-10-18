package com.learn.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author zhang
 * @projectName spring-cloud-parent
 * @title WebSecurityConfig
 * @package com.learn.config
 * @description 安全认证
 * 新版（Spring Cloud 2.0 以上）的security默认启用了csrf检验，
 *      导致client在向eureka服务上注册的时候无法注册。
 *      要在eurekaServer端配置security的csrf检验为false;
 * @date 2019/10/10 7:16 下午
 */
@EnableWebSecurity
public class WebSecurityConfig  extends WebSecurityConfigurerAdapter {
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		super.configure(http);
	}
}
