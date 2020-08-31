package com.example.learn.ui.network.mvp.view;

import android.os.Bundle;
import android.view.View;

import com.example.common.base.BaseActivity;
import com.example.learn.R;
import com.example.learn.ui.network.mvp.bean.MvpBean;
import com.example.learn.ui.network.mvp.bean.MvpSecondBean;
import com.example.learn.ui.network.mvp.contract.MvpContract;
import com.example.learn.ui.network.mvp.presenter.MvpPresenter;
import com.example.network.bean.ErrorBean;
import com.example.ui.toast.ToastUtil;

/**
 * 视图层
 */
public class MvpActivity extends BaseActivity implements MvpContract.View {

    private MvpContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mvp);
        //
        presenter = new MvpPresenter(this, this);

    }

    public void startRequest(View view) {
        if (presenter != null) {
            presenter.mainRequest();
        }
    }

    public void startSecondRequest(View view) {
        if (presenter != null) {
            presenter.secondRequest();
        }
    }

    @Override
    public void mainResponse(MvpBean response) {
        if (response == null) {
            return;
        }
        ToastUtil.toast(response.key);
    }

    @Override
    public void error(ErrorBean errorBean) {
        ToastUtil.toast(errorBean.toString());
    }

    @Override
    public void secondResponse(MvpSecondBean response) {
        ToastUtil.toast(response.key);
    }

}
