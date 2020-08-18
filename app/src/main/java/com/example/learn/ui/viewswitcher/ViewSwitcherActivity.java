package com.example.learn.ui.viewswitcher;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ViewSwitcher;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.learn.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fqxyi
 * @date 2020/8/18
 * ViewSwitcher使用示例
 */
public class ViewSwitcherActivity extends AppCompatActivity {

    private int index = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_switcher);
        testViewSwitcher();
    }

    private void testViewSwitcher() {
        final ViewSwitcher viewSwitcher = (ViewSwitcher) findViewById(R.id.view_switcher);
        viewSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                return new ViewSwitcherItemView(ViewSwitcherActivity.this);
            }
        });
        viewSwitcher.setInAnimation(this, R.anim.view_switcher_slide_in);
        viewSwitcher.setOutAnimation(this, R.anim.view_switcher_slide_out);

        final List<ViewSwitcherBean> viewSwitcherBeanList = new ArrayList<>();

        for (int i = 0; i < 1000; i++) {
            ViewSwitcherBean viewSwitcherBean = new ViewSwitcherBean();
            viewSwitcherBean.title = "title " + i;
            viewSwitcherBean.content = "content " + i;
            viewSwitcherBeanList.add(viewSwitcherBean);
        }

        ViewSwitcherItemView viewSwitcherItemView = (ViewSwitcherItemView) viewSwitcher.getNextView();
        viewSwitcherItemView.initData(viewSwitcherBeanList.get(index++ % viewSwitcherBeanList.size()));
        viewSwitcher.showNext();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ViewSwitcherItemView viewSwitcherItemView = (ViewSwitcherItemView) viewSwitcher.getNextView();
                viewSwitcherItemView.initData(viewSwitcherBeanList.get(index++ % viewSwitcherBeanList.size()));

                if (index >= viewSwitcherBeanList.size()) {
                    index = 0;
                }

                viewSwitcher.showNext();

                handler.postDelayed(this, 2000);
            }
        }, 2000);
    }
}
