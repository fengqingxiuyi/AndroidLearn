package com.example.learn.ui.refresh;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.learn.R;

import java.util.List;

/**
 * @author fqxyi
 * @date 2017/05/19
 */
public class RefreshListGridAdapter extends BaseAdapter {

    Context context;
    List<String> data;

    public RefreshListGridAdapter(Context context, List<String> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_refresh_list_grid_item, null);

            holder.textView = (TextView) convertView.findViewById(R.id.textView);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (data != null && data.size() > 0) {
            holder.textView.setText(data.get(position));
        }

        return convertView;
    }

    public void setData(List<String> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    class ViewHolder {
        public TextView textView;
    }

}
