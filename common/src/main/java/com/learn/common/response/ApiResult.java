package com.learn.common.response;


import com.learn.common.message.BaseException;
import lombok.Data;

/**
 * @author zhang
 * @projectName spring-cloud-alibaba
 * @title ApiResult
 * @package com.learn.cloud.common.response
 * @description 默认方法返回结果集包装类
 * @date 2019/12/30 3:24 下午
 */
@Data
public class ApiResult<T> {
	
	private static final long serialVersionUID = -7543096494555336135L;
	
	/**
	 * 接口状态码 0成功，1失败
	 */
	private String code = "0";
	
	/**
	 * 接口返回消息
	 */
	private String msg = "success";
	
	/**
	 * 错误类型 see  com.learn.cloud.common.message.RuntimeException
	 */
	private String errorType;
	
	/**
	 * 返回数据
	 */
	private T data;
	
	/**
	 * 详细信息，错误时用来返回详细错误信息
	 */
	private String detailMsg = "success";
	
	public ApiResult() {
	}
	
	public ApiResult(T data) {
		this.data = data;
	}
	
	public static ApiResult<Object> success(Object data) {
		ApiResult<Object> result = new ApiResult<>();
		result.setData(data);
		return result;
	}
	
	public static ApiResult<Object> error(BaseException error) {
		ApiResult<Object> result = new ApiResult<>();
		result.setCode(error.getCode());
		result.setMsg(error.getMessage());
		result.setErrorType(error.getErrorType());
		result.setDetailMsg(error.getDetailMsg());
		return result;
	}
	
	public static ApiResult<Object> error(String code, String message, String errorType) {
		ApiResult<Object> result = new ApiResult<>();
		result.setCode(code);
		result.setMsg(message);
		result.setErrorType(errorType);
		return result;
	}
	
	
	public static ApiResult<Object> error( String message, String errorType) {
		ApiResult<Object> result = new ApiResult<>();
		result.setCode("1");
		result.setMsg(message);
		result.setErrorType(errorType);
		return result;
	}
	
	public static ApiResult<String> error( String message) {
		ApiResult<String> result = new ApiResult<String>();
		result.setCode("1");
		result.setMsg(message);
		return result;
	}
	
}
