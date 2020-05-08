package com.learn.server.controller;

import com.learn.common.response.ApiResult;
import com.learn.server.request.AddUserRequest;
import com.learn.server.service.UserService;
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
@RequestMapping("/user")
public class UserController {
	
	private final UserService userService;
	
	UserController(UserService userService) {
		this.userService = userService;
	}
	
	@PostMapping("/add")
	public ApiResult<?> addUser(@Valid @RequestBody AddUserRequest addUserRequest){
		userService.addUser(addUserRequest);
		return ApiResult.success();
	}
}
