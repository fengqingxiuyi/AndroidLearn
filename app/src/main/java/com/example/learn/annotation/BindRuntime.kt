package com.example.learn.annotation

/**
 * @author fqxyi
 * @date 2020/8/14
 * 运行时注解
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation class BindRuntime(val value: Int)