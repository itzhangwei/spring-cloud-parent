package com.learn.storage.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author zhang
 * @projectName spring-cloud-parent
 * @title AddUserRequest
 * @package com.learn.server.request
 * @description 新增用户请求类
 * @date 2020/4/28 6:44 下午
 */
@Data
public class AddProductRequest {
	
	@NotBlank(message = "商品名称不能为空")
	private String productName;
	@NotBlank(message = "颜色不能为空")
	private String color;
	@NotBlank(message = "用户ID为空")
	private String userId;
	
	@NotBlank(message = "创建人不能为空")
	private String createBy;

}
