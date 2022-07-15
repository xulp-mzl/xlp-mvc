package com.mengzhilan.exception;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * <p>创建时间：2022年7月15日 下午11:56:38</p>
 * @author xlp
 * @version 1.0 
 * @Description controller异常处理接口
*/
@FunctionalInterface
public interface IExceptionHandler {
	/**
	 * 异常处理
	 * 
	 * @param request
	 * @param response
	 * @param exception
	 * @return
	 */
	Object exceptionHandle(ServletRequest request, ServletResponse response, 
			Exception exception);
}
