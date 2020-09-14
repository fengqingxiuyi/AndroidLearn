package com.example.learn.jetpack.viewmodel.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.common.base.BaseViewModelActivity;
import com.example.learn.R;
import com.example.learn.jetpack.viewmodel.observer.ActivityLifecycleObserver;
import com.example.learn.jetpack.viewmodel.viewmodel.MainViewModel;

/**
 * View层
 * 只负责显示UI，不执行数据处理逻辑
 */
public class ViewModelActivity extends BaseViewModelActivity<MainViewModel> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewmodel);
        initFragment();
        initLifecycle();
    }

    private void initFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        Fragment fragment = new ViewModelFragment();

        Bundle bundle = getIntent().getExtras();
        if (null == bundle) {
            bundle = new Bundle();
        }

        Bundle fragmentArguments = fragment.getArguments();
        if (null != fragmentArguments) {
            bundle.putAll(fragmentArguments);
        }

        fragment.setArguments(bundle);

        transaction.add(R.id.main_activity_root, fragment, "TestViewModelActivity");

        transaction.commitAllowingStateLoss();
    }

    public void initLifecycle() {
        getLifecycle().addObserver(new ActivityLifecycleObserver(getApplicationContext()));
    }

    @Override
    protected MainViewModel getViewModel() {
        return null;
    }
}
