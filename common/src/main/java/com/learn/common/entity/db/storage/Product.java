package com.learn.common.entity.db.storage;

import lombok.Data;

import java.util.Date;

/**
 * @author zhang
 * @projectName spring-cloud-parent
 * @title Product
 * @package com.learn.common.entity.db.storage
 * @description
 * @date 2020/4/28 5:22 下午
 */
@Data
public class Product {
	private String id;
	private String productName;
	private String color;
	private String userId;
	private String createBy;
	private Date createTime;
	private String lastUpdateBy;
	private Date lastUpdateTime;
}
