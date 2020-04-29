package com.learn.common.handler;

import com.learn.common.message.BaseException;
import com.learn.common.message.Errors;
import com.learn.common.response.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

/**
 * @author zhang
 * @projectName spring-cloud-alibaba
 * @title ExceptionHandler
 * @package com.learn.cloud.common.handler
 * @description 全局异常处理
 * @date 2019/12/30 6:31 下午
 */
@RestControllerAdvice
@Slf4j
public class MyExceptionHandler {
	
	
	/**
	 * 全局异常处理，统一返回结构 <BR>
	 *
	 *     ExceptionHandler 定义拦截的异常，这里拦截所有异常
	 * @param e 拦截到的异常
	 * @return com.learn.cloud.common.response.ApiResult
	 * @throws Exception 异常
	 * @author zhangwei
	 * @createTime 2019/12/30 6:37 下午
	 */
	@ResponseBody
	@ExceptionHandler(value = Exception.class)
	public ApiResult<?> handler(Exception e){
		//判断是不是自定义异常
		if (e instanceof BaseException) {
			return ApiResult.error((BaseException) e);
		} else {
			BaseException sysError = Errors.SYS_ERROR;
			log.error("错误信息", e);
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			sysError.setDetailMsg(sw.toString());
			return ApiResult.error(sysError);
		}
	}
	
	/**
	 * 自定义异常拦击
	 * @param req 请求
	 * @param e 异常
	 * @return ApiResult
	 */
	@ResponseStatus(HttpStatus.OK)
	@ExceptionHandler(value = BaseException.class)
	public ApiResult<?> baseExceptionHandler(HttpServletRequest req, BaseException e){
		//自定义异常
		log.error("自定义异常：url:{},message:{}",req.getRequestURL(),e.getMessage());
		return ApiResult.error(e);
	}
	
	/**
	 * 全局参数校验异常拦截 <BR>
	 * @param e 参数校验异常类
	 * @return com.learn.common.response.ApiResult<?>
	 * @author zhangwei
	 * @createTime 2020/4/29 2:29 下午
	 */
	@ResponseStatus(HttpStatus.OK)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ApiResult<?> handleValidationException(MethodArgumentNotValidException e){
		//自定义异常
		
		BindingResult bindingResult = e.getBindingResult();
		StringBuilder msg = new StringBuilder();
		
		List<ObjectError> objErrorList = bindingResult.getAllErrors();
		final BaseException errorParam = Errors.ERROR_PARAM;
		
		for (ObjectError error : objErrorList) {
			msg.append(error.getDefaultMessage()).append(",");
		}
		log.error("=============>参数验证不通过：{}",msg.toString());
		
		errorParam.setDetailMsg(msg.toString());
		return ApiResult.error(errorParam);
	}
	
	@ResponseStatus(HttpStatus.OK)
	@ExceptionHandler({HttpMessageNotReadableException.class})
	public ApiResult<?> aa(Exception exception){
		log.error("异常",exception);
		final BaseException sysError = Errors.SYS_ERROR;
		return ApiResult.error(sysError);
	}
}
