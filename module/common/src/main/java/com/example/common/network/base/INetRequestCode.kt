package com.example.common.network.base

/**
 * 请求码
 */
interface INetRequestCode {
    companion object {
        /**
         * 成功
         */
        const val SUCCESS = "000000"

        /**
         * 请登录
         */
        const val TO_LOGIN = "000001"
    }
}