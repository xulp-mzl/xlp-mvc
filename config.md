#xlp-MVC配置说明

### com.mengzhilan.servlet.XLPDispatchedServlet可配置参数（在web.xml里配置）
```
1.configDefault： 值为true时，配置了自定义处理器（配置自定义处理器时，暂不支持该功能），否则没有
2.openExceptionHandler：是否开启controller异常处理功能， true开启，false不开启，
默认值为true，该参数值最后会保存到[System.setProperty("xlp.open.controller.exception.handler", value)]
3.openControllerMethodExcuteBeforeOfAfterDealing：是否开启controller函数调用前后进行自定义操作，
true开启，false不开启，该参数值最后会保存到[System.setProperty("xlp.open.controller.method.execute.dealing", value)]
```

### 模型对应表单配置类
```
com.mengzhilan.form.FormConfig.reload();
```
#可清除缓存类
```
com.mengzhilan.util.ModelBaseConfigReaderUtils.clearCache();
```