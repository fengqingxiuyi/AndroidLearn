package com.example.network.tag;

import com.example.network.callback.IResponseCallback;

/**
 * @author fqxyi
 * @date 2018/2/27
 * 请求标识 & 携带数据 & 缓存key
 */
public class ReqTag {

    /**
     * 请求标识，在{@link IResponseCallback}回调中返回，并且会作为observer缓存的关键
     */
    private int tag;
    /**
     * 经过整个请求流程的携带数据
     */
    private Object object;

    public ReqTag() {

    }

    public ReqTag(int tag) {
        this.tag = tag;
    }

    public ReqTag(Object object) {
        this.object = object;
    }

    public ReqTag(int tag, Object object) {
        this.tag = tag;
        this.object = object;
    }

    public int getTag() {
        return tag;
    }

    public ReqTag setTag(int tag) {
        this.tag = tag;
        return this;
    }

    public Object getObject() {
        return object;
    }

    public ReqTag setObject(Object object) {
        this.object = object;
        return this;
    }

    /**
     * 如果cacheKey不为空则优先执行缓存逻辑
     */
    private String cacheKey;

    public String getCacheKey() {
        return cacheKey;
    }

    public void setCacheKey(String cacheKey) {
        this.cacheKey = cacheKey;
    }

    /**
     * 允许外部使用默认的Observer
     */
    private boolean useDefaultObserver;

    public boolean isUseDefaultObserver() {
        return useDefaultObserver;
    }

    public void setUseDefaultObserver(boolean useDefaultObserver) {
        this.useDefaultObserver = useDefaultObserver;
    }
}
