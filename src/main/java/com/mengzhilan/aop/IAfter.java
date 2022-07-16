package com.mengzhilan.aop;

import java.lang.reflect.Method;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * <p>创建时间：2022年7月16日 下午4:49:14</p>
 * @author xlp
 * @version 1.0 
 * @Description 函数执行后要执行另一些操作接口
*/
public interface IAfter {
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
	void after(ServletRequest request, ServletResponse response, 
			Class<?> target, Method method, Throwable throwable, Object ...parameters);
}
