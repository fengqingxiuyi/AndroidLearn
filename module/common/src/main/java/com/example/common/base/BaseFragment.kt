package com.example.common.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.network.RequestManager;

/**
 * 不需要Presenter时继承他
 */
public abstract class BaseFragment extends Fragment {

    protected BaseActivity activity;
    protected Context appContext;
    //loading
    private View loadingView;

    private View rootView;
    private boolean isFirstVisible = true;
    private boolean isFirstInVisible = true;
    //判断第一次有效载入fragment
    private boolean isActivityCreated;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        appContext = context.getApplicationContext();
        if (context instanceof BaseActivity) {
            activity = (BaseActivity) context;
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initPresenter();
        initData(savedInstanceState);
        isActivityCreated = true;
        judgeIsFirstVisible();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = initView(inflater, container);
        return rootView;
    }

    public void addLoadingView(View view, FrameLayout.LayoutParams params) {
        if (rootView == null) return;
        loadingView = view;
        ((ViewGroup) rootView).addView(view, params);
    }

    public void removeLoadingView() {
        if (loadingView != null && loadingView.getParent() != null) {
            ((ViewGroup) loadingView.getParent()).removeView(loadingView);
            loadingView = null;
        }
    }

    public View getLoadingView() {
        return loadingView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isFirstVisible) {
            isFirstVisible = false;
        } else {
            onReUserVisible();
        }
    }

    @Override
    public void onDestroy() {
        RequestManager.get().destroy(this);
        super.onDestroy();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            onReUserVisible();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        judgeIsFirstVisible();
        if (getUserVisibleHint()) {
            if (isFirstVisible) {
                isFirstVisible = false;
            } else {
                onHiddenChanged(false);
            }
        } else {
            if (isFirstInVisible) {
                isFirstInVisible = false;
            } else {
                onHiddenChanged(true);
            }
        }
    }

    /**
     * 判断是否第一次可见
     */
    private void judgeIsFirstVisible() {
        if (isActivityCreated && getUserVisibleHint()) {
            isActivityCreated = false;
            onFirstUserVisible();
        }
    }

    public boolean isFirstVisible() {
        return isFirstVisible;
    }

    /**
     * 第一次Fragment可见（进行初始化工作）
     */
    protected void onFirstUserVisible() {

    }

    /**
     * 除第一次见到Fragment外，之后每次看见Fragment都会执行
     */
    protected void onReUserVisible() {

    }

    /**
     * 初始化Presenter
     */
    protected void initPresenter() {

    }

    /**
     * 初始化Presenter的attach方法
     */
    protected void initPresenterAttach() {

    }

    /**
     * 初始化控件
     */
    protected abstract View initView(LayoutInflater inflater, ViewGroup container);

    /**
     * 初始化数据
     */
    protected abstract void initData(Bundle savedInstanceState);
}
