package com.learn.common.mybatis.injector;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.learn.common.mybatis.injector.method.ExistTableMethod;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author zhang
 * @projectName spring-cloud-parent
 * @title MySqlInjector
 * @package com.learn.common.mybatis.injector
 * @description sql注入器
 * @date 2020/5/19 10:25 上午
 */
@Component
public class MySqlInjector extends DefaultSqlInjector {
	@Override
	public List<AbstractMethod> getMethodList(Class<?> mapperClass) {
		final List<AbstractMethod> methodList = super.getMethodList(mapperClass);
		methodList.add(new ExistTableMethod());
		return methodList;
	}
}
