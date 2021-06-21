package com.example.learn.ui.drawer;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.learn.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Random;

public class VerticalDrawerActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        Adapter adapter = new Adapter();
        recyclerView.setAdapter(adapter);

//        ScrollView scrollView = findViewById(R.id.scrollView);

        VerticalDrawerLayout dragLayout = findViewById(R.id.dragLayout);
        dragLayout.setDragHeightFirst(150);
        dragLayout.setDragHeightSecond(900);
        dragLayout.setInterceptScrollView(recyclerView);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
        }
    }

    class Adapter extends RecyclerView.Adapter<ViewHolder> {

        private ArrayList<String> list = new ArrayList<>();

        Adapter() {
            for (int i = 0; i < 100; i++) {
                list.add("111111");
            }
        }

        @NonNull
        @NotNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
            TextView textView = new TextView(VerticalDrawerActivity.this);
            textView.setHeight(100);
            Random random = new Random();
            int r = random.nextInt(256);
            int g = random.nextInt(256);
            int b = random.nextInt(256);
            textView.setBackgroundColor(Color.rgb(r,g,b));
            return new ViewHolder(textView);
        }

        @Override
        public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
            TextView textView = (TextView) holder.itemView;
            textView.setText(list.get(position));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }
}
