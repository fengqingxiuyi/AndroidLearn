package com.example.annotation

/**
 * @author fqxyi
 * @date 2020/8/14
 * 编译时注解，注解属性
 */
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.FIELD)
annotation class BindCompile(val value: Int)