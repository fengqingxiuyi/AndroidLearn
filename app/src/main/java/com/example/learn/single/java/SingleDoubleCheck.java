package com.example.learn.single.java;

/**
 * @author fqxyi
 * @date 2020/8/17
 * 懒汉式，双检锁
 * instance = new SingleDoubleCheck();
 * 这句代码在执行时会分解为三个步骤：
 *   1. 为对象分配内存空间。
 *   2. 执行初始化的代码。
 *   3. 将分配好的内存地址设置给instance引用。
 * 不加`volatile`，因为第2步和第3步之间无依赖关系，所以存在指令重排序。
 *   假设先执行第3步，再执行第2步，此时某个线程A刚好执行完第3步，正在执行第2步时，接着线程B进入if (instance == null)判断，
 *   会发现instance不为null，然后将instance返回，但是实际上instance还没有完成初始化，线程B会访问到一个未初始化完成的instance对象。
 * 加了`volatile`之后，会多出一个lock前缀指令，lock前缀指令相当于一个内存屏障（内存栅栏），有三个作用：
 *   1. 确保指令重排序时，内存屏障前的指令不会排到后面去，内存屏障后的指令不会排到前面去。
 *   2. 强制对变量在线程工作内存中的修改操作立即写入到物理内存。
 *   3. 如果是写操作，会导致其他CPU中对这个变量的缓存失效，强制其他CPU中的线程在获取变量时从物理内存中获取更新后的值。
 */
public class SingleDoubleCheck {

    private SingleDoubleCheck() {

    }

    private static volatile SingleDoubleCheck instance;

    public static SingleDoubleCheck getInstance() {
        if (instance == null) {
            synchronized (SingleDoubleCheck.class) {
                if (instance == null) {
                    instance = new SingleDoubleCheck();
                }
            }
        }
        return instance;
    }

    public void single() {
        System.out.println("java class SingleDoubleCheck single");
    }

}
