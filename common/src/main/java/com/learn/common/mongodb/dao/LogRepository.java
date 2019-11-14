package com.learn.common.mongodb.dao;

import com.learn.common.entity.LogEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * @author zhang
 * @projectName spring-cloud-parent
 * @title LogRepository
 * @package com.learn.common.mongodb.dao
 * @description 日志dao
 * @date 2019/11/8 5:09 下午
 */
@Repository
public interface LogRepository extends MongoRepository<LogEntity, Serializable> {
}
