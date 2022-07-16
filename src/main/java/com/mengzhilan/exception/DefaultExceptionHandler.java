package com.mengzhilan.exception;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.xlp.json.JsonObject;
import org.xlp.utils.XLPStackTraceUtil;

/**
 * <p>创建时间：2022年7月16日 上午12:02:01</p>
 * @author xlp
 * @version 1.0 
 * @Description 默认异常处理器，什么都不处理
*/
public class DefaultExceptionHandler implements IExceptionHandler{

	@Override
	public Object exceptionHandle(ServletRequest request, ServletResponse response, 
			Throwable throwable) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.put("errorMsg", XLPStackTraceUtil.getStackTrace(throwable).replace("\n", "\\\n"));
		return jsonObject;
	}

}
