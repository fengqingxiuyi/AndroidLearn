package com.example.learn.ui.appbarlayout;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AppbarLayoutRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<String> list = new ArrayList<>();

    Context context;

    public AppbarLayoutRecyclerAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(new TextView(context));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder holder2 = (ViewHolder) holder;
        holder2.textView.setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void updateData() {
        list.clear();
        list.add("111111111");
        list.add("22222222");
        list.add("33333333");
        list.add("4444444444");
        list.add("55555555");
        list.add("66666666");
        list.add("77777777");
        list.add("88888888");
        list.add("111111111");
        list.add("22222222");
        list.add("33333333");
        list.add("4444444444");
        list.add("55555555");
        list.add("66666666");
        list.add("77777777");
        list.add("88888888");
        list.add("111111111");
        list.add("22222222");
        list.add("33333333");
        list.add("4444444444");
        list.add("55555555");
        list.add("66666666");
        list.add("77777777");
        list.add("88888888");
        list.add("111111111");
        list.add("22222222");
        list.add("33333333");
        list.add("4444444444");
        list.add("55555555");
        list.add("66666666");
        list.add("77777777");
        list.add("88888888");
        list.add("111111111");
        list.add("22222222");
        list.add("33333333");
        list.add("4444444444");
        list.add("55555555");
        list.add("66666666");
        list.add("77777777");
        list.add("88888888");
        list.add("111111111");
        list.add("22222222");
        list.add("33333333");
        list.add("4444444444");
        list.add("55555555");
        list.add("66666666");
        list.add("77777777");
        list.add("88888888");
        list.add("111111111");
        list.add("22222222");
        list.add("33333333");
        list.add("4444444444");
        list.add("55555555");
        list.add("66666666");
        list.add("77777777");
        list.add("88888888");
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = (TextView) itemView;
        }
    }

}
