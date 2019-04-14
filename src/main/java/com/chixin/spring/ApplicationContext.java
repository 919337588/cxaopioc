package com.chixin.spring;

import com.chixin.note.CxAutowired;
import com.chixin.note.CxCompent;
import com.chixin.note.CxControl;
import com.chixin.note.CxService;
import com.chixin.utils.ClassUtil;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ApplicationContext {
    /*扫包地址*/
    private String packagePath;
    //bean容器
    private ConcurrentHashMap<Class,Object> beanfactory;
    /*bean注释集合*/
    private static HashSet<Class> containerAnnotations;

    static {
        //初始化bean的注入注解
        containerAnnotations=new HashSet<Class>();
        containerAnnotations.add(CxControl.class);
        containerAnnotations.add(CxService.class);
        containerAnnotations.add(CxCompent.class);
    }
    public ApplicationContext(String packagePath) throws Exception {
        this.packagePath=packagePath;
        beanfactory = new ConcurrentHashMap<Class, Object>();
        //初始化bean
        initBeans();
        //完善bean
        beanDecoration();
    }
  // 初始化bean对象
    public void initBeans() throws Exception {
        List<Class> listClassesAnnotation= findClassExisService();
        for (Class classInfo : listClassesAnnotation) {
            // 初始化对象
            Object newInstance =classInfo.newInstance();
            // bean放入容器
            beanfactory.put(classInfo, newInstance);
        }
    }
    //完善bean
    public void beanDecoration() throws Exception {
        for(Map.Entry<Class,Object> val: beanfactory.entrySet()){
            //给bean的属性注入值
            attriAssign(val.getValue());
            //aop包裹
            doAop(val);
        };
    }
    // 使用反射读取类的属性,赋值信息
    public void attriAssign(Object object) throws Exception {
        // 1.获取类的属性是否存在 获取bean注解
        Class<? extends Object> classInfo = object.getClass();
        Field[] declaredFields = classInfo.getDeclaredFields();
        for (Field field : declaredFields) {
            if (field.getDeclaredAnnotation(CxAutowired.class) != null) {
                Object bean = beanfactory.get(field.getType());
                if (bean != null) {
                    // 私有访问允许访问
                    field.setAccessible(true);
                    // 给属性赋值
                    field.set(object, bean);
                }
            }
        }
    }
    // bean变为aop代理对象
    public void doAop(Map.Entry<Class,Object> val ) throws Exception {
        val.setValue(CglibProxy.initAop(val.getValue()));
    }

    // 使用反射机制获取该包下所有的类已经存在bean的注解类
    public List<Class> findClassExisService() throws Exception {
        // 1.使用反射机制获取该包下所有的类
        if (StringUtils.isBlank(packagePath)) {
            throw new Exception("扫包地址不能为空!");
        }
        // 2.使用反射技术获取当前包下所有的类
        List<Class<?>> classesByPackageName = ClassUtil.getClasses(packagePath);
        // 3.存放类上有bean注入注解
        List<Class> exisClassesAnnotation = new LinkedList<>();
        // 4.判断该类上属否存在注解
        for (Class classInfo : classesByPackageName) {
            for (Class containerAnnotation:containerAnnotations){
                Object cx =  classInfo.getDeclaredAnnotation(containerAnnotation);
                if (cx != null) {
                    exisClassesAnnotation.add(classInfo);
                    break;
                }
            }
        }
        return exisClassesAnnotation;
    }
    public <T>T getBean(Class classinfo){
        return (T) beanfactory.get(classinfo);
    }
}
