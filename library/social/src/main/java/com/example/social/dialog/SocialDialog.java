package com.example.social.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.social.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 社会化弹框
 */
public class SocialDialog extends Dialog {

    //静态常量
    private static final String TAG = "SocialDialog";
    //上下文
    private Context context;
    //view
    private RecyclerView socialRecyclerView;
    private Button socialCancel;
    //adapter
    private SocialAdapter socialAdapter;
    //数据源-社会化类型
    private List<SocialTypeBean> socialTypeBeans;
    //点击事件回调
    private ItemClickListener itemClickListener;

    public SocialDialog(Context context) {
        super(context, R.style.SocialDialogStyle);
        init(context);
    }

    public SocialDialog(Context context, int themeResId) {
        super(context, themeResId);
        init(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.social_dialog);
        //findView
        socialRecyclerView = (RecyclerView) findViewById(R.id.social_recycler_view);
        socialCancel = (Button) findViewById(R.id.social_cancel);
        //初始化RecyclerView
        initRecyclerView();
        //事件初始化
        initEvent();
    }

    private void initRecyclerView() {
        socialRecyclerView.setLayoutManager(new GridLayoutManager(context, 4));
        socialAdapter = new SocialAdapter(context);
        socialAdapter.updateData(socialTypeBeans);
        socialRecyclerView.setAdapter(socialAdapter);
    }

    private void initEvent() {
        //social item icon 点击事件
        socialAdapter.setItemClickListener(new ItemClickListener() {
            @Override
            public void click(SocialTypeBean socialTypeBean, int position) {
                if (itemClickListener != null) {
                    itemClickListener.click(socialTypeBean, position);
                }
            }
        });
        //social cancel 点击事件
        socialCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    /**
     * 初始化社会化类型
     */
    public void initSocialType(List<SocialTypeBean> list) {
        if (list == null) {
            return;
        }
        if (socialTypeBeans == null) {
            socialTypeBeans = new ArrayList<>();
        } else {
            socialTypeBeans.clear();
        }
        socialTypeBeans.addAll(list);
    }

    /**
     * 设置点击事件回调
     */
    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    /**
     * Dialog View初始化
     */
    private void init(Context context) {
        this.context = context;
        if (!(context instanceof Activity)) {
            Log.e(TAG, "context is not Activity");
            return;
        }
        Window window = getWindow();
        if (window == null) {
            Log.e(TAG, "getWindow() == null");
            return;
        }
        // 设置显示动画
        window.setWindowAnimations(R.style.SocialDialogAnim);
        WindowManager.LayoutParams params = window.getAttributes();
        params.x = 0;
        if (((Activity) context).getWindowManager() != null &&
                ((Activity) context).getWindowManager().getDefaultDisplay() != null) {
            params.y = ((Activity) context).getWindowManager().getDefaultDisplay().getHeight();
        }
        // 以下这两句是为了保证按钮可以水平满屏
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        // 设置显示位置
        onWindowAttributesChanged(params);
    }

}
