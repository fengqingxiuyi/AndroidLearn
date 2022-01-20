package com.example.utils.network

import android.annotation.TargetApi
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkInfo
import android.net.NetworkRequest
import android.os.Build
import android.telephony.TelephonyManager
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.utils.other.Utils

object NetworkUtils {

    private val netReceiver: NetworkReceiver by lazy { NetworkReceiver() }
    val netStatusLiveData =
        object : MutableLiveData<NetStatus>() {
            override fun removeObserver(observer: Observer<in NetStatus>) {
                super.removeObserver(observer)
                if (!hasObservers()) {
                    stopObs()
                }
            }

            override fun observe(owner: LifecycleOwner, observer: Observer<in NetStatus>) {
                if (!hasObservers()) {
                    startObs()
                }
                super.observe(owner, observer)
            }

            override fun observeForever(observer: Observer<in NetStatus>) {
                if (!hasObservers()) {
                    startObs()
                }
                super.observeForever(observer)
            }
        }

    private val netCallback: ConnectivityManager.NetworkCallback by lazy {
        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                netStatusLiveData.postValue(NetStatus(true, netType()))
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                netStatusLiveData.postValue(NetStatus(false))
            }
        }
    }


    fun isConnected(): Boolean {
        return activeNetworkInfo()?.isConnected ?: false
    }

    fun netType(): NetworkType {
        return netType(activeNetworkInfo())
    }

    fun isWifiConnected(): Boolean {
        return netType() == NetworkType.NETWORK_WIFI
    }

    fun networkOperatorName(): String {
        return telephonyManager()?.networkOperatorName ?: ""
    }

    private fun activeNetworkInfo(): NetworkInfo? {
        return connectivityManager()?.activeNetworkInfo
    }

    private fun connectivityManager(): ConnectivityManager? {
        val connectivityManager = Utils.context.getSystemService(Context.CONNECTIVITY_SERVICE)
        return if (connectivityManager is ConnectivityManager) {
            connectivityManager
        } else null
    }

    private fun telephonyManager(): TelephonyManager? {
        val tm = Utils.context.getSystemService(Context.TELEPHONY_SERVICE)
        return if (tm is TelephonyManager) tm else null
    }

    private fun netType(networkInfo: NetworkInfo?): NetworkType {
        if (networkInfo == null) return NetworkType.NETWORK_NO
        if (networkInfo.type == ConnectivityManager.TYPE_WIFI)
            return NetworkType.NETWORK_WIFI
        if (networkInfo.type == ConnectivityManager.TYPE_MOBILE) {
            return when (networkInfo.subtype) {
                TelephonyManager.NETWORK_TYPE_GSM,
                TelephonyManager.NETWORK_TYPE_GPRS,
                TelephonyManager.NETWORK_TYPE_CDMA,
                TelephonyManager.NETWORK_TYPE_EDGE,
                TelephonyManager.NETWORK_TYPE_1xRTT,
                TelephonyManager.NETWORK_TYPE_IDEN -> {
                  NetworkType.NETWORK_2G
                }
                TelephonyManager.NETWORK_TYPE_TD_SCDMA,
                TelephonyManager.NETWORK_TYPE_EVDO_A,
                TelephonyManager.NETWORK_TYPE_UMTS,
                TelephonyManager.NETWORK_TYPE_EVDO_0,
                TelephonyManager.NETWORK_TYPE_HSDPA,
                TelephonyManager.NETWORK_TYPE_HSUPA,
                TelephonyManager.NETWORK_TYPE_HSPA,
                TelephonyManager.NETWORK_TYPE_EVDO_B,
                TelephonyManager.NETWORK_TYPE_EHRPD,
                TelephonyManager.NETWORK_TYPE_HSPAP -> {
                  NetworkType.NETWORK_3G
                }
                TelephonyManager.NETWORK_TYPE_IWLAN,
                TelephonyManager.NETWORK_TYPE_LTE -> {
                  NetworkType.NETWORK_4G
                }
                TelephonyManager.NETWORK_TYPE_NR -> {
                  NetworkType.NETWORK_5G
                }
                else -> {
                    val subtypeName = networkInfo.subtypeName
                    if (subtypeName.equals("TD-SCDMA", true)
                        || subtypeName.equals("WCDMA", true)
                        || subtypeName.equals("CDMA2000", true)
                    ) {
                      NetworkType.NETWORK_3G
                    } else {
                      NetworkType.NETWORK_UNKNOWN
                    }
                }
            }
        } else {
            return NetworkType.NETWORK_UNKNOWN
        }
        return NetworkType.NETWORK_NO
    }

    fun startObs() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            registerNetReceiver()
        } else {
            val networkRequest = NetworkRequest.Builder().build()
            connectivityManager()?.registerNetworkCallback(
                networkRequest,
                netCallback
            )
        }
    }

    fun stopObs() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            unregisterNetReceiver()
        } else {
            // 部分ROM对ConnectivityManager的unregisterNetworkCallback进行了修改，如果已取消注册再调用次方法抛出异常
            try {
                connectivityManager()?.unregisterNetworkCallback(netCallback)
            } catch (e: IllegalArgumentException) {
                Log.d("NetworkUtils", "NetworkCallback was already unregistered")
            }
        }
    }

    private fun registerNetReceiver() {
        val intentFilter = IntentFilter()
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE")
        intentFilter.addAction("android.net.ethernet.ETHERNET_STATE_CHANGED")
        intentFilter.addAction("android.net.ethernet.STATE_CHANGE")
        intentFilter.addAction("android.net.wifi.WIFI_STATE_CHANGED")
        intentFilter.addAction("android.net.wifi.STATE_CHANGE")
        Utils.context.registerReceiver(netReceiver, intentFilter)
    }

    private fun unregisterNetReceiver() {
        Utils.context.unregisterReceiver(netReceiver)
    }

    /**
     * 判断是否有Sim卡
     */
    fun hasSim(): Boolean {
        val tm =
            Utils.context.getSystemService(Context.TELEPHONY_SERVICE)
        if (tm is TelephonyManager) {
            return tm.simState == TelephonyManager.SIM_STATE_READY
        }
        return false
    }

    @Suppress("DEPRECATION")
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    class NetworkReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context?, intent: Intent?) {

        }
    }

    class NetStatus(val isConnected: Boolean, val netType: NetworkType = NetworkType.NETWORK_NO)

    enum class NetworkType {
        NETWORK_WIFI, NETWORK_5G, NETWORK_4G, NETWORK_3G, NETWORK_2G, NETWORK_UNKNOWN, NETWORK_NO
    }

}