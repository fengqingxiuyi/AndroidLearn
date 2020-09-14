package com.example.learn.java.src.structure.pattern_proxy.proxy_dynamic;

import com.example.learn.java.src.structure.pattern_proxy.proxy_dynamic.factory.ProxyFactory;
import com.example.learn.java.src.structure.pattern_proxy.proxy_dynamic.image.IImage;
import com.example.learn.java.src.structure.pattern_proxy.proxy_dynamic.image.impl.RealImage;

/**
 * @author ShenBF
 * @desc 动态代理模式 控制访问
 * @date 2018/7/24
 */
public class ProxyDynamicPattern {

    public static void main(String[] args) {
        //目标对象
        IImage target = new RealImage("test_10mb.jpg");
        //【原始的类型 class cn.itcast.b_dynamic.UserDao】
        System.out.println(target.getClass());

        //给目标对象，创建代理对象
        IImage proxy = (IImage) new ProxyFactory(target).getProxyInstance();
        //class $Proxy0   内存中动态生成的代理对象
        System.out.println(proxy.getClass());

        //执行方法【代理对象】
        proxy.display();
    }

}
