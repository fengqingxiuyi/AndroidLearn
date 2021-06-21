package com.example.learn.ui.drawer;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.customview.widget.ViewDragHelper;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author fqxyi
 * @date 2021/6/20
 * 垂直方向的DrawerLayout
 */
public class VerticalDrawerLayout extends ViewGroup {
    private static final String TAG = "DragLayout";

    private static final float TOUCH_SLOP_SENSITIVITY = 1.f;
    private static final int[] LAYOUT_ATTRS = new int[]{
            android.R.attr.layout_gravity
    };

    private final ViewDragHelper mBottomDragger;
    private final ViewDragCallback mBottomCallback;
    private final ViewDragHelper mTopDragger;
    private final ViewDragCallback mTopCallback;
    private int mCurrentOrientation;

    //外部设置的参数
    private int mDragHeightFirst = 0; //拖拽View初始阶段显示区域高度，0为全部隐藏
    private int mDragHeightSecond = 0; //拖拽View第二阶段显示的区域高度
    private int mDragHeightThird = 0; //拖拽View最终阶段显示的区域高度，方向不同值不同
    private View mScrollView = null; //滚动试图View，可能是RecyclerView，ScrollView等

    public VerticalDrawerLayout(Context context) {
        this(context, null);
    }

    public VerticalDrawerLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerticalDrawerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mTopCallback = new ViewDragCallback(Gravity.TOP);
        mTopDragger = ViewDragHelper.create(this, TOUCH_SLOP_SENSITIVITY, mTopCallback);
        mTopDragger.setEdgeTrackingEnabled(ViewDragHelper.EDGE_TOP);
        mTopCallback.setDragger(mTopDragger);

        mBottomCallback = new ViewDragCallback(Gravity.BOTTOM);
        mBottomDragger = ViewDragHelper.create(this, TOUCH_SLOP_SENSITIVITY, mBottomCallback);
        mBottomDragger.setEdgeTrackingEnabled(ViewDragHelper.EDGE_BOTTOM);
        mBottomCallback.setDragger(mBottomDragger);
    }

    class ViewDragCallback extends ViewDragHelper.Callback {
        private final int mAbsGravity;
        private ViewDragHelper mDragger;

        public ViewDragCallback(int gravity) {
            mAbsGravity = gravity;
        }

        public void setDragger(ViewDragHelper dragger) {
            mDragger = dragger;
        }

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return isDrawerView(child) && checkDrawerViewAbsoluteGravity(child, mAbsGravity);
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            return child.getLeft();
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            if (checkDrawerViewAbsoluteGravity(child, Gravity.BOTTOM)) {
                final int bottomBound = getHeight() - mDragHeightFirst;
                final int newTop = Math.min(Math.max(top, 0), bottomBound);
                Log.i(TAG, "BOTTOM -> newTop = " + newTop + ", top = " + top);
                return newTop;
            } else {
                final int bottomBound = getHeight() - mDragHeightFirst;
                final int newTop = Math.min(Math.max(top, -bottomBound), 0);
                Log.i(TAG, "TOP -> newTop = " + newTop + ", top = " + top);
                return newTop;
            }
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            final int childTop = releasedChild.getTop();

            int top;
            if (checkDrawerViewAbsoluteGravity(releasedChild, Gravity.BOTTOM)) {
                if (mDragHeightSecond <= mDragHeightFirst) {
                    if (childTop > getHeight() / 2) {
                        top = getHeight() - mDragHeightFirst;
                    } else {
                        top = 0;
                    }
                } else {
                    if (childTop > getHeight() - mDragHeightSecond) {
                        if (childTop > getHeight() - mDragHeightSecond / 2) {
                            top = getHeight() - mDragHeightFirst;
                        } else {
                            top = mDragHeightSecond;
                        }
                    } else {
                        if (childTop > (getHeight() - mDragHeightSecond) / 2) {
                            top = mDragHeightSecond;
                        } else {
                            top = 0;
                        }
                    }
                }
            } else {
                if (mDragHeightSecond <= mDragHeightFirst) {
                    if (Math.abs(childTop) > getHeight() / 2) {
                        top = -(getHeight() - mDragHeightFirst);
                    } else {
                        top = 0;
                    }
                } else {
                    if (Math.abs(childTop) > mDragHeightSecond) {
                        if (Math.abs(childTop) > mDragHeightSecond + (getHeight() - mDragHeightSecond) / 2) {
                            top = -(getHeight() - mDragHeightFirst);
                        } else {
                            top = -mDragHeightSecond;
                        }
                    } else {
                        if (Math.abs(childTop) > mDragHeightSecond / 2) {
                            top = -mDragHeightSecond;
                        } else {
                            top = 0;
                        }
                    }
                }
            }

            mDragger.settleCapturedViewAt(releasedChild.getLeft(), top);
            invalidate();
        }

        @Override
        public int getViewVerticalDragRange(View child) {
            return isDrawerView(child) ? child.getHeight() : 0;
        }
    }

    public class LayoutParams extends MarginLayoutParams {

        public int gravity = Gravity.NO_GRAVITY;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);

            final TypedArray a = c.obtainStyledAttributes(attrs, LAYOUT_ATTRS);
            this.gravity = a.getInt(0, Gravity.NO_GRAVITY);
            mCurrentOrientation = gravity;
            a.recycle();
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(int width, int height, int gravity) {
            this(width, height);
            this.gravity = gravity;
        }

        public LayoutParams(LayoutParams source) {
            super(source);
            this.gravity = source.gravity;
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }
    }

    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams
                ? new LayoutParams((LayoutParams) p)
                : p instanceof MarginLayoutParams
                ? new LayoutParams((MarginLayoutParams) p)
                : new LayoutParams(p);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams && super.checkLayoutParams(p);
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(widthSize, heightSize);

        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = getChildAt(i);

            if (child.getVisibility() == GONE) {
                continue;
            }

            final LayoutParams lp = (LayoutParams) child.getLayoutParams();

            if (isContentView(child)) {
                // Content views get measured at exactly the layout's size.
                final int contentWidthSpec = MeasureSpec.makeMeasureSpec(
                        widthSize - lp.leftMargin - lp.rightMargin, MeasureSpec.EXACTLY);
                final int contentHeightSpec = MeasureSpec.makeMeasureSpec(
                        heightSize - lp.topMargin - lp.bottomMargin, MeasureSpec.EXACTLY);
                child.measure(contentWidthSpec, contentHeightSpec);
            } else if (isDrawerView(child)) {
                final int drawerWidthSpec = getChildMeasureSpec(widthMeasureSpec,
                        lp.leftMargin + lp.rightMargin, lp.width);
                final int drawerHeightSpec = getChildMeasureSpec(heightMeasureSpec,
                        lp.topMargin + lp.bottomMargin, lp.height);
                child.measure(drawerWidthSpec, drawerHeightSpec);
            } else {
                throw new IllegalStateException("Child " + child + " at index " + i +
                        " does not have a valid layout_gravity - must be Gravity.TOP, " +
                        "Gravity.BOTTOM or Gravity.NO_GRAVITY");
            }

        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = getChildAt(i);

            if (child.getVisibility() == GONE) {
                continue;
            }

            final LayoutParams lp = (LayoutParams) child.getLayoutParams();

            if (isContentView(child)) {
                child.layout(lp.leftMargin, lp.topMargin,
                        lp.leftMargin + child.getMeasuredWidth(),
                        lp.topMargin + child.getMeasuredHeight());
            } else { // Drawer, if it wasn't onMeasure would have thrown an exception.
                final int childWidth = child.getMeasuredWidth();
                final int childHeight = child.getMeasuredHeight();

                final int gravity = lp.gravity & Gravity.VERTICAL_GRAVITY_MASK;

                switch (gravity) {
                    case Gravity.BOTTOM: {
                        final int height = b - t;
                        child.layout(lp.leftMargin,
                                height - mDragHeightFirst,
                                lp.leftMargin + child.getMeasuredWidth(),
                                height - mDragHeightFirst + childHeight);
                        break;
                    }
                    case Gravity.TOP: {
                        final int height = b - t;
                        child.layout(lp.leftMargin,
                                -(height - mDragHeightFirst),
                                lp.leftMargin + child.getMeasuredWidth(),
                                mDragHeightFirst);
                        break;
                    }
                    default:
                        child.layout(lp.leftMargin,
                                lp.topMargin,
                                lp.leftMargin + child.getMeasuredWidth(),
                                lp.topMargin + child.getMeasuredHeight());
                        break;
                }
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final boolean interceptForDrag = mTopDragger.shouldInterceptTouchEvent(ev)
                | mBottomDragger.shouldInterceptTouchEvent(ev);
        //返回true说明可继续滑动，需要拦截
        boolean childIntercept = false;
        if (mScrollView instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) mScrollView;
            if (mCurrentOrientation == Gravity.BOTTOM) {
                childIntercept = recyclerView.canScrollVertically(-1);
            } else if (mCurrentOrientation == Gravity.TOP) {
                childIntercept = recyclerView.canScrollVertically(1);
            }
        } else if (mScrollView instanceof ScrollView) {
            ScrollView scrollView = (ScrollView) mScrollView;
            if (mCurrentOrientation == Gravity.BOTTOM) {
                childIntercept = scrollView.getScrollY() != 0;
            } else if (mCurrentOrientation == Gravity.TOP) {
                View contentView = scrollView.getChildAt(0);
                childIntercept = contentView.getMeasuredHeight() > scrollView.getScrollY() + scrollView.getHeight();
            }
        }
        if (childIntercept) {
            return false;
        } else {
            return interceptForDrag;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        mTopDragger.processTouchEvent(ev);
        mBottomDragger.processTouchEvent(ev);
        return true;
    }

    @Override
    public void computeScroll() {
        if (mTopDragger.continueSettling(true) | mBottomDragger.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    boolean isContentView(View child) {
        return ((LayoutParams) child.getLayoutParams()).gravity == Gravity.NO_GRAVITY;
    }

    boolean isDrawerView(View child) {
        final int gravity = ((LayoutParams) child.getLayoutParams()).gravity;
        final int absGravity = GravityCompat.getAbsoluteGravity(gravity,
                ViewCompat.getLayoutDirection(child));
        return (absGravity & (Gravity.TOP | Gravity.BOTTOM)) != 0;
    }

    boolean checkDrawerViewAbsoluteGravity(View drawerView, int checkFor) {
        final int absGravity = getDrawerViewAbsoluteGravity(drawerView);
        return (absGravity & checkFor) == checkFor;
    }

    int getDrawerViewAbsoluteGravity(View drawerView) {
        final int gravity = ((LayoutParams) drawerView.getLayoutParams()).gravity;
        return GravityCompat.getAbsoluteGravity(gravity, ViewCompat.getLayoutDirection(this));
    }

    public void setDragHeightFirst(int dragHeight) {
        mDragHeightFirst = dragHeight;
    }

    public void setDragHeightSecond(int dragHeight) {
        mDragHeightSecond = dragHeight;
    }

    public void setInterceptScrollView(View scrollView) {
        mScrollView = scrollView;
    }

}
