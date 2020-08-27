package com.example.social.dialog;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.social.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 社会化弹框的适配器
 */
public class SocialAdapter extends RecyclerView.Adapter<SocialAdapter.SocialViewHolder> {

    //上下文
    private Context context;
    //数据源
    private List<SocialTypeBean> socialTypeBeans;
    //点击事件回调
    private ItemClickListener itemClickListener;

    public SocialAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public SocialViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SocialViewHolder(View.inflate(context, R.layout.social_dialog_item, null));
    }

    @Override
    public void onBindViewHolder(@NonNull SocialViewHolder holder, final int position) {
        //数据处理
        if (socialTypeBeans == null || socialTypeBeans.get(position) == null) {
            return;
        }
        final SocialTypeBean socialTypeBean = socialTypeBeans.get(position);
        //View展示
        if (socialTypeBean.socialType != 5) {
            holder.socialItemIcon.setImageResource(SocialDialogUtil.getIcon(socialTypeBean.socialType));
            holder.socialItemName.setText(SocialDialogUtil.getName(socialTypeBean.socialType));
        } else {
            if (!TextUtils.isEmpty(socialTypeBean.socialIcon)) {
                holder.socialItemIcon.setImageURI(Uri.parse(socialTypeBean.socialIcon));
            }
            holder.socialItemName.setText(socialTypeBean.socialName);
        }
        //点击事件
        holder.socialItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    itemClickListener.click(socialTypeBean, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return socialTypeBeans == null ? 0 : socialTypeBeans.size();
    }

    /**
     * 更新数据
     */
    public void updateData(List<SocialTypeBean> list) {
        if (list == null) {
            return;
        }
        if (socialTypeBeans == null) {
            socialTypeBeans = new ArrayList<>();
        } else {
            socialTypeBeans.clear();
        }
        socialTypeBeans.addAll(list);
        notifyDataSetChanged();
    }

    /**
     * 设置点击事件回调
     */
    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    class SocialViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout socialItem;
        private ImageView socialItemIcon;
        private TextView socialItemName;

        public SocialViewHolder(View itemView) {
            super(itemView);
            socialItem = (LinearLayout) itemView.findViewById(R.id.social_item);
            socialItemIcon = (ImageView) itemView.findViewById(R.id.social_item_icon);
            socialItemName = (TextView) itemView.findViewById(R.id.social_item_name);
        }
    }

}
