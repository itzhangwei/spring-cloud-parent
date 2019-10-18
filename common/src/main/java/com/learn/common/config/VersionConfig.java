package com.learn.common.config;

import com.netflix.appinfo.ApplicationInfoManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * @author zhang
 * @projectName spring-cloud-parent
 * @title VersionConfig
 * @package com.learn.common.config
 * @description eureka中的服务版本号配置
 * @date 2019/10/18 2:58 下午
 *
 * 通过其两个属性name以及havingValue来实现的，其中name用来从application.properties中读取某个属性值。
 * 如果该值为空，则返回false;
 * 如果值不为空，则将该值与havingValue指定的值进行比较，如果一样则返回true;否则返回false。
 * 如果返回值为false，则该configuration不生效；为true则生效。
 *
 *
 * 构建成docker镜像的时候，在docker镜像启动的时候，使用 -e 参数 myself.eureka.version= 。。。
 * 来将自己自定义的版本号写入到eureka注册中心去。
 */
@Slf4j
@Configuration
@ConditionalOnProperty(name="myself.eureka.version")
public class VersionConfig {
	
	@Autowired
	private ApplicationInfoManager manager;
	
	@Value("${myself.eureka.version}")
	private String version;
	
	/**
	 * PostConstruct 修饰一个非静态的void（）方法，在服务器加载Servlet的时候运行，并且只会被服务器执行一次。
	 *
	 * @PostConstruct注解的方法将会在依赖注入完成后被自动调用。（依赖注入的对象中包含PostConstruct修饰的方法，优先执行构造函数）
	 * Constructor >> @Autowired >> @PostConstruct
	 */
	@PostConstruct
	public void init(){
		log.error("myself.eureka.version:{}", version);
		
		// 获取注册实例信息map
		Map<String, String> metadata = manager.getInfo().getMetadata();
		
		//添加版本号到实例信息map中
		metadata.put("myself.eureka.version",version);
	}

}
