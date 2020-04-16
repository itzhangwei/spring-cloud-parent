package com.learn.common.entity.db.tools;

import lombok.Data;

/**
 * @author zhang
 * @projectName spring-cloud-parent
 * @title RequestLog
 * @package com.learn.common.entity.db.tools
 * @description 请求日志实体类
 * @date 2020/4/16 10:29 上午
 */
@Data
public class RequestLog {
	public Integer id;
	public Long duration;
	public String userId;
	public String requestBody;
	public String responseBody;
	public String applicationName;
	public String restUri;
	public String restUrl;
	public Long logTime;
	public String remoteAddr;
	public String remoteHost;
	public int remotePort;
	public String protocol;
	public int portNumber;
	public Long startTime;
	public int status;
	public String method;
	public String userAgent;
	public String reqId;
	public String ver;
	public String queryString;
	public String contentType;
	
}
