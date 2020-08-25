package com.example.webview_module.js;

import android.content.Context;
import android.text.TextUtils;

import com.example.utils.LogUtil;
import com.example.webview_module.IWebviewBinderCallback;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 解析给web调用的接口类，根据web调用传过来的方法名、参数等信息，调用这里解析出来的方法
 * 这种实现是为了不需要改动底层真正给web调用的接口{@link JsInterface}
 * 只需要上层通过 JsBridge.get().register(JsInterfaceImpl.class)
 *
 * 主进程中执行
 *  
 */
public class JsBridge {

    private static volatile JsBridge mInstance;

    private JsBridge() {
    }

    public static JsBridge get() {
        if (mInstance == null) {
            synchronized (JsBridge.class) {
                if (mInstance == null) {
                    mInstance = new JsBridge();
                }
            }
        }
        return mInstance;
    }

    //clazz的实例
    private Object object;
    //key：方法名 value：方法对象Method
    private Map<String, Method> mMethodMp = new HashMap<>();

    /**
     * 解析提供给h5调用的方法所在的类，把该类的所有方法解析，并以键值对的形式放到 mMethodMp 中
     *   1、该类中的方法不允许重名，否则后面的方法会覆盖前面的方法
     *   2、方法的参数为String
     *
     * @param clazz 提供给h5调用的方法所在的类
     */
    public void register(Class clazz) {
        try {
            this.object = clazz.newInstance();
            mMethodMp.clear();
            parseMethods(clazz);
        } catch (Exception e) {
            LogUtil.e(e);
        }
    }

    private void parseMethods(Class injectedCls) throws Exception {
        Method[] methods = injectedCls.getDeclaredMethods();
        Method[] methods2 = injectedCls.getSuperclass().getDeclaredMethods();
        appendMethodsToMap(methods, mMethodMp);
        appendMethodsToMap(methods2, mMethodMp);
    }

    private void appendMethodsToMap(Method[] methods, Map<String, Method> mMethodsMap) {
        for (Method method : methods) {
            String name = method.getName();
            if (TextUtils.isEmpty(name)) {
                continue;
            }
            mMethodsMap.put(name, method);
        }
    }

    /**
     * 只能在主进程中调用该函数
     */
    public void callJava(String methodName, Context context, String params) {
        callJava(methodName, context, params, null);
    }

    /**
     * 只能在主进程中调用该函数，根据网页调用的方法名、参数，通过反射调用注册的客户端方法
     */
    public void callJava(String methodName, Context context, String params, IWebviewBinderCallback callback) {
        if (mMethodMp.containsKey(methodName)) {
            Method method = mMethodMp.get(methodName);
            if (method != null) {
                try {
                    method.invoke(object, context, params, callback);
                } catch (Exception e) {
                    LogUtil.e(e);
                }
            } else {
                LogUtil.e("WebviewComponent", "Android侧没有定义该方法，请检查接口参数名称是否有误！");
            }
        } else {
            LogUtil.e("WebviewComponent", "Android侧没有定义接口[" + methodName + "]，请检查接口参数名称是否有误！");
        }
    }

}
