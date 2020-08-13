package com.example.jetpacklearn.koin

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

/**
 * @author fqxyi
 * @date 2020/8/13
 */
val appModule = module {
    // single instance of HelloRepository
//    single<HelloRepository> { HelloRepositoryImpl() }
//    single { HelloRepositoryImpl() as HelloRepository } //与上述方式功能一致
//    factory<HelloRepository> { HelloRepositoryImpl() } //single即单例，factory每次都会新建实例
    single<HelloRepository> { (code: String) -> HelloRepositoryImpl2(code) } //传参
    // MyViewModel ViewModel
//    viewModel { MyViewModel(get()) }
    viewModel { (code: String) -> MyViewModel(get { parametersOf(code) }) }
}