package com.example.learn.ui.drawer;

import android.app.Activity;
import android.os.Bundle;

import com.example.learn.R;

public class VerticalDrawerActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        VerticalDrawerLayout dragLayout = findViewById(R.id.dragLayout);
        dragLayout.setDragHeightFirst(150);
        dragLayout.setDragHeightSecond(900);
    }
}
