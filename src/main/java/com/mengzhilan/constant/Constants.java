package com.mengzhilan.constant;
/**
 * <p>创建时间：2022年7月16日 下午4:24:25</p>
 * @author xlp
 * @version 1.0 
 * @Description 常量
*/
public class Constants {
	/**
	 * 是否开启异常处理key
	 */
	public static final String OPEN_EXCEPTION_HANDLER_KEY = "xlp.open.controller.exception.handler";
	/**
	 * 异常处理实现类配置key
	 */
	public static final String EXCEPTION_HANDLER_IMPL_KEY = "xlp.controller.exception.handler.impl";
	
	/**
	 * 开启Controller函数调用前后进行自定义操作key
	 */
	public static final String OPEN_CONTROLLER_METHOD_EXECUTE_DEALING = "open.controller.method.execute.dealing";
	
	/**
	 * 设置Controller函数调用前进行自定义操作实现类key
	 */
	public static final String CONTROLLER_METHOD_EXECUTE_BEFORE = "xlp.controller.method.execute.before";
	
	/**
	 * 设置Controller函数调用后进行自定义操作实现类key
	 */
	public static final String CONTROLLER_METHOD_EXECUTE_AFTER = "xlp.controller.method.execute.after";
}
