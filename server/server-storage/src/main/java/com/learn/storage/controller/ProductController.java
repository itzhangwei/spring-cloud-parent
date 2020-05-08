package com.learn.storage.controller;

import com.learn.common.response.ApiResult;
import com.learn.storage.request.AddProductRequest;
import com.learn.storage.service.ProductService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author zhang
 * @projectName spring-cloud-parent
 * @title UserController
 * @package com.learn.server.controller
 * @description 用户
 * @date 2020/4/28 5:32 下午
 */
@RestController
@RequestMapping("/product")
public class ProductController {
	
	private final ProductService productService;
	
	ProductController(ProductService productService) {
		this.productService = productService;
	}
	
	@PostMapping("/add")
	public ApiResult<?> addUser(@Valid @RequestBody AddProductRequest addProductRequest){
		productService.addProduct(addProductRequest);
		return ApiResult.success();
	}
}
