package com.example.learn.ui.partition.partition.one;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OneViewHolder extends RecyclerView.ViewHolder {

    public OneView oneView;

    public OneViewHolder(@NonNull View itemView) {
        super(itemView);
        oneView = (OneView) itemView;
    }
}
