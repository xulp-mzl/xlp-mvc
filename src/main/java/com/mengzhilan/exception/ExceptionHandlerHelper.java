package com.mengzhilan.exception;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xlp.utils.XLPStringUtil;

import com.mengzhilan.constant.Constants;

/**
 * <p>创建时间：2022年7月16日 下午12:06:57</p>
 * @author xlp
 * @version 1.0 
 * @Description 异常处理工具类
*/

public class ExceptionHandlerHelper {
	private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandlerHelper.class);
	
	public static Object handleException(ServletRequest request, ServletResponse response, 
			Throwable throwable, ExceptionHandler exceptionHandler){
		
		Class<?> exceptionHandlerClass = null;
		if (exceptionHandler != null) { 
			//获取异常处理实现类
			String exceptionHandlerClassName = exceptionHandler.exceptionHandlerClassName();
			exceptionHandlerClass = createExceptionHandler(exceptionHandlerClassName);
			
			if (exceptionHandlerClass == null) {
				//获取异常处理实现类
				exceptionHandlerClass = exceptionHandler.value();
			} 
		}
		
		// 如果没有获取到异常处理实现类则从系统设置中获取
		if (exceptionHandlerClass == null || exceptionHandlerClass == IExceptionHandler.class) { 
			exceptionHandlerClass = createExceptionHandler(
					System.getProperty(Constants.EXCEPTION_HANDLER_IMPL_KEY));
		}
		
		if (exceptionHandlerClass == null || exceptionHandlerClass == IExceptionHandler.class) { 
			if (LOGGER.isWarnEnabled()) {
				LOGGER.warn("没有获取到异常处理实现类"); 
			}
			return null;
		}
		//实例化异常处理类
		try {
			IExceptionHandler handler = (IExceptionHandler) exceptionHandlerClass.newInstance();
			return handler.exceptionHandle(request, response, throwable);
		} catch (InstantiationException | IllegalAccessException e) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error("实例化异常处理类失败：", e); 
			}
		}
		return null;
	}

	/**
	 * 通过异常处理实现类名，获取实现类
	 * 
	 * @param exceptionHandlerClassName
	 * @return
	 */
	private static Class<?> createExceptionHandler(String exceptionHandlerClassName) {
		Class<?> exceptionHandlerClass = null;
		if (!XLPStringUtil.isEmpty(exceptionHandlerClassName)) {
			try {
				exceptionHandlerClass = Class.forName(exceptionHandlerClassName);
			} catch (ClassNotFoundException e) {
				if (LOGGER.isErrorEnabled()) {
					LOGGER.error("加载异常处理类失败：", e); 
				}
			}
		}
		return exceptionHandlerClass;
	}
}
