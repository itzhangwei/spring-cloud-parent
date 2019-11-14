package com.learn.common.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * @author zhang
 * @projectName spring-cloud-parent
 * @title LogEntity
 * @package com.learn.common.entity
 * @description 日志实体类
 * @date 2019/11/13 7:43 下午
 */
@Data
@Document(collection = "log")
public class LogEntity {
	
	private Date timestamp;
	private String severity;
	private String service;
	private String trace;
	private String span;
	private String exportable;
	private String pid;
	private String thread;
	private String className;
	private String message;
	
}
