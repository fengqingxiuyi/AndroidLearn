package com.example.learn.jetpack.viewmodel.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.common.base.BaseViewModelFragment;
import com.example.learn.R;
import com.example.learn.jetpack.viewmodel.observer.FragmentLifecycleObserver;
import com.example.learn.jetpack.viewmodel.viewmodel.MainViewModel;
import com.example.network.bean.ErrorBean;

/**
 * View层
 * 只负责显示UI，不执行数据处理逻辑
 */
public class ViewModelFragment extends BaseViewModelFragment<MainViewModel> {

    //findView
    private Button mainFragmentBtn;
    private TextView mainFragmentText;

    @Override
    protected View initView(LayoutInflater inflater, @Nullable ViewGroup container) {
        View rootView = inflater.inflate(R.layout.fragment_viewmodel, container, false);
        //findView
        mainFragmentBtn = (Button) rootView.findViewById(R.id.main_fragment_btn);
        mainFragmentText = (TextView) rootView.findViewById(R.id.main_fragment_text);
        //
        initEvent();
        initObserver();
        initLifecycle();
        return rootView;
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {

    }

    private void initEvent() {
        mainFragmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mViewModel == null) {
                    return;
                }
                mViewModel.loadData();
            }
        });
    }

    private void initObserver() {
        if (mViewModel == null) {
            return;
        }
        mViewModel.getResponseData().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                mainFragmentText.setText(s);
            }
        });
        mViewModel.getErrorData().observe(this, new Observer<ErrorBean>() {
            @Override
            public void onChanged(ErrorBean errorBean) {
                mainFragmentText.setText(errorBean.msg);
            }
        });
    }

    public void initLifecycle() {
        getLifecycle().addObserver(new FragmentLifecycleObserver(appContext));
    }

    @Override
    protected MainViewModel getViewModel() {
        if (mActivity == null) {
            return null;
        }
        return ViewModelProviders.of(mActivity).get(MainViewModel.class);
    }
}
