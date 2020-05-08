package com.learn.storage.service;

import com.learn.storage.request.AddProductRequest;

/**
 * @author zhang
 * @projectName spring-cloud-parent
 * @title ProductService
 * @package com.learn.storage
 * @description 商品业务类
 * @date 2020/5/8 3:39 下午
 */
public interface ProductService {
	
	/**
	 * 新增商品
	 * @param productRequest 新增商品请求类
	 */
	void addProduct(AddProductRequest productRequest);
}
