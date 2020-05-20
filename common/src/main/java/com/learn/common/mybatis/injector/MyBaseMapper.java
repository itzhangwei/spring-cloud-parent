package com.learn.common.mybatis.injector;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @author zhang
 * @projectName spring-cloud-parent
 * @title MyBaseMapper
 * @package com.learn.common.mybatis.injector
 * @description 添加自定义全局方法，结合baseMapper,继承MyBaseMapper即可
 * @date 2020/5/19 10:36 上午
 */
public interface MyBaseMapper<T> extends BaseMapper<T> {
	
	/**
	 * 判断实体类T对应的数据表是否存在
	 * count大于0就存在 <BR>
	 * @return int
	 * @author zhangwei
	 * @createTime 2020/4/16 11:57 上午
	 */
	int existTable();
	
}
