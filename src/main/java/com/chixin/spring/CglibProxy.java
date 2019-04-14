package com.chixin.spring;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class CglibProxy implements MethodInterceptor {
    private Object tar;
    CglibProxy(Object tar){
        this.tar=tar;
    }
    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        Object result = null;
       try {
           System.out.println("----------------------------------"+method.getDeclaringClass().getName()+" "+method.getName()+" aop前置捕捉");
           result = methodProxy.invoke(tar, objects);
           System.out.println("----------------------------------"+method.getDeclaringClass().getName()+" "+method.getName()+" aop后置捕捉");
       }catch (Exception e){
           System.out.println("----------------------------------"+method.getDeclaringClass().getName()+" "+method.getName()+" aop异常捕捉");
       }
        return result;
    }
    public static Object initAop(Object classInfo) throws Exception {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(classInfo.getClass());
        enhancer.setCallback(new CglibProxy(classInfo));
        return enhancer.create();
    }

}
