package com.example.jetpacklearn.koin

/**
 * @author fqxyi
 * @date 2020/8/13
 */
class HelloRepositoryImpl2(val code : String) : HelloRepository {
    override fun giveHello(): String = "Hello $code"
}