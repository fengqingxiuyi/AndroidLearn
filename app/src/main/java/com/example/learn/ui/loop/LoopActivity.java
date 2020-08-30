package com.example.learn.ui.loop;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.learn.R;
import com.example.ui.loop.IItemView;
import com.example.ui.loop.RollLoopView;
import com.example.ui.updown.UpDownTextView;

import java.util.ArrayList;
import java.util.List;

public class LoopActivity extends AppCompatActivity {

    private RollLoopView viewRollLoop;
    private RollLoopView viewRollLoopSub;
    private UpDownTextView viewUpDown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loop);

        viewRollLoop = (RollLoopView) findViewById(R.id.view_roll_loop);
        viewRollLoopSub = (RollLoopView) findViewById(R.id.view_roll_loop_sub);
        viewUpDown = (UpDownTextView) findViewById(R.id.view_up_down);

        final List<String> dataList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            dataList.add("----------"+i+"----------");
        }
        final List<String> dataList2 = new ArrayList<>();
        for (int i = 5; i < 10; i++) {
            dataList2.add("----------"+i+"----------");
        }
        viewRollLoop.start(1000, new IItemView() {
            @Override
            public View getItemView() {
                return new LoopItemView(LoopActivity.this);
            }

            @Override
            public List getDataList() {
                return dataList;
            }

            @Override
            public void perform(View view, int position) {
                LoopItemView itemView = (LoopItemView) view;
                itemView.setText(dataList.get(position));
            }
        });
        viewRollLoopSub.start(1000, new IItemView() {
            @Override
            public View getItemView() {
                return new LoopItemView(LoopActivity.this);
            }

            @Override
            public List getDataList() {
                return dataList2;
            }

            @Override
            public void perform(View view, int position) {
                LoopItemView itemView = (LoopItemView) view;
                itemView.setText(dataList2.get(position));
            }
        });

        final List<String> dataList3 = new ArrayList<>();
        for (int i = 0; i < 5000; i++) {
            dataList3.add("----------"+i+"----------");
        }
        viewUpDown.setData(dataList3);
        viewUpDown.startAutoScroll();

    }

}
