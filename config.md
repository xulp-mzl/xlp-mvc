#xlp-MVC配置说明

### com.mengzhilan.servlet.XLPDispatchedServlet可配置参数（在web.xml里配置）
```
1.configDefault： 值为true时，配置了自定义处理器（配置自定义处理器时，暂不支持该功能），否则没有
```

### 系统配置
```
1.System.setProperty("xlp.open.controller.exception.handler", "true") 
统一开启异常处理

2.System.setProperty("xlp.controller.exception.handler.impl", "com.mengzhilan.exception.IExceptionHandler 实现类全路径")
统一设置异常处理实现类

3.System.setProperty("xlp.open.controller.method.execute.dealing", "true")
开启Controller函数调用前后进行自定义操作

4.System.setProperty("xlp.controller.method.execute.before", "com.mengzhilan.aop.IBefore 实现类全路径")
统一设置Controller函数调用前进行自定义操作实现类

5.System.setProperty("xlp.controller.method.execute.after", "com.mengzhilan.aop.IAfter 实现类全路径")
统一设置Controller函数调用后进行自定义操作实现类
```

