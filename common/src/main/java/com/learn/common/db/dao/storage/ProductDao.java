package com.learn.common.db.dao.storage;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.learn.common.entity.db.storage.Product;
import org.springframework.stereotype.Repository;

/**
 * @author zhang
 * @projectName spring-cloud-parent
 * @title ProductDao
 * @package com.learn.common.db.dao.storage
 * @description 商品dao
 * @date 2020/4/28 5:21 下午
 */
@DS("storage")
@Repository
public interface ProductDao extends BaseMapper<Product> {
}
