package com.example.image.fresco;

import android.content.Context;
import android.util.AttributeSet;

import com.example.image.listener.ICircleImageView;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.RoundingParams;

/**
 * @author fqxyi
 * @date 2018/2/22
 * 圆形图
 */
public class FrescoCircleImageView extends FrescoImageView implements ICircleImageView {

    public FrescoCircleImageView(Context context, GenericDraweeHierarchy hierarchy) {
        super(context, hierarchy);
        init();
    }

    public FrescoCircleImageView(Context context) {
        super(context);
        init();
    }

    public FrescoCircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FrescoCircleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public FrescoCircleImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        getHierarchy().setRoundingParams(getRoundingParams());
    }

    @Override
    public void setBorderColor(int color) {
        RoundingParams roundingParams = getRoundingParams();
        roundingParams.setBorder(color, roundingParams.getBorderWidth());
        getHierarchy().setRoundingParams(roundingParams);
    }

    @Override
    public void setBorderWidth(int width) {
        RoundingParams roundingParams = getRoundingParams();
        roundingParams.setBorder(roundingParams.getBorderColor(), width);
        getHierarchy().setRoundingParams(roundingParams);
    }

    private RoundingParams getRoundingParams() {
        RoundingParams roundingParams = getHierarchy().getRoundingParams();
        if (null == roundingParams) {
            roundingParams = RoundingParams.asCircle();
        } else  {
            roundingParams.setRoundAsCircle(true);
        }
        return roundingParams;
    }

}
