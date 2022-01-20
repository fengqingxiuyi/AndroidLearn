package com.example.learn.java.src.structure.pattern_proxy.proxy_static;

import com.example.learn.java.src.structure.pattern_proxy.proxy_static.image.IImage;
import com.example.learn.java.src.structure.pattern_proxy.proxy_static.image.impl.ProxyImage;
import com.example.learn.java.src.structure.pattern_proxy.proxy_static.image.impl.RealImage;

/**
 * @author fqxyi
 * @desc 静态代理模式 当被请求时，使用 ProxyImage 来获取 RealImage 类的对象
 * @date 2018/7/24
 */
public class ProxyStaticPattern {

    public static void main(String[] args) {
        //目标对象
        IImage target = new RealImage("test_10mb.jpg");
        //代理对象,把目标对象传给代理对象,建立代理关系
        IImage image = new ProxyImage(target);
        //执行的是代理的方法
        image.display();
    }

}
