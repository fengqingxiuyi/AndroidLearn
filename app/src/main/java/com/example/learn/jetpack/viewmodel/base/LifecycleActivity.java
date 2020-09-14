package com.example.learn.jetpack.viewmodel.base;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleRegistry;

/**
 * Activity基类
 */
public class LifecycleActivity extends FragmentActivity {

    private LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(this);

    @NonNull
    @Override
    public LifecycleRegistry getLifecycle() {
        return lifecycleRegistry;
    }
}
