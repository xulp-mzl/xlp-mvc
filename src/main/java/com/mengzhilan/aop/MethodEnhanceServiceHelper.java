package com.mengzhilan.aop;

import java.lang.reflect.Method;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xlp.utils.XLPStringUtil;

import com.mengzhilan.constant.Constants;

/**
 * <p>创建时间：2022年7月16日 下午5:05:14</p>
 * @author xlp
 * @version 1.0 
 * @Description 函数增强工具类
*/
public class MethodEnhanceServiceHelper {
	private static final Logger LOGGER = LoggerFactory.getLogger(MethodEnhanceServiceHelper.class);
	
	/**
	 * 函数处理前要执行另一些操作
	 * 
	 * @param request 请求对象
	 * @param response 响应对象
	 * @param target 执行的类
	 * @param method 执行的方法
	 * @param parameters 执行方法参数
	 * @return
	 */
	public static void before(ServletRequest request, ServletResponse response, 
			Class<?> target, Method method, Before before, Object ...parameters){
		if (before == null) return;
		
		Class<?> ibeforeClass = null;
		String ibeforeClassName = before.beforeClassName();
		ibeforeClass = createExceptionHandler(ibeforeClassName);
		
		if (ibeforeClass == null) {
			ibeforeClass = before.value();
		}
		
		// 如果没有获取到实现类则从系统设置中获取
		if (ibeforeClass == null || ibeforeClass == IBefore.class) { 
			ibeforeClass = createExceptionHandler(
					System.getProperty(Constants.CONTROLLER_METHOD_EXECUTE_BEFORE));
		}
		
		if (ibeforeClass == null || ibeforeClass == IBefore.class) { 
			if (LOGGER.isWarnEnabled()) {
				LOGGER.warn("没有获取到函数增强实现类"); 
			}
			return;
		}
		
		//实例化函数增强类
		try {
			IBefore iBefore = (IBefore) ibeforeClass.newInstance();
			iBefore.before(request, response, target, method, parameters);
		} catch (InstantiationException | IllegalAccessException e) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error("实例化函数增强类失败：", e); 
			}
		}
	}
	
	/**
	 * 函数执行后要执行另一些操作
	 * 
	 * @param request 请求对象
	 * @param response 响应对象
	 * @param target 执行的类
	 * @param method 执行的方法
	 * @param parameters 执行方法参数
	 * @param throwable 异常
	 * @return
	 */
	public static void after(ServletRequest request, ServletResponse response, 
			Class<?> target, Method method, Throwable throwable, 
			After after, Object ...parameters){
		if (after == null) return;
		
		Class<?> iafterClass = null; 
		String iafterClassName = after.afterClassName(); 
		iafterClass = createExceptionHandler(iafterClassName);
		
		if (iafterClass == null) {
			iafterClass = after.value();
		}
		
		// 如果没有获取到实现类则从系统设置中获取
		if (iafterClass == null || iafterClass == IAfter.class) { 
			iafterClass = createExceptionHandler(
					System.getProperty(Constants.CONTROLLER_METHOD_EXECUTE_AFTER));
		}
		
		if (iafterClass == null || iafterClass == IBefore.class) { 
			if (LOGGER.isWarnEnabled()) {
				LOGGER.warn("没有获取到函数增强实现类"); 
			}
			return;
		}
		
		//实例化函数增强类
		try {
			IAfter iAfter = (IAfter) iafterClass.newInstance();
			iAfter.after(request, response, target, method, throwable, parameters);
		} catch (InstantiationException | IllegalAccessException e) {
			if (LOGGER.isErrorEnabled()) {
				LOGGER.error("实例化函数增强类失败：", e); 
			}
		}
	}
	
	
	/**
	 * 通过函数增强处理类名，获取实现类
	 * 
	 * @param className
	 * @return
	 */
	private static Class<?> createExceptionHandler(String className) {
		Class<?> targetClass = null;
		if (!XLPStringUtil.isEmpty(className)) {
			try {
				targetClass = Class.forName(className);
			} catch (ClassNotFoundException e) {
				if (LOGGER.isErrorEnabled()) {
					LOGGER.error("加载函数增强处理类失败：", e); 
				}
			}
		}
		return targetClass;
	}
}
