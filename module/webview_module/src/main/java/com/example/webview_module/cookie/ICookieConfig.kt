package com.example.webview_module.cookie

/**
 * cookie配置信息
 */
interface ICookieConfig {
    companion object {
        //测试域名
        const val debugDomain = ".mamahao.com"

        //正式域名
        const val releaseDomain = ".mamhao.com"

        //h5同步cookie加密key
        const val h5key = "jdakjfa20dsllll2"
    }
}