package com.learn.server.service;

import com.learn.server.request.AddUserRequest;

/**
 * @author zhang
 * @projectName spring-cloud-parent
 * @title UserService
 * @package com.learn.server.service
 * @description
 * @date 2020/4/28 6:47 下午
 */
public interface UserService {
	/**
	 * 新增用户 <BR>
	 * @param addUserRequest 请求参数
	 * @author zhangwei
	 * @createTime 2020/4/28 6:49 下午
	 */
	void addUser(AddUserRequest addUserRequest);
}
