package com.learn.common.entity.db.storage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
