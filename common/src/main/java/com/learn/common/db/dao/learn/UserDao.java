package com.learn.common.db.dao.learn;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.learn.common.entity.db.learn.User;
import org.springframework.stereotype.Repository;

/**
 * @author zhang
 * @projectName spring-cloud-parent
 * @title UserDao
 * @package com.learn.common.db.dao.learn
 * @description 用户dao
 * @date 2020/4/28 11:14 上午
 */
@DS("master")
@Repository
public interface UserDao extends BaseMapper<User> {
}
