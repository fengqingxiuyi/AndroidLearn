package com.example.common.loading;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.common.R;
import com.example.common.base.BaseFragment;
import com.example.common.global.AppGlobal;

/**
 * 显示隐藏Loading工具类
 */
public class LoadingUtil {

    public static void showLoading(ViewGroup viewGroup) {
        if (viewGroup == null) return;
        if (null != viewGroup.findViewById(R.id.loading_id)) {
            View loadingView = viewGroup.findViewById(R.id.loading_id);
            if (loadingView != null && loadingView.getVisibility() != View.VISIBLE) {
                loadingView.setVisibility(View.VISIBLE);
            }
        } else {
            View view = View.inflate(AppGlobal.appContext, R.layout.loading_view, null);
            view.setId(R.id.loading_id);
            viewGroup.addView(view);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT);
            view.setLayoutParams(params);
        }
    }

    public static void hideLoading(ViewGroup viewGroup) {
        if (viewGroup == null) return;
        if (null != viewGroup.findViewById(R.id.loading_id)) {
            View loadingView = viewGroup.findViewById(R.id.loading_id);
            if (loadingView != null && loadingView.getVisibility() != View.GONE) {
                loadingView.setVisibility(View.GONE);
            }
        }
    }

    public static void showLoading(Context context){
        if(context != null && context instanceof Activity){
            showLoading((Activity)context);
        }
    }

    public static void showLoading(Activity activity) {
        if (activity == null || activity.getWindow() == null) {
            return;
        }
        View decorView = activity.getWindow().getDecorView();
        if (!(decorView instanceof ViewGroup)) {
            return;
        }
        ViewGroup viewGroup = (ViewGroup) decorView;
        showLoading(viewGroup);
    }

    public static void hideLoading(Context context){
        if(context != null && context instanceof Activity){
            hideLoading((Activity)context);
        }
    }

    public static void hideLoading(Activity activity) {
        if (activity == null || activity.getWindow() == null) {
            return;
        }
        View decorView = activity.getWindow().getDecorView();
        if (!(decorView instanceof ViewGroup)) {
            return;
        }
        ViewGroup viewGroup = (ViewGroup) decorView;
        hideLoading(viewGroup);
    }

    public static void showLoading(BaseFragment fragment) {
        if (fragment == null || fragment.getActivity() == null || fragment.getActivity().getApplicationContext() == null) {
            return;
        }
        if (fragment.getLoadingView() == null) {
            View view = View.inflate(fragment.getActivity().getApplicationContext(), R.layout.loading_view, null);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT);
            fragment.addLoadingView(view, params);
        }
    }

    public static void hideLoading(BaseFragment fragment) {
        if (fragment == null) {
            return;
        }
        fragment.removeLoadingView();
    }

}
