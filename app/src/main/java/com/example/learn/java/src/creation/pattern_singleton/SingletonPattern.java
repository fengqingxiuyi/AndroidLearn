package com.example.learn.java.src.creation.pattern_singleton;

import com.example.learn.java.src.creation.pattern_singleton.single.SingleObject3;

/**
 * @author ShenBF
 * @desc 单例模式
 *
 * 注意：
 * 1、单例类只能有一个实例。
 * 2、单例类必须自己创建自己的唯一实例。
 * 3、单例类必须给所有其他对象提供这一实例。
 *
 * 经验之谈：一般情况下，不建议使用第 1 种和第 2 种懒汉方式，建议使用第 3 种饿汉方式。
 * 只有在要明确实现 lazy loading 效果时，才会使用第 5 种登记方式。
 * 如果涉及到反序列化创建对象时，可以尝试使用第 6 种枚举方式。
 * 如果有其他特殊的需求，可以考虑使用第 4 种双检锁方式。
 *
 * @date 2018/7/24
 */
public class SingletonPattern {

    public static void main(String[] args) {
        //获取唯一可用的对象
        SingleObject3 object = SingleObject3.getInstance();
        //显示消息
        object.showMessage();
    }

}
