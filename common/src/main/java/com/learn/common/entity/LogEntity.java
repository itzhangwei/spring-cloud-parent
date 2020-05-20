package com.learn.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("t_slf4j")
public class LogEntity {
	/**
	 * int long类型ID，mybatis会自动给一个超级大的默认主键值，会引起错误，这里使用数据库自增
	 * AUTO : AUTO(0, “数据库ID自增”),
	 * INPUT : INPUT(1, “用户输入ID”),
	 * ID_WORKER : ID_WORKER(2, “全局唯一ID”),
	 * UUID : UUID(3, “全局唯一ID”),
	 * NONE : NONE(4, “该类型为未设置主键类型”),
	 * ID_WORKER_STR : ID_WORKER_STR(5, “字符串全局唯一ID”);
	 */
	@TableId(value = "id",type = IdType.AUTO)
	public Long id;
	private Date timestamp;
	private String level;
	private String serviceName;
	private String traceId;
	private String spanId;
	private String exportable;
	private String lineNumber;
	private String pid;
	private String thread;
	private String className;
	private String message;
	
}
