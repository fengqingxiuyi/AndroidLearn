package com.example.utils.device;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.annotation.ColorInt;
import androidx.core.view.ViewCompat;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author fqxyi
 * @date 2020/8/20
 * 设备相关工具类：屏幕大小、状态栏等
 */
public class DeviceUtil {

    private static int screenWidth;
    private static int screenHeight;

    private static final String TAG_FAKE_STATUS_BAR_VIEW = "TAG_FAKE_STATUS_BAR_VIEW";
    private static final String TAG_MARGIN_ADDED = "TAG_MARGIN_ADDED";
    private static int statusBarColor;

    /**
     * 获取设备屏幕宽度
     */
    public static int getScreenWidth(Context context) {
        if (screenWidth > 0) {
            return screenWidth;
        }
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point point = new Point();
        wm.getDefaultDisplay().getSize(point);
        screenWidth = point.x;
        screenHeight = point.y;
        return screenWidth;
    }

    /**
     * 获取设备屏幕高度
     */
    public static int getScreenHeight(Context context) {
        if (screenHeight > 0) {
            return screenHeight;
        }
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point point = new Point();
        wm.getDefaultDisplay().getSize(point);
        screenWidth = point.x;
        screenHeight = point.y;
        return screenHeight;
    }

    /**
     * 获取虚拟功能键高度
     */
    public static int getVirtualBarHeight(Context context) {
        int vh = 0;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        try {
            @SuppressWarnings("rawtypes")
            Class c = Class.forName("android.view.Display");
            @SuppressWarnings("unchecked")
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, dm);
            vh = dm.heightPixels - windowManager.getDefaultDisplay().getHeight();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vh;
    }

    /**
     * 获取虚拟功能键高度
     */
    public static int getVirtualBarHeight(Activity activity) {
        int titleHeight = 0;
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusHeight = frame.top;
        titleHeight = activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop() - statusHeight;
        return titleHeight;
    }

    /**
     * 判断指定包名的应用是否安装
     *
     * @param pkgName 应用包名
     */
    public static boolean isPkgInstalled(Context context, String pkgName) {
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(pkgName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        return packageInfo != null;
    }

    /**
     * 获取设备基础信息，默认换行符为\n\n
     */
    public static String getBuildInfo() {
        return getBuildInfo("\n\n");
    }

    /**
     * 获取设备基础信息
     *
     * @param lineBreak 换行符
     */
    public static String getBuildInfo(String lineBreak) {
        return String.valueOf(
                "手机品牌: " + Build.BRAND + lineBreak +
                        "手机型号: " + Build.MODEL + lineBreak +
                        "SDK版本: " + Build.VERSION.SDK_INT + lineBreak +
                        "手机系统版本: " + Build.VERSION.INCREMENTAL + lineBreak +
                        "Android系统版本: " + Build.VERSION.RELEASE
        );
    }

    /**
     * 获取微信版本号
     */
    public static String getWeiXinVersionName(Context context) {
        String versionName = "获取微信版本号失败";
        try {
            PackageManager packageManager = context.getPackageManager();
            List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
            for (PackageInfo pInfo : packageInfos) {
                if ("com.tencent.mm".equals(pInfo.packageName)) { // 获取微信版本号
                    versionName = "微信版本号: " + pInfo.versionName;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * 设置指定Activity的状态栏的颜色
     */
    public static void setStatusBarColor(Activity activity, @ColorInt int color) {
        if (activity == null) {
            return;
        }
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                activity.getWindow().setStatusBarColor(color);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                setStatusBarColorBelow21(activity, color);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置指定Activity的状态栏透明
     */
    public static void setStatusBarTransparent(Activity activity, boolean transparent) {
        if (transparent) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = activity.getWindow();
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                        | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION  // 该参数指布局能延伸到navigationbar，我们场景中不应加这个参数
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                statusBarColor = window.getStatusBarColor();
                window.setStatusBarColor(Color.TRANSPARENT);
//                window.setNavigationBarColor(Color.TRANSPARENT); // 设置navigationbar颜色为透明
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                WindowManager.LayoutParams localLayoutParams = activity.getWindow().getAttributes();
                localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = activity.getWindow();
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                        | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                window.setStatusBarColor(statusBarColor);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
        }
    }

    /**
     * 获取状态栏的高度，默认值为50
     */
    public static int getStatusBarHeight(Context context) {
        int statusHeight;
        try {
            int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
            statusHeight = context.getResources().getDimensionPixelSize(resourceId);

        } catch (Exception e) {
            statusHeight = 50;
        }
        return statusHeight;
    }

    private static void setStatusBarColorBelow21(Activity activity, int statusColor) {
        Window window = activity.getWindow();
        //设置Window为全透明
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        ViewGroup mContentView = (ViewGroup) window.findViewById(Window.ID_ANDROID_CONTENT);
        //获取父布局
        View mContentChild = mContentView.getChildAt(0);
        //获取状态栏高度
        int statusBarHeight = getStatusBarHeight(activity);

        //如果已经存在假状态栏则移除，防止重复添加
        removeFakeStatusBarViewIfExist(activity);
        //添加一个View来作为状态栏的填充
        addFakeStatusBarView(activity, statusColor, statusBarHeight);
        //设置子控件到状态栏的间距
        addMarginTopToContentChild(mContentChild, statusBarHeight);
        //不预留系统栏位置
        if (mContentChild != null) {
            ViewCompat.setFitsSystemWindows(mContentChild, false);
        }
        //如果在Activity中使用了ActionBar则需要再将布局与状态栏的高度跳高一个ActionBar的高度，否则内容会被ActionBar遮挡
        int action_bar_id = activity.getResources().getIdentifier("action_bar", "id", activity.getPackageName());
        View view = activity.findViewById(action_bar_id);
        if (view != null) {
            TypedValue typedValue = new TypedValue();
            if (activity.getTheme().resolveAttribute(android.R.attr.actionBarSize, typedValue, true)) {
                int actionBarHeight = TypedValue.complexToDimensionPixelSize(typedValue.data, activity.getResources().getDisplayMetrics());
                setContentTopPadding(activity, actionBarHeight);
            }
        }
    }

    private static void removeFakeStatusBarViewIfExist(Activity activity) {
        Window window = activity.getWindow();
        ViewGroup mDecorView = (ViewGroup) window.getDecorView();

        View fakeView = mDecorView.findViewWithTag(TAG_FAKE_STATUS_BAR_VIEW);
        if (fakeView != null) {
            mDecorView.removeView(fakeView);
        }
    }

    private static View addFakeStatusBarView(Activity activity, int statusBarColor, int statusBarHeight) {
        Window window = activity.getWindow();
        ViewGroup mDecorView = (ViewGroup) window.getDecorView();

        View mStatusBarView = new View(activity);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight);
        layoutParams.gravity = Gravity.TOP;
        mStatusBarView.setLayoutParams(layoutParams);
        mStatusBarView.setBackgroundColor(statusBarColor);
        mStatusBarView.setTag(TAG_FAKE_STATUS_BAR_VIEW);

        mDecorView.addView(mStatusBarView);
        return mStatusBarView;
    }

    private static void addMarginTopToContentChild(View mContentChild, int statusBarHeight) {
        if (mContentChild == null) {
            return;
        }
        if (!TAG_MARGIN_ADDED.equals(mContentChild.getTag())) {
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mContentChild.getLayoutParams();
            lp.topMargin += statusBarHeight;
            mContentChild.setLayoutParams(lp);
            mContentChild.setTag(TAG_MARGIN_ADDED);
        }
    }

    private static void setContentTopPadding(Activity activity, int padding) {
        ViewGroup mContentView = (ViewGroup) activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT);
        mContentView.setPadding(0, padding, 0, 0);
    }

}
