package com.learn.common.entity.db.tools;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author zhang
 * @projectName spring-cloud-parent
 * @title RequestLog
 * @package com.learn.common.entity.db.tools
 * @description 请求日志实体类
 * @date 2020/4/16 10:29 上午
 */
@Data
public class RequestLog implements Serializable {
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
	public Long duration;
	public String requestBody;
	public String responseBody;
	public String header;
	public String applicationName;
	public String restUri;
	public String restUrl;
	public Date endTime;
	public String remoteAddr;
	public String remoteHost;
	public int remotePort;
	public String protocol;
	public int portNumber;
	public Date startTime;
	public int status;
	public String method;
	public String userAgent;
	public String traceId;
	public String userName;
	public String queryString;
	public String contentType;
	public String versionControl;
	public String webVersionControl;
	
}