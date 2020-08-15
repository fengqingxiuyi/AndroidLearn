package com.example.learn.koin

/**
 * @author fqxyi
 * @date 2020/8/13
 */
class HelloRepositoryImpl() : HelloRepository {
    override fun giveHello(): String = "Hello Koin"
}