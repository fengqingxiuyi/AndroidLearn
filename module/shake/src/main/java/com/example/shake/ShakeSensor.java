package com.example.shake;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.util.Log;

/**
 * @author fqxyi
 * @date 2017/1/18
 * 摇一摇的功能实现类，需要用户注册onShareListener来事件回调.
 * 摇一摇传感器，内部原理为加速度传感器，摇一摇到达速度阀值则出发相应的动作，回调onShakeComplete方法
 */
public class ShakeSensor implements SensorListener {

    /**
     * 类的tag
     */
    private final String TAG = ShakeSensor.class.getName();
    /**
     * 速度阈值，当摇晃速度达到这值后产生作用
     */
    public static int mSpeedThreshold = 950;
    /**
     * 第一次检测的时间间隔阈值
     */
    private int mTimeThreshold = 110;
    /**
     * 第二次检测的时间间隔阈值
     */
    private int mSpeedTimeThreshold = 500;
    /**
     * 第三次检测的时间间隔阈值
     */
    private int mFinalTimeThreshold = 1000;
    /**
     * 计数器阈值
     */
    private int mCountThreshold = 4;
    /**
     * 传感器管理器
     */
    private SensorManager mSensorManager;
    /**
     * 手机上一个位置时重力感应坐标
     */
    private float mLastX = -1.0F;
    private float mLastY = -1.0F;
    private float mLastZ = -1.0F;
    /**
     * 上次检测时间
     */
    private long mLastTime;
    /**
     * 传感器监听器, 摇一摇以后回调onShakeComplete方法
     */
    private OnShakeListener mShakeListener;
    /**
     * App Context
     */
    private Context mContext;
    /**
     * 计数器
     */
    private int count = 0;
    /**
     * 第二次上次检测时间
     */
    private long mFinalTime;
    /**
     * 首次上次检测时间
     */
    private long mSpeedTime;

    /**
     * ShakeListener Constructor
     *
     * @param context
     */
    public ShakeSensor(Context context) {
        this(context, mSpeedThreshold);
    }

    /**
     * ShakeListener Constructor
     *
     * @param context
     */
    public ShakeSensor(Context context, int speedThreshold) {
        mContext = context;
        mSpeedThreshold = speedThreshold;
    }

    /**
     * 设置 mShakeListener
     *
     * @param listener 对mShakeListener进行赋值
     */
    public void setShakeListener(OnShakeListener listener) {
        this.mShakeListener = listener;
    }

    /**
     * 注册传感器，返回是否注册成功
     *
     * @return boolean 注册是否成功的标识
     */
    public void register() {
        // 获得传感器管理器
        mSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
        if (mSensorManager != null) {
            // 注册传感器
            boolean isStart = mSensorManager.registerListener(this, Sensor.TYPE_MAGNETIC_FIELD, SensorManager.SENSOR_DELAY_GAME);
            if (!isStart) {
                mSensorManager.unregisterListener(this, Sensor.TYPE_MAGNETIC_FIELD);
            }
        }
    }

    /**
     * 在传感器没有被锁住的情况下,注销传感器，并且清理一些对象和状态
     */
    public void unregister() {
        if (mSensorManager != null) {
            mSensorManager.unregisterListener(this, Sensor.TYPE_MAGNETIC_FIELD);
            mShakeListener = null;
        }
    }

    /**
     * (非 Javadoc)
     *
     * onAccuracyChanged
     *
     * @param sensor
     * @param accuracy
     * @see android.hardware.SensorEventListener#onAccuracyChanged(Sensor, int)
     */
    @Override
    public void onAccuracyChanged(int sensor, int accuracy) {
        Log.d(TAG, " ### onAccuracyChanged, accuracy = " + accuracy);
    }

    /**
     * (非 Javadoc)
     *
     * 重力感应器感应获得变化数据
     *
     * @param sensor
     * @param values
     * @see android.hardware.SensorEventListener#onSensorChanged(android.hardware.SensorEvent)
     */
    public void onSensorChanged(int sensor, float[] values) {
        if (sensor == Sensor.TYPE_MAGNETIC_FIELD) {
            // 现在检测时间
            long currentTime = System.currentTimeMillis();
            if (currentTime - mSpeedTime > (long) mSpeedTimeThreshold) {
                count = 0;
            }

            if (currentTime - mLastTime > (long) mTimeThreshold) {
                float speed = Math.abs(values[0] + values[1] + values[2] - mLastX - mLastY - mLastZ) / (float)(currentTime - mLastTime) * 10000.0F;
                if (speed > (float) mSpeedThreshold) {
                    if(++count >= mCountThreshold && currentTime - mFinalTime > (long) mFinalTimeThreshold) {
                        mFinalTime = currentTime;
                        count = 0;
                        if(mShakeListener != null) {
                            mShakeListener.onShakeComplete();
                        }
                    }

                    // 现在的时间变成首次last时间
                    mSpeedTime = currentTime;
                }

                // 现在的时间变成last时间
                mLastTime = currentTime;
                // 将现在的坐标变成last坐标
                mLastX = values[0];
                mLastY = values[1];
                mLastZ = values[2];
            }

        }

    }

}
