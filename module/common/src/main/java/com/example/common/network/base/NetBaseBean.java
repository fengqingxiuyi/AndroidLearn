package com.example.common.network.base;

import androidx.annotation.NonNull;

import com.example.network.bean.NetBean;
import com.example.network.utils.GsonUtil;

import java.io.Serializable;

/**
 * 理论上所有接口都应继承该数据bean
 */
public class NetBaseBean extends NetBean implements Serializable {

    private String returnCode;
    private String returnMsg;

    public String getReturnCode() {
        return returnCode == null ? "" : returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public String getReturnMsg() {
        return returnMsg == null ? "" : returnMsg;
    }

    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
    }

    public boolean isSuccess() {
        return getReturnCode().equals(INetRequestCode.SUCCESS);
    }

    public boolean isToLogin() {
        return getReturnCode().equals(INetRequestCode.TO_LOGIN);
    }

    @NonNull
    @Override
    public String toString() {
        return GsonUtil.GsonToString(this);
    }
}
