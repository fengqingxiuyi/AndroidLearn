// IFPlayer.aidl
package com.example.player;

// Declare any non-default types here with import statements

interface IFPlayer {

    /**
     * 资源信息
     * @param currentPosition 资源播放进度
     * @param bufferedPosition 资源缓冲进度
     */
    void getPosition(int currentPosition, int bufferedPosition);

    /**
     * 获取资源总时长
     */
    void getDuration(int duration);

    /**
     * 监听资源加载进度
     * @param isLoading true 正在加载资源 false 资源加载完成
     */
    void isLoading(boolean isLoading);

}
