package com.learn.server.client;

import com.learn.common.entity.db.storage.Product;
import com.learn.common.response.ApiResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author zhang
 * @projectName spring-cloud-parent
 * @title ProductClient
 * @package com.learn.storage.client
 * @description storage的 feign 客户端
 * @date 2020/5/8 3:56 下午
 */
@Component
@FeignClient("server-storage")
public interface ProductClient {
	
	@PostMapping("/product/add")
	ApiResult<?> addUser(Product product);
}
