package com.mengzhilan.handler;


import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.xlp.assertion.AssertUtils;

/**
 * <p>创建时间：2022年10月25日 下午9:37:45</p>
 * @author xlp
 * @version 1.0 
 * @Description 通过字节码获取函数参数名称
*/

public class MethodParameterASMHandler {
	/**
	 * 要获取参数的方法
	 */
	private Method method;
	
	/**
	 * 方法参数名称集合
	 */
	private List<String> methodParameterNames;
	
	/**
	 * 当前索引
	 */
	private int currentIndex = 0;
	
	/**
	 * @param method
	 * @throws NullPointerException 假如参数为空则抛出该异常
	 */
	public MethodParameterASMHandler(Method method){
		AssertUtils.isNotNull(method, "method parameter is not null!");
		this.method = method;
	}
	
	public void handle() {
		if (methodParameterNames == null) {
			methodParameterNames = new ArrayList<String>();
			final String methodName = method.getName();
	        final Class<?>[] methodParameterTypes = method.getParameterTypes();
	        final String className = method.getDeclaringClass().getName();
	        final boolean isStatic = Modifier.isStatic(method.getModifiers());

	        ClassReader cr;
			try {
				cr = new ClassReader(className);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
	        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
	        cr.accept(new ClassVisitor(Opcodes.ASM9, cw) {
	            public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
	                MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);

	                final Type[] argTypes = Type.getArgumentTypes(desc);

	                //参数类型不一致
	                if (!methodName.equals(name) || !matchTypes(argTypes, methodParameterTypes)) {
	                    return mv;
	                }
	                
	                return new MethodVisitor(Opcodes.ASM9, mv) {
	                    public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
	                    	//假如不是静态方法，跳过第一个参数名称，非静态方法第一个参数是this
	                    	if (isStatic || index != 0) {
	                            methodParameterNames.add(name);
	                        }
	                        super.visitLocalVariable(name, desc, signature, start, end, index);
	                    }
	                };
	            }
	        }, 0);
		}
    }

    /**
     * 比较参数是否一致
     */
	private static boolean matchTypes(Type[] types, Class<?>[] parameterTypes) {
        if (types.length != parameterTypes.length) {
            return false;
        }
        for (int i = 0; i < types.length; i++) {
            if (!Type.getType(parameterTypes[i]).equals(types[i])) {
                return false;
            }
        }
        return true;
    }

	/**
	 * 获取参数名称集合
	 * @return the methodParameterNames
	 */
	public List<String> getMethodParameterNames() {
		return methodParameterNames;
	}
	
	/**
	 * 获取指定索引的参数名称
	 * 
	 * @param index
	 * @return 假如索引值不合法，返回null，否则返回指定索引的参数名称
	 */
	public String getMethodParameterName(int index) {
		if (index < 0 || index >= methodParameterNames.size()) return null;
		return methodParameterNames.get(index);
	}
	
	/**
	 * 获取当前索引的参数名称，执行该方法后，当前索引自动加1
	 * 
	 * @return 假如索引值不合法，返回null，否则返回指定索引的参数名称
	 */
	public String getMethodParameterNameOfCurrentIndex() {
		String parameterName = getMethodParameterName(currentIndex);
		currentIndex++;
		return parameterName;
	}

	public int getCurrentIndex() {
		return currentIndex;
	}

	/**
	 * 设置当前参数获取的索引值
	 * @param currentIndex
	 */
	public void setCurrentIndex(int currentIndex) {
		this.currentIndex = currentIndex;
	}
}
