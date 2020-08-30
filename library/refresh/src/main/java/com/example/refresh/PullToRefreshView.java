package com.example.refresh;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.Date;

public class PullToRefreshView extends LinearLayout {

	private static final String TAG = "PullToRefreshView";

	private static final int PB_ARROW_TEXT = 2;
	private static final int ARROW_TEXT = 3;

	/**
	 * 控件
	 */
	private LayoutInflater mInflater;

	//// Header
	private View mHeaderView;
	// Header-样式2
	private LinearLayout mHeaderArrowpbContainer;
	private RelativeLayout mHeaderArrowpb;
	private RoundProgressBar mHeaderArrowpbPb;
	private ImageView mHeaderArrowpbArrow;
	private ProgressBar mHeaderArrowpbLoading;
	private TextView mHeaderArrowpbTip;
	private TextView mHeaderArrowpbTime;
	// Header-样式3
	private LinearLayout mHeaderArrowContainer;
	private ImageView mHeaderArrow;
	private ProgressBar mHeaderArrowLoading;
	private TextView mHeaderArrowTip;
	private TextView mHeaderArrowTime;

	//// Footer
	private View mFooterView;
	// Footer-样式1
	private ImageView mFooterImg;
	private TextView mFooterTipTxt;
	private ProgressBar mFooterRefreshPB;

	//// mAdapterView可以包含的View类型
	private AdapterView<?> mAdapterView;
	private ScrollView mScrollView;
	private WebView mWebView;
	private View mOtherView;
	private ViewGroup mViewGroup;

	// 变为向下的箭头,改变箭头方向
	private RotateAnimation mFlipAnimation;
	// 变为逆向的箭头,旋转
	private RotateAnimation mReverseFlipAnimation;

	/**
	 * 监听器
	 */
	// 刷新加载监听器
	private OnHeaderRefreshListener mOnHeaderRefreshListener;
	private OnFooterRefreshListener mOnFooterRefreshListener;
	// 滑动距离监听器
	private DisYListener listener;

	/**
	 * 数据
	 */
	// 拉动状态
	private static final int PULL_UP_STATE = 0;
	private static final int PULL_DOWN_STATE = 1;
	// 刷新状态
	private static final int PULL_TO_REFRESH = 2;
	private static final int RELEASE_TO_REFRESH = 3;
	private static final int REFRESHING = 4;
	// 提示文案
	private String headerPullLabel = "下拉即可刷新";
	private String headerReleaseLabel = "松开即可刷新";
	private String headerRefreshLabel = "正在刷新";
	private String footerPullLabel = "上拉加载更多";
	private String footerReleaseLabel = "释放加载更多";
	private String footerRefreshLabel = "正在加载更多";
	/*
	通用
	 */
	// 记录拉动和刷新的状态
	private int mPullState;
	// 记录手指点击的位置
	private int mLastMotionY;
	private int mLastMotionX;
	// 记录当前头部或底部的拉动刷新状态
	private int mHeaderState;
	private int mFooterState;
	// 记录当前头部或底部View的高度
	private int mHeaderViewHeight;
	private int mFooterViewHeight;
	/*
	头部
	 */
	// 头部滑动偏移量，值得范围为0~newTopMargin
	private int headerOffset = 0;
	// 如果头部布局是 Header-样式1 ，则需要此字段进行首次偏移量的判断
	private boolean isHeaderGirlOffsetFirst = true;
	// rate值越大，滑动相同的距离，头部下滑的距离更大
	private static final float rate = 0.5f;
	// mHeaderArrowpbPb显示的进度
	private int tempProgress = 0;
	/*
	标记类型变量
	 */
	// 是否允许刷新，加载
	private boolean enableRefresh = true;
	private boolean enableLoadMore = true;
	/*
	1 头部布局为小女孩
	2 头部布局为进度条+箭头
	3 头部布局为箭头
	 */
	private int headerRefreshType;
	// 是否显示最近更新时间提示文案
	private boolean isShowTime = false;

	public PullToRefreshView(Context context) {
		super(context);
		init();
	}

	public PullToRefreshView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// 获取自定义的属性
		initAttrs(context, attrs);
		init();
	}

	//获取到attrs中的属性
	private void initAttrs(Context context, AttributeSet attrs) {
		if (attrs != null) {
			TypedArray typeArray = context.getTheme().obtainStyledAttributes(attrs,
					R.styleable.PullToRefreshView, 0, 0);
			headerRefreshType = typeArray.getInt(R.styleable.PullToRefreshView_headerRefreshType, ARROW_TEXT);
			typeArray.recycle();
		}
	}

	private void init() {
		// Load all of the animations we need in code rather than through XML
		setOrientation(VERTICAL);
		mFlipAnimation = new RotateAnimation(0, -180,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		mFlipAnimation.setInterpolator(new LinearInterpolator());
		mFlipAnimation.setDuration(180);
		mFlipAnimation.setFillAfter(true);
		mReverseFlipAnimation = new RotateAnimation(-180, 0,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		mReverseFlipAnimation.setInterpolator(new LinearInterpolator());
		mReverseFlipAnimation.setDuration(180);
		mReverseFlipAnimation.setFillAfter(true);

		mInflater = LayoutInflater.from(getContext());
		// header view 在此添加,保证是第一个添加到LinearLayout的最上端
		addHeaderView();
	}

	private void addHeaderView() {
		// header view
		mHeaderView = mInflater.inflate(R.layout.refresh_header, this, false);
		// Header-样式2
		mHeaderArrowpbContainer = (LinearLayout) mHeaderView.findViewById(R.id.header_arrowpb_container);
		mHeaderArrowpb = (RelativeLayout) mHeaderView.findViewById(R.id.header_arrowpb);
		mHeaderArrowpbPb = (RoundProgressBar) mHeaderView.findViewById(R.id.header_arrowpb_pb);
		mHeaderArrowpbLoading = (ProgressBar) mHeaderView.findViewById(R.id.header_arrowpb_loading);
		mHeaderArrowpbArrow = (ImageView) mHeaderView.findViewById(R.id.header_arrowpb_arrow);
		mHeaderArrowpbArrow.setScaleType(ImageView.ScaleType.MATRIX);
		mHeaderArrowpbTip = (TextView) mHeaderView.findViewById(R.id.header_arrowpb_tip);
		mHeaderArrowpbTime = (TextView) mHeaderView.findViewById(R.id.header_arrowpb_time);
		// Header-样式3
		mHeaderArrowContainer = (LinearLayout) mHeaderView.findViewById(R.id.header_arrow_container);
		mHeaderArrow = (ImageView) mHeaderView.findViewById(R.id.header_arrow);
		mHeaderArrow.setScaleType(ImageView.ScaleType.MATRIX);
		mHeaderArrowLoading = (ProgressBar) mHeaderView.findViewById(R.id.header_arrow_loading);
		mHeaderArrowTip = (TextView) mHeaderView.findViewById(R.id.header_arrow_tip);
		mHeaderArrowTime = (TextView) mHeaderView.findViewById(R.id.header_arrow_time);

		showCurrentHeader();

		// header layout
		measureView(mHeaderView);
		mHeaderViewHeight = mHeaderView.getMeasuredHeight();

		if (PB_ARROW_TEXT == headerRefreshType) {
			// 设置显示进度条的总进度
			mHeaderArrowpbPb.setMax(mHeaderViewHeight);
		}

		// 设置topMargin的值为负的header View高度,即将其隐藏在最上方
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, mHeaderViewHeight);
		params.topMargin = -(mHeaderViewHeight);
		addView(mHeaderView, params);
	}

	private void showCurrentHeader() {
		switch (headerRefreshType) {
			case PB_ARROW_TEXT:
				mHeaderArrowpbContainer.setVisibility(View.VISIBLE);
				mHeaderArrowpb.setVisibility(View.VISIBLE);
				mHeaderArrowContainer.setVisibility(View.GONE);
				break;
			case ARROW_TEXT:
				mHeaderArrowpbContainer.setVisibility(View.GONE);
				mHeaderArrowpb.setVisibility(View.GONE);
				mHeaderArrowContainer.setVisibility(View.VISIBLE);
				break;
		}
	}

	private void measureView(View child) {
		ViewGroup.LayoutParams p = child.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		}
		child.measure(childWidthSpec, childHeightSpec);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		// footer view 在此添加保证添加到LinearLayout中的最后
		addFooterView();
		initContentAdapterView();
	}

	private void addFooterView() {
		// footer view
		mFooterView = mInflater.inflate(R.layout.refresh_footer, this, false);
		mFooterImg = (ImageView) mFooterView.findViewById(R.id.img_footer);
		mFooterTipTxt = (TextView) mFooterView.findViewById(R.id.txt_footer_tip);
		mFooterRefreshPB = (ProgressBar) mFooterView.findViewById(R.id.pb_footer_refresh);
		// footer layout
		measureView(mFooterView);
		mFooterViewHeight = mFooterView.getMeasuredHeight();
		// 由于是线性布局可以直接添加,
		// 只要AdapterView的高度是MATCH_PARENT,那么footer view就会被添加到最后, 并隐藏
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, mFooterViewHeight);
		addView(mFooterView, params);
	}

	/**
	 * 初始化包含的view
	 */
	private void initContentAdapterView() {
		int count = getChildCount();
		if (count < 3) {
			throw new IllegalArgumentException("SHPullToRefreshView can only contain one view!");
		}
		View view;
		for (int i = 0; i < count - 1; ++i) {
			view = getChildAt(i);
			if (view == mFooterView) {
				continue;
			}
			if (view == mHeaderView) {
				continue;
			}
			if (view instanceof AdapterView<?>) {
				mAdapterView = (AdapterView<?>) view;

				if (mAdapterView instanceof AbsListView) {
					AbsListView absListView = (AbsListView) mAdapterView;
					absListView.setOnScrollListener(new AbsListView.OnScrollListener() {
						@Override
						public void onScrollStateChanged(AbsListView view, int scrollState) {
							switch (scrollState) {
								// 当不滚动时
								case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
									// 滑动到底部，自动触发上拉加载操作
									if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
										// 判断是否禁用上拉加载更多操作
										if (!enableLoadMore) {
											return;
										}
										footerRefreshing();
									}
									break;
							}
						}

						@Override
						public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

						}
					});
				}
			}else if (view instanceof ScrollView) {
				// finish later
				mScrollView = (ScrollView) view;

				mScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
					@Override
					public void onScrollChanged() {
						// 滑动到底部，自动触发上拉加载操作
						if (mScrollView.getScrollY() == mScrollView.getChildAt(0).getHeight() - mScrollView.getHeight()) {
							// 判断是否禁用上拉加载更多操作
							if (!enableLoadMore) {
								return;
							}
							footerRefreshing();
						}
					}
				});
			}else if (view instanceof WebView) {
				mWebView = (WebView) view;
			}else if (view instanceof ViewGroup) {
				mViewGroup = (ViewGroup) view;
			}else {
				mOtherView = view;
			}
		}
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent e) {
		int y = (int) e.getRawY();
		int x = (int) e.getRawX();
		switch (e.getAction()) {
			case MotionEvent.ACTION_DOWN:
				// 首先拦截down事件,记录y坐标
				mLastMotionY = y;
				mLastMotionX = x;
				break;
			case MotionEvent.ACTION_MOVE:
				int xDistance = 0;
				int yDistance = 0;
				// deltaY > 0 是向下运动, < 0是向上运动
				int deltaY = y - mLastMotionY;
				int deltaX = x - mLastMotionX;
				xDistance += Math.abs(deltaX);
				yDistance += Math.abs(deltaY);

				if (xDistance > yDistance) {
					return false;
				} else if (isRefreshViewScroll(deltaY)) {
					return true;
				}
				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL:
				break;
		}
		return false;
	}

	/**
	 * 是否应该到了父View,即PullToRefreshView滑动
	 *
	 * @param deltaY , deltaY > 0 是向下运动, < 0是向上运动
	 */
	private boolean isRefreshViewScroll(int deltaY) {
		if (mHeaderState == REFRESHING || mFooterState == REFRESHING) {
			return false;
		}
		// 对于ListView和GridView
		if (mAdapterView != null) {
			// 子view(ListView or GridView)滑动到最顶端
			if (deltaY > 0) {
				// 判断是否禁用下拉刷新操作
				if (!enableRefresh) {
					return false;
				}
				View child = mAdapterView.getChildAt(0);
				if (child == null) {
					// 如果mAdapterView中没有数据,不拦截
					return false;
				}
				if (mAdapterView.getFirstVisiblePosition() == 0
						&& child.getTop() == 0) {
					mPullState = PULL_DOWN_STATE;
					return true;
				}
				int top = child.getTop();
				int padding = mAdapterView.getPaddingTop();
				if (mAdapterView.getFirstVisiblePosition() == 0
						&& Math.abs(top - padding) <= 8) {// 这里之前用3可以判断,但现在不行,还没找到原因
					mPullState = PULL_DOWN_STATE;
					return true;
				}
			} else if (deltaY < 0) {
				// 判断是否禁用上拉加载更多操作
				if (!enableLoadMore) {
					return false;
				}
				View lastChild = mAdapterView.getChildAt(mAdapterView.getChildCount() - 1);
				if (lastChild == null) {
					// 如果mAdapterView中没有数据,不拦截
					return false;
				}
				// 最后一个子view的Bottom小于父View的高度说明mAdapterView的数据没有填满父view,
				// 等于父View的高度说明mAdapterView已经滑动到最后
				if (lastChild.getBottom() <= getHeight()
						&& mAdapterView.getLastVisiblePosition() == mAdapterView.getCount() - 1) {
					mPullState = PULL_UP_STATE;
					return true;
				}
			}
		} else if (mScrollView != null) {
			// 子scroll view滑动到最顶端
			View child = mScrollView.getChildAt(0);
			if (deltaY > 0 && mScrollView.getScrollY() == 0) {
				// 判断是否禁用下拉刷新操作
				if (!enableRefresh) {
					return false;
				}
				mPullState = PULL_DOWN_STATE;
				return true;
			} else if (deltaY < 0
					&& child.getMeasuredHeight() <= getHeight() + mScrollView.getScrollY()) {
				// 判断是否禁用上拉加载更多操作
				if (!enableLoadMore) {
					return false;
				}
				mPullState = PULL_UP_STATE;
				return true;
			}
		} else if (mWebView != null) {
			if (deltaY > 0 && mWebView.getScrollY() == 0) {
				// 判断是否禁用下拉刷新操作
				if (!enableRefresh) {
					return false;
				}
				mPullState = PULL_DOWN_STATE;
				return true;
			} else if (deltaY < 0
					&& mWebView.getContentHeight() <= getHeight()) {
				// 判断是否禁用上拉加载更多操作
				if (!enableLoadMore) {
					return false;
				}
				mPullState = PULL_UP_STATE;
				return true;
			}
		} else if (null != mOtherView){
			if (deltaY > 0 && mOtherView.getScrollY() == 0) {
				// 判断是否禁用下拉刷新操作
				if (!enableRefresh) {
					return false;
				}
				mPullState = PULL_DOWN_STATE;
				return true;
			} else if (deltaY < 0) {
				// 判断是否禁用上拉加载更多操作
				if (!enableLoadMore) {
					return false;
				}
				mPullState = PULL_UP_STATE;
				return true;
			}
		} else if (null != mViewGroup) {
			if (deltaY > 0 && mViewGroup.getScrollY() == 0) {
				// 判断是否禁用下拉刷新操作
				if (!enableRefresh) {
					return false;
				}
				mPullState = PULL_DOWN_STATE;
				return true;
			} else if (deltaY < 0
					&& mViewGroup.getMeasuredHeight() <= getHeight() + mViewGroup.getScrollY()) {
				// 判断是否禁用上拉加载更多操作
				if (!enableLoadMore) {
					return false;
				}
				mPullState = PULL_UP_STATE;
				return true;
			}
		}
		return false;
	}

	/*
	 * 如果在onInterceptTouchEvent()方法中没有拦截(即onInterceptTouchEvent()方法中 return false;)
	 * 则由PullToRefreshView的子View来处理;否则由下面的方法来处理(即由PullToRefreshView自己来处理)
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int y = (int) event.getRawY();
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				// onInterceptTouchEvent已经记录
				// mLastMotionY = y;
				break;
			case MotionEvent.ACTION_MOVE:
				int deltaY = y - mLastMotionY;

				if (mPullState == PULL_DOWN_STATE) {
					// PullToRefreshView执行下拉
					Log.i(TAG, " pull down!parent view move!");
					headerPrepareToRefresh(deltaY);
					// setHeaderPadding(-mHeaderViewHeight);
				} else if (mPullState == PULL_UP_STATE) {
					// PullToRefreshView执行上拉
					Log.i(TAG, "pull up!parent view move!");
					footerPrepareToRefresh(deltaY);
				}
				mLastMotionY = y;
				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL:
				int topMargin = getHeaderTopMargin();

				if (listener != null) {
					listener.getTopMargin(Math.abs(topMargin),
							mHeaderViewHeight + mFooterViewHeight, true);
				}

				if (mPullState == PULL_DOWN_STATE) {
					if (topMargin >= 0) {
						// 开始刷新
						headerRefreshing();
					} else {
						// 还没有执行刷新，重新隐藏
						setHeaderTopMargin(-mHeaderViewHeight);
					}
				} else if (mPullState == PULL_UP_STATE) {
					if (Math.abs(topMargin) >= mHeaderViewHeight
							+ mFooterViewHeight) {
						// 开始执行footer刷新
						footerRefreshing();
					} else {
						// 还没有执行刷新，重新隐藏
						setHeaderTopMargin(-mHeaderViewHeight);
					}
				}
				break;
		}
		return super.onTouchEvent(event);
	}

	/**
	 * header准备刷新,手指移动过程,还没有释放
	 *
	 * @param deltaY,手指滑动的距离
	 */
	private void headerPrepareToRefresh(int deltaY) {
		int newTopMargin = changingHeaderViewTopMargin(deltaY);
		changeTopMargin(newTopMargin, 0);
	}

	private void changeTopMargin(int newTopMargin, int offset) {
		if (ARROW_TEXT == headerRefreshType) {
			if (newTopMargin < offset && Math.abs(newTopMargin) <= mHeaderViewHeight) {
				tempProgress = mHeaderViewHeight + newTopMargin;
				rotate(mHeaderArrow, 1.0f * tempProgress / mHeaderViewHeight * 180);
			} else {
				if (tempProgress < mHeaderViewHeight) {
					rotate(mHeaderArrow, 180);
				}
			}
			// 当header view的topMargin>=offset时，说明已经完全显示出来了,修改header view的提示状态
			if (newTopMargin > offset && mHeaderState != RELEASE_TO_REFRESH) {
				mHeaderArrowTip.setText(getHeaderReleaseLabel());
				if (isShowTime()) {
					mHeaderArrowTime.setVisibility(View.VISIBLE);
				}
				mHeaderState = RELEASE_TO_REFRESH;
			} else if (newTopMargin < offset && newTopMargin > -mHeaderViewHeight) {// 拖动时没有释放
				// mHeaderImageView.
				mHeaderArrowTip.setText(getHeaderPullLabel());
				mHeaderState = PULL_TO_REFRESH;
			}
		} else {
			if (PB_ARROW_TEXT == headerRefreshType) {
				if (newTopMargin < offset && Math.abs(newTopMargin) <= mHeaderViewHeight) {
					tempProgress = mHeaderViewHeight + newTopMargin;
					mHeaderArrowpbPb.setProgress(tempProgress);
					rotate(mHeaderArrowpbArrow, 1.0f * tempProgress / mHeaderViewHeight * 180);
				} else {
					if (tempProgress < mHeaderViewHeight) {
						mHeaderArrowpbPb.setProgress(mHeaderViewHeight);
						rotate(mHeaderArrowpbArrow, 180);
					}
				}
			}
			// 当header view的topMargin>=offset时，说明已经完全显示出来了,修改header view的提示状态
			if (newTopMargin > offset && mHeaderState != RELEASE_TO_REFRESH) {
				mHeaderArrowpbTip.setText(getHeaderReleaseLabel());
				if (isShowTime()) {
					mHeaderArrowpbTime.setVisibility(View.VISIBLE);
				}
				mHeaderState = RELEASE_TO_REFRESH;
			} else if (newTopMargin < offset && newTopMargin > -mHeaderViewHeight) {// 拖动时没有释放
				// mHeaderImageView.
				mHeaderArrowpbTip.setText(getHeaderPullLabel());
				mHeaderState = PULL_TO_REFRESH;
			}
		}
	}

	private void rotate(ImageView imageView, float degree){
		if (null == imageView) {
			return;
		}
		BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
		if (null == bitmapDrawable) {
			return;
		}
		Bitmap bitmap = bitmapDrawable.getBitmap();
		Matrix matrix = new Matrix();
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		matrix.postRotate(degree, width/2, height/2);
		if (PB_ARROW_TEXT == headerRefreshType) {
			int dispalyWidth = dp2px(10);
			float scale = 1.0f * dispalyWidth / width;
			matrix.postScale(scale, scale);
		}
		if (ARROW_TEXT == headerRefreshType) {
			int dispalyWidth = dp2px(25);
			float scale = 1.0f * dispalyWidth / width;
			matrix.postScale(scale, scale);
		}
		imageView.setImageMatrix(matrix);
	}

	private int dp2px(int dpVal) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, getResources()
				.getDisplayMetrics());

	}
	/**
	 * footer 准备刷新,手指移动过程,还没有释放 移动footer view高度同样和移动header view
	 * 高度是一样，都是通过修改header view的topmargin的值来达到
	 *
	 * @param deltaY ,手指滑动的距离
	 */
	private void footerPrepareToRefresh(int deltaY) {
		int newTopMargin = changingHeaderViewTopMargin(deltaY);

		if (listener != null) {
			listener.getTopMargin(Math.abs(newTopMargin), mHeaderViewHeight + mFooterViewHeight, false);
			listener.getDisY(Math.abs(newTopMargin) - mHeaderViewHeight);

		}

		// 如果header view topMargin的绝对值大于或等于header + footer的高度
		// 说明footer view完全显示出来了，修改footer view的提示状态
		if (Math.abs(newTopMargin) >= (mHeaderViewHeight + mFooterViewHeight)
				&& mFooterState != RELEASE_TO_REFRESH) {
			mFooterTipTxt.setText(getFooterReleaseLabel());
			mFooterImg.clearAnimation();
			mFooterImg.startAnimation(mFlipAnimation);
			mFooterState = RELEASE_TO_REFRESH;
		} else if (Math.abs(newTopMargin) < (mHeaderViewHeight + mFooterViewHeight)) {
			mFooterImg.clearAnimation();
			mFooterImg.startAnimation(mFlipAnimation);
			mFooterTipTxt.setText(getFooterPullLabel());
			mFooterState = PULL_TO_REFRESH;
		}
	}

	/**
	 * 修改Header view top margin的值
	 *
	 * @param deltaY
	 * @return
	 */
	private int changingHeaderViewTopMargin(int deltaY) {
		LayoutParams params = (LayoutParams) mHeaderView.getLayoutParams();
		float newTopMargin = params.topMargin + deltaY * rate;

		// 这里对上拉做一下限制,因为当前上拉后然后不释放手指直接下拉,会把下拉刷新给触发了
		// 表示如果是在上拉后一段距离,然后直接下拉
		if (deltaY > 0 && mPullState == PULL_UP_STATE && params.topMargin < 0
				&& Math.abs(params.topMargin) <= mHeaderViewHeight) {
			return params.topMargin;
		}
		// 同样地,对下拉做一下限制,避免出现跟上拉操作时一样的bug
		if (deltaY < 0 && mPullState == PULL_DOWN_STATE && params.topMargin < 0
				&& Math.abs(params.topMargin) >= mHeaderViewHeight) {
			return params.topMargin;
		}
		params.topMargin = (int) newTopMargin;
		mHeaderView.setLayoutParams(params);
		invalidate();
		return params.topMargin;
	}

	public void headerRefreshing() {
		mHeaderState = REFRESHING;
		setHeaderTopMargin(0);
		if (mOnHeaderRefreshListener != null) {
			mOnHeaderRefreshListener.onHeaderRefresh(this);
		}
		if (PB_ARROW_TEXT == headerRefreshType) {
			mHeaderArrowpbPb.setVisibility(View.GONE);
			mHeaderArrowpbArrow.setVisibility(View.GONE);
			mHeaderArrowpbArrow.clearAnimation();
			mHeaderArrowpbArrow.setImageDrawable(null);
			mHeaderArrowpbLoading.setVisibility(View.VISIBLE);
			mHeaderArrowpbTip.setText(getHeaderRefreshLabel());
		} else if (ARROW_TEXT == headerRefreshType) {
			mHeaderArrow.setVisibility(View.GONE);
			mHeaderArrow.clearAnimation();
			mHeaderArrow.setImageDrawable(null);
			mHeaderArrowLoading.setVisibility(View.VISIBLE);
			mHeaderArrowTip.setText(getHeaderRefreshLabel());
		}
	}

	private void footerRefreshing() {
		mFooterState = REFRESHING;
		int top = mHeaderViewHeight + mFooterViewHeight;
		setHeaderTopMargin(-top);
		mFooterImg.setVisibility(View.GONE);
		mFooterImg.clearAnimation();
		mFooterImg.setImageDrawable(null);
		mFooterRefreshPB.setVisibility(View.VISIBLE);
		mFooterTipTxt.setText(getFooterRefreshLabel());
		if (mOnFooterRefreshListener != null) {
			mOnFooterRefreshListener.onFooterRefresh(this);
		}
	}

	/**
	 * 设置header view的topMargin的值
	 */
	private void setHeaderTopMargin(int topMargin) {
		LayoutParams params = (LayoutParams) mHeaderView.getLayoutParams();
		params.topMargin = topMargin;
		mHeaderView.setLayoutParams(params);
		invalidate();
	}

	/**
	 * header view完成更新后恢复初始状态
	 */
	@SuppressWarnings("deprecation")
	public void onHeaderRefreshComplete() {
		setHeaderTopMargin(-mHeaderViewHeight);
		mHeaderState = PULL_TO_REFRESH;
		if (isShowTime()) {
			setLastUpdated("最近更新:" + new Date().toLocaleString());
		}
		if (PB_ARROW_TEXT == headerRefreshType) {
			mHeaderArrowpbPb.setVisibility(View.VISIBLE);
			mHeaderArrowpbArrow.setVisibility(View.VISIBLE);
			mHeaderArrowpbArrow.setImageResource(R.drawable.header_arrow_purple);
			mHeaderArrowpbLoading.setVisibility(View.GONE);
			mHeaderArrowpbTip.setText(getHeaderPullLabel());
		} else if (ARROW_TEXT == headerRefreshType) {
			mHeaderArrow.setVisibility(View.VISIBLE);
			mHeaderArrow.setImageResource(R.drawable.header_arrow_grey);
			mHeaderArrowLoading.setVisibility(View.GONE);
			mHeaderArrowTip.setText(getHeaderPullLabel());
		}
	}

	public void onHeaderRefreshComplete(CharSequence lastUpdated) {
		if (isShowTime()) {
			setLastUpdated(lastUpdated);
		}
		onHeaderRefreshComplete();
	}

	/**
	 * footer view完成更新后恢复初始状态
	 */
	public void onFooterRefreshComplete() {
		setHeaderTopMargin(-mHeaderViewHeight);
		mFooterImg.setVisibility(View.VISIBLE);
		mFooterImg.setImageResource(R.drawable.footer_arrow_grey);
		mFooterTipTxt.setText(getFooterPullLabel());
		mFooterRefreshPB.setVisibility(View.GONE);
		if (isShowTime()) {
			setLastUpdated("");
		}
		mFooterState = PULL_TO_REFRESH;
	}

	/**
	 * footer view完成更新后恢复初始状态
	 */
	public void onFooterRefreshComplete(int size) {
		if (size > 0) {
			mFooterView.setVisibility(View.VISIBLE);
		} else {
			mFooterView.setVisibility(View.GONE);
		}
		setHeaderTopMargin(-mHeaderViewHeight);
		mFooterImg.setVisibility(View.VISIBLE);
		mFooterImg.setImageResource(R.drawable.footer_arrow_grey);
		mFooterTipTxt.setText(getFooterPullLabel());
		mFooterRefreshPB.setVisibility(View.GONE);
		if (isShowTime()) {
			setLastUpdated("");
		}
		mFooterState = PULL_TO_REFRESH;
	}

	/**
	 * 动态设置显示的刷新的时间
	 * @param lastUpdated
     */
	public void setLastUpdated(CharSequence lastUpdated) {
		if (PB_ARROW_TEXT == headerRefreshType) {
			if (lastUpdated != null) {
				mHeaderArrowpbTime.setVisibility(View.VISIBLE);
				mHeaderArrowpbTime.setText(lastUpdated);
			} else {
				mHeaderArrowpbTime.setVisibility(View.GONE);
			}
		} else if (ARROW_TEXT == headerRefreshType) {
			if (lastUpdated != null) {
				mHeaderArrowTime.setVisibility(View.VISIBLE);
				mHeaderArrowTime.setText(lastUpdated);
			} else {
				mHeaderArrowTime.setVisibility(View.GONE);
			}
		}
	}

	/**
	 * 设置是否显示时间
	 * @return
     */
	public boolean isShowTime() {
		return isShowTime;
	}

	/**
	 * 设置是否显示时间
	 * @return
	 */
	public void setShowTime(boolean isShowTime) {
		this.isShowTime = isShowTime;
	}

	/**
	 * 滑动距离回调监听
	 */
	public interface DisYListener {
		public void getDisY(int disY);

		public void getTopMargin(int topMargin, int totalHeight, boolean isHandRelease);
	}

	public void setDisYListener(DisYListener listener) {
		this.listener = listener;
	}

	/**
	 * 设置头部下拉刷新的回调
	 *
	 * @param headerRefreshListener
	 */
	public void setOnHeaderRefreshListener(
			OnHeaderRefreshListener headerRefreshListener) {
		mOnHeaderRefreshListener = headerRefreshListener;
	}


	/**
	 * 设置底部上拉刷新的回调
	 *
	 * @param footerRefreshListener
	 */
	public void setOnFooterRefreshListener(
			OnFooterRefreshListener footerRefreshListener) {
		mOnFooterRefreshListener = footerRefreshListener;
	}

	/**
	 * 是否允许下拉刷新
	 *
	 * @return
	 */
	public boolean isEnableRefresh() {
		return enableRefresh;
	}

	/**
	 * 设置是否允许下拉刷新
	 *
	 * @param enableRefresh
	 */
	public void setEnableRefresh(boolean enableRefresh) {
		this.enableRefresh = enableRefresh;
	}

	/**
	 * 是否允许加载更多
	 *
	 * @return
	 */
	public boolean isEnableLoadMore() {
		return enableLoadMore;
	}


	/**
	 * 设置是否允许加载更多
	 *
	 * @param enableLoadMore
	 */
	public void setEnableLoadMore(boolean enableLoadMore) {
		this.enableLoadMore = enableLoadMore;
	}


	/**
	 * 设置头部下拉时的文字
	 *
	 * @return
	 */
	public String getHeaderPullLabel() {
		return headerPullLabel;
	}

	/**
	 * 设置头部下拉时的文字
	 *
	 * @param headerPullLabel
	 */
	public void setHeaderPullLabel(String headerPullLabel) {
		this.headerPullLabel = headerPullLabel;
	}

	/**
	 * 获取头部释放时的文字
	 *
	 * @return
	 */
	public String getHeaderReleaseLabel() {
		return headerReleaseLabel;
	}

	/**
	 * 设置头部释放时的文字
	 *
	 * @param headerReleaseLabel
	 */
	public void setHeaderReleaseLabel(String headerReleaseLabel) {
		this.headerReleaseLabel = headerReleaseLabel;
	}

	/**
	 * 获取头部刷新时的文字
	 *
	 * @return
	 */
	public String getHeaderRefreshLabel() {
		return headerRefreshLabel;
	}

	/**
	 * 设置头部刷新时的文字
	 *
	 * @param headerRefreshLabel
	 */
	public void setHeaderRefreshLabel(String headerRefreshLabel) {
		this.headerRefreshLabel = headerRefreshLabel;
	}

	/**
	 * 获取底部上拉时的文字
	 *
	 * @return
	 */
	public String getFooterPullLabel() {
		return footerPullLabel;
	}

	/**
	 * 设置底部上拉时的文字
	 *
	 * @param footerPullLabel
	 */
	public void setFooterPullLabel(String footerPullLabel) {
		this.footerPullLabel = footerPullLabel;
	}

	/**
	 * 获取底部释放后的文字
	 *
	 * @return
	 */
	public String getFooterReleaseLabel() {
		return footerReleaseLabel;
	}

	/**
	 * 设置底部释放后的文字
	 *
	 * @param footerReleaseLabel
	 */
	public void setFooterReleaseLabel(String footerReleaseLabel) {
		this.footerReleaseLabel = footerReleaseLabel;
	}

	/**
	 * 获取底部加载时的文字
	 *
	 * @return
	 */
	public String getFooterRefreshLabel() {
		return footerRefreshLabel;
	}

	/**
	 * 设置底部加载时的文字
	 *
	 * @param footerRefreshLabel
	 */
	public void setFooterRefreshLabel(String footerRefreshLabel) {
		this.footerRefreshLabel = footerRefreshLabel;
	}

	public void showFootView(boolean show) {
		if (show) {
			mFooterView.setVisibility(View.VISIBLE);
		} else {
			mFooterView.setVisibility(View.GONE);
		}
	}

	private int getHeaderTopMargin() {
		LayoutParams params = (LayoutParams) mHeaderView.getLayoutParams();
		return params.topMargin;
	}

	public interface OnFooterRefreshListener {
		void onFooterRefresh(PullToRefreshView view);
	}

	public interface OnHeaderRefreshListener {
		void onHeaderRefresh(PullToRefreshView view);
	}

	/**
	 * 样式2-设置提示颜色
	 * @param color
	 */
	public PullToRefreshView setHeaderArrowpbTipTextColor(int color) {
		mHeaderArrowpbTip.setTextColor(getResources().getColor(color));
		return this;
	}

	/**
	 * 样式2-设置时间颜色
	 * @param color
	 */
	public PullToRefreshView setHeaderArrowpbTimeTextColor(int color) {
		mHeaderArrowpbTime.setTextColor(getResources().getColor(color));
		return this;
	}

	/**
	 * 样式3-设置提示颜色
	 * @param color
	 */
	public PullToRefreshView setHeaderArrowTipTextColor(int color) {
		mHeaderArrowTip.setTextColor(getResources().getColor(color));
		return this;
	}

	/**
	 * 样式3-设置时间颜色
	 * @param color
	 */
	public PullToRefreshView setHeaderArrowTimeTextColor(int color) {
		mHeaderArrowTime.setTextColor(getResources().getColor(color));
		return this;
	}

}