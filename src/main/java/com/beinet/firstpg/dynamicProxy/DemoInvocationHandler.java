package com.beinet.firstpg.dynamicProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DemoInvocationHandler implements InvocationHandler {
    private Object target;
    private Class clazz;

    private DemoInvocationHandler(Class clazz, Object obj) {
        this.clazz = clazz;
        this.target = obj;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String methodName = method.getName();
        if (methodName.equals("toString"))
            return toString(proxy);

        System.out.println("代理开始...");
        System.out.println("被代理类: " + clazz);

        System.out.println("调用方法: " + method.getDeclaringClass().getName() + "." + methodName);
        if (args != null && args.length > 0) {
            System.out.print("参数:");
            for (Object obj : args) {
                System.out.print(" " + obj);
            }
            System.out.println("");
        }
        Object ret;
        if (!(target instanceof Class)) {
            ret = method.invoke(target, args);// invoke参数如果传入proxy，会陷入死循环
        } else {
            ret = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
        }
        System.out.println("返回结果: " + ret);
        System.out.println("代理结束.");
        return ret;
    }

    private String toString(Object proxy) {
        if (!(target instanceof Class))
            return String.valueOf(target);
        return clazz.getName();
    }

    public static <T> T getProxy(Object obj) {
        if (obj == null)
            throw new IllegalArgumentException("parameter cant be null");

        Class clazz;
        if (obj instanceof Class) {
            clazz = (Class) obj;
        } else {
            clazz = obj.getClass();
        }

        Class[] interfaces;
        if (clazz.isInterface()) {
            interfaces = new Class[]{clazz};
        } else {
            interfaces = clazz.getInterfaces();
        }
        // 参数1：由哪个ClassLoader来加载【新生成的代理对象】到jvm里
        // 参数2：被代理的接口信息，代理类可以强转为这些接口之一
        // 参数3： 具体代理处理器，所有接口方法都会转到处理器处理
        Object proxy = Proxy.newProxyInstance(clazz.getClassLoader(), interfaces, new DemoInvocationHandler(clazz, obj));
        return (T) proxy;
    }
}
