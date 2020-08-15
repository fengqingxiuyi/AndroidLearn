package com.example.learn.koin

import androidx.lifecycle.ViewModel

/**
 * @author fqxyi
 * @date 2020/8/13
 */
class MyViewModel(val repo : HelloRepository) : ViewModel() {
    fun sayHello() = "${repo.giveHello()} from $this"
}