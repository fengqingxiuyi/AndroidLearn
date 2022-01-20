package com.example.learn.java.src.structure.pattern_proxy.proxy_dynamic.factory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author fqxyi
 * @desc 创建动态代理对象，动态代理不需要实现接口,但是需要指定接口类型
 * 代理对象不需要实现接口,但是目标对象一定要实现接口,否则不能用动态代理
 * @date 2018/7/24
 */
public class ProxyFactory {

    //维护一个目标对象
    private Object target;

    public ProxyFactory(Object target) {
        this.target = target;
    }

    //给目标对象生成代理对象
    public Object getProxyInstance() {
        return Proxy.newProxyInstance(
                target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        System.out.println("开始事务2");
                        //执行目标对象方法
                        Object returnValue = method.invoke(target, args);
                        System.out.println("提交事务2");
                        return returnValue;
                    }
                }
        );
    }

}
