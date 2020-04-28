package com.learn.common.entity.db.learn;

import lombok.Data;

import java.util.Date;

/**
 * @author zhang
 * @projectName spring-cloud-parent
 * @title User
 * @package com.learn.common.entity.db.learn
 * @description 用户实体类
 * @date 2020/4/28 11:12 上午
 */
@Data
public class User {
	
	private String id;
	private String name;
	private char sex;
	private String phone;
	private String nickname;
	private String username;
	private String password;
	private String createBy;
	private Date createTime;
	private String lastUpdateBy;
	private Date lastUpdateTime;
}
