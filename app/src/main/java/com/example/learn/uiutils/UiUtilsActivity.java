package com.example.learn.uiutils;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.common.base.BaseActivity;
import com.example.common.ui.titlebar.TitleBarView;
import com.example.learn.R;
import com.example.learn.uiutils.event.TestRxBusSendEvent;
import com.example.utils.activity.ActivityUtil;
import com.example.utils.event.RxBusUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 主界面，列举应用中存在的所有Activity
 */
public class UiUtilsActivity extends BaseActivity {

    private TitleBarView mainTitleBar;

    private Disposable testRxBusSendEventSub;
    private Disposable testRxBusCancelEventSub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uiutils);

        mainTitleBar = (TitleBarView) findViewById(R.id.main_title_bar);
        mainTitleBar.setLeftIconVisible(false);

        // 显示当前包名下的Activities
        showActivities();

        testRxBus();
    }

    /**
     * 展示应用中所有的Activity
     */
    private void showActivities() {
        ListView mainListActivities = (ListView) findViewById(R.id.main_list_activities);
        final List<ActivityInfo> activityInfos = ActivityUtil.getActivityInfos(context);
        // 显示ListView
        List<String> activityNames = new ArrayList<>();
        List<ActivityInfo> newActivityInfos = new ArrayList<>();
        if (activityInfos != null) {
            for (ActivityInfo activityInfo : activityInfos) {
                String name = activityInfo.name.substring(activityInfo.name.lastIndexOf(".") + 1);
                if (name.startsWith("Test")) {
                    activityNames.add(name);
                    newActivityInfos.add(activityInfo);
                }
            }
        }
        mainListActivities.setAdapter(new ArrayAdapter<>(context,
                R.layout.activity_uiutils_item, activityNames));
        // 跳转到指定的Activity页面
        mainListActivities.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if ( newActivityInfos.size() < 1) {
                    return;
                }
                ActivityInfo info = newActivityInfos.get(position);
                if (info == null || info.name == null) {
                    return;
                }
                if (info.name.contains("MainActivity")) { // 禁止点击MainActivity
                    return;
                }
                try {
                    startActivity(new Intent(context, Class.forName(info.name)));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void testRxBus() {
        if (testRxBusSendEventSub == null) {
            testRxBusSendEventSub = RxBusUtil.getInstance().subscribe(TestRxBusSendEvent.class, new Consumer<TestRxBusSendEvent>() {
                @Override
                public void accept(TestRxBusSendEvent testRxBusSendEvent) throws Exception {
                    toast(testRxBusSendEvent.msg);
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {
                    throwable.printStackTrace();
                }
            });
        }
        if (testRxBusCancelEventSub == null) {
            testRxBusCancelEventSub = RxBusUtil.getInstance().subscribe(TestRxBusSendEvent.class, new Consumer<TestRxBusSendEvent>() {
                @Override
                public void accept(TestRxBusSendEvent testRxBusSendEvent) throws Exception {
                    destroySubscription(testRxBusSendEventSub);
                    destroySubscription(testRxBusCancelEventSub);
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) throws Exception {
                    throwable.printStackTrace();
                }
            });
        }
    }

    @Override
    public TitleBarView getTitleBarView() {
        return mainTitleBar;
    }

}
