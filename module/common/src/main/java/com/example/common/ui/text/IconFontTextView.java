package com.example.common.ui.text;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.text.Html;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

/**
 * iconfont字体库，旨在不用图片显示小图标而是用文本显示
 */
public class IconFontTextView extends AppCompatTextView {

    private static Typeface iconFont;

    public IconFontTextView(Context context) {
        super(context);
        init(context, null);
    }

    public IconFontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public IconFontTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (isInEditMode()) {
            return;
        }
        // 字体资源放入assets文件夹中
        if (null == iconFont) {
            AssetManager am = context.getAssets();
            try {
                iconFont = Typeface.createFromAsset(am, "fonts/iconfont.ttf");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (null != iconFont) {
            setTypeface(iconFont);
        }

    }

    public void setIconText(String text) {
        setText(Html.fromHtml(text));
    }
}
