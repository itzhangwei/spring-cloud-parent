package com.learn.server.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author zhang
 * @projectName spring-cloud-parent
 * @title AddUserRequest
 * @package com.learn.server.request
 * @description 新增用户请求类
 * @date 2020/4/28 6:44 下午
 */
@Data
public class AddUserRequest {
	@NotBlank(message = "姓名不能为空")
	private String name;
	@NotNull(message = "性别不能为空")
	private Character sex;
	@NotBlank(message = "手机号不能为空")
	private String phone;
	@NotBlank(message = "昵称不能为空")
	private String nickname;
	@NotBlank(message = "用户名不能为空")
	private String username;
	@NotBlank(message = "密码不能为空")
	private String password;

}
