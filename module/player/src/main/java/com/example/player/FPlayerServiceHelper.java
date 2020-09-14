package com.example.player;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;

import com.example.utils.LogUtil;

/**
 * 多进程交互帮助类
 */
public class FPlayerServiceHelper {

    private static final String TAG = "FPlayerServiceHelper";

    /**
     * 单例
     **/

    private FPlayerServiceHelper() {

    }

    private static class FPlayerServiceHelperHolder {
        private static final FPlayerServiceHelper INSTANCE = new FPlayerServiceHelper();
    }

    public static FPlayerServiceHelper get() {
        return FPlayerServiceHelperHolder.INSTANCE;
    }

    /**
     * 成员变量
     **/
    //binder
    private IFPlayerBinder binder;
    //service
    private Intent serviceIntent;
    //接口回调
    private IFPlayer iFPlayer;
    /**
     * 初始化需要的参数
     */
    private String url;
    private long maxCacheSize;
    private long maxFileSize;

    /*** 设置初始化需要的参数 start ***/

    public FPlayerServiceHelper setUrl(String url) {
        this.url = url;
        return this;
    }

    public FPlayerServiceHelper setMaxCacheSize(long maxCacheSize) {
        this.maxCacheSize = maxCacheSize;
        return this;
    }

    public FPlayerServiceHelper setMaxFileSize(long maxFileSize) {
        this.maxFileSize = maxFileSize;
        return this;
    }

    /*** 设置初始化需要的参数 end ***/

    /*** Service操作行为 start ***/

    /**
     * 开启Service
     */
    public FPlayerServiceHelper startService(Activity activity) {
        if (activity == null) {
            LogUtil.e(TAG, "activity is null");
            return this;
        }
        if (TextUtils.isEmpty(url)) {
            LogUtil.e(TAG, "url is empty");
            return this;
        }
        if (serviceIntent == null) {
            serviceIntent = new Intent();
            serviceIntent.setAction("com.fqxyi.player.service");
            serviceIntent.setPackage(activity.getPackageName());
            serviceIntent.putExtra("url", url);
            serviceIntent.putExtra("maxCacheSize", maxCacheSize);
            serviceIntent.putExtra("maxFileSize", maxFileSize);
            activity.startService(serviceIntent);
        }
        return this;
    }

    /**
     * 停止Service
     */
    public FPlayerServiceHelper stopService(Activity activity) {
        if (activity == null || serviceIntent == null) {
            return this;
        }
        activity.stopService(serviceIntent);
        return this;
    }

    /**
     * 绑定Service
     */
    public FPlayerServiceHelper bindService(Activity activity) {
        if (activity == null || serviceIntent == null || conn == null) {
            return this;
        }
        activity.bindService(serviceIntent, conn, Context.BIND_AUTO_CREATE);
        return this;
    }

    /**
     * 解绑Service
     */
    public FPlayerServiceHelper unbindService(Activity activity) {
        if (activity == null || conn == null) {
            return this;
        }
        activity.unbindService(conn);
        return this;
    }

    /*** Service操作行为 end ***/

    /*** Binder接口设置 start ***/

    public FPlayerServiceHelper setCallback(IFPlayer iFPlayer) {
        this.iFPlayer = iFPlayer;
        return this;
    }

    public FPlayerServiceHelper setEnableBufferOnMobile(boolean enableBufferOnMobile) {
        try {
            if (binder != null) {
                binder.setEnableBufferOnMobile(enableBufferOnMobile);
            }
        } catch (RemoteException e) {
            LogUtil.e(e);
        }
        return this;
    }

    public int getCurrentPosition() {
        try {
            if (binder != null) {
                return binder.getCurrentPosition();
            }
        } catch (RemoteException e) {
            LogUtil.e(e);
        }
        return 0;
    }

    public int getBufferedPosition() {
        try {
            if (binder != null) {
                return binder.getBufferedPosition();
            }
        } catch (RemoteException e) {
            LogUtil.e(e);
        }
        return 0;
    }

    public int getDuration() {
        try {
            if (binder != null) {
                return binder.getDuration();
            }
        } catch (RemoteException e) {
            LogUtil.e(e);
        }
        return 0;
    }

    public boolean isPlaying() {
        try {
            if (binder != null) {
                return binder.isPlaying();
            }
        } catch (RemoteException e) {
            LogUtil.e(e);
        }
        return false;
    }

    public FPlayerServiceHelper play() {
        try {
            if (binder != null) {
                binder.play();
            }
        } catch (RemoteException e) {
            LogUtil.e(e);
        }
        return this;
    }

    public FPlayerServiceHelper pause() {
        try {
            if (binder != null) {
                binder.pause();
            }
        } catch (RemoteException e) {
            LogUtil.e(e);
        }
        return this;
    }

    public FPlayerServiceHelper seekTo(long positionMs) {
        try {
            if (binder != null) {
                binder.seekTo(positionMs);
            }
        } catch (RemoteException e) {
            LogUtil.e(e);
        }
        return this;
    }

    public FPlayerServiceHelper release() {
        try {
            if (binder != null) {
                binder.release();
            }
        } catch (RemoteException e) {
            LogUtil.e(e);
        }
        return this;
    }

    /*** Binder接口设置 end ***/

    private ServiceConnection conn = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = IFPlayerBinder.Stub.asInterface(service);
            try {
                if (binder != null) {
                    binder.setCallback(iFPlayer);
                }
            } catch (RemoteException e) {
                LogUtil.e(e);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            binder = null;
        }

    };

}
