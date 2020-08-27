package com.example.network.bean;

/**
 * @author fqxyi
 * @date 2018/2/27
 * 回调给调用者的错误数据
 */
public class ErrorBean {

    /**
     * 网络库封装的错误码
     */
    public int code;
    /**
     * 服务端返回的错误码
     */
    public String serverCode;
    /**
     * 网络库 和 服务端 返回的错误消息
     */
    public String msg;

    public ErrorBean() {
    }

    public ErrorBean(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ErrorBean(int code, String serverCode, String msg) {
        this.code = code;
        this.serverCode = serverCode;
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "ErrorBean{" +
                "code=" + code +
                ", serverCode='" + serverCode + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}
