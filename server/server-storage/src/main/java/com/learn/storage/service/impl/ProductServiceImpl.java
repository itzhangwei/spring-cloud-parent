package com.learn.storage.service.impl;

import com.learn.common.db.dao.storage.ProductDao;
import com.learn.common.entity.db.storage.Product;
import com.learn.common.util.StringUtil;
import com.learn.storage.request.AddProductRequest;
import com.learn.storage.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author zhang
 * @projectName spring-cloud-parent
 * @title ProductServiceImpl
 * @package com.learn.server.service.impl
 * @description 用户
 * @date 2020/4/28 6:48 下午
 */
@Service
@Slf4j(topic = "用户业务类【UserServiceImpl】")
public class ProductServiceImpl implements ProductService {
	
	private final ProductDao productDao;
	
	ProductServiceImpl(ProductDao productDao) {
		this.productDao=productDao;
	}
	
	@Override
	public void addProduct(AddProductRequest productRequest) {
		final Product product = new Product();
		BeanUtils.copyProperties(productRequest, product);
		product.setId(StringUtil.getUUID32());
		product.setLastUpdateBy(productRequest.getCreateBy());
		product.setCreateTime(new Date());
		product.setLastUpdateTime(new Date());
		this.productDao.insert(product);
	}
	
}
