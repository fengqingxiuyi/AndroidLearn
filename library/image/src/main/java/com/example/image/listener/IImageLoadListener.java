package com.example.image.listener;

/**
 * @author fqxyi
 * @date 2018/2/22
 * 用于检测图片加载成功或失败的监听器
 */
public interface IImageLoadListener {

    void onSuccess(String id, int width, int height);

    void onFailure(String id, Throwable throwable);

}
