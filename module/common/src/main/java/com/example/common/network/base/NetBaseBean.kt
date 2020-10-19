package com.example.common.network.base

import com.example.network.bean.NetBean
import com.example.network.utils.GsonUtil.GsonToString
import java.io.Serializable

/**
 * 理论上所有接口都应继承该数据bean
 */
open class NetBaseBean : NetBean(), Serializable {
    var returnCode: String? = null
        get() = if (field == null) "" else field
    var returnMsg: String? = null
        get() = if (field == null) "" else field

    val isSuccess: Boolean
        get() = returnCode == INetRequestCode.SUCCESS

    val isToLogin: Boolean
        get() = returnCode == INetRequestCode.TO_LOGIN

    override fun toString(): String {
        return GsonToString(this)
    }
}