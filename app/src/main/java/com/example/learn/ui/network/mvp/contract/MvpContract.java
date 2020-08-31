package com.example.learn.ui.network.mvp.contract;

import com.example.learn.ui.network.mvp.bean.MvpBean;
import com.example.learn.ui.network.mvp.bean.MvpSecondBean;
import com.example.network.bean.ErrorBean;

/**
 * 契约接口
 */
public interface MvpContract {

    interface View {
        void mainResponse(MvpBean response);

        void error(ErrorBean errorBean);

        void secondResponse(MvpSecondBean response);
    }

    interface Presenter {
        void mainRequest();

        void secondRequest();
    }

}
