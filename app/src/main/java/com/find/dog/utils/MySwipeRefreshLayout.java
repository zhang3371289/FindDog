package com.find.dog.utils;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.find.dog.R;

public class MySwipeRefreshLayout extends SwipeRefreshLayout implements OnScrollListener {

	/**
	 * 解决SwipeRefreshLayout左右滑动事件冲突的问题
	 */
	private float mPrevX;

	private int mTouchSlop;
	private ListView mListView;
	private OnLoadListener mOnLoadListener;
	public View mListViewFooter;
	public LinearLayout mListViewFooter1;
	private LinearLayout.LayoutParams linParam;
	private int mYDown;
	private int mLastY;
	private boolean isLoading = false;
	private boolean canload = true;
	OnScrollListener listener;
	private boolean isFullScreen = true;

	public MySwipeRefreshLayout(Context context) {
		this(context, null);
	}

	public void setScrollListener(OnScrollListener listen) {
		this.listener = listen;
	}

	public void canLoadMore(boolean can) {
		canload = can;
	}

	public MySwipeRefreshLayout(Context context, AttributeSet attrs) {
		super(context, attrs);

		mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
		mListViewFooter1 = new LinearLayout(context);
		linParam = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		linParam.gravity = Gravity.CENTER_HORIZONTAL;
		mListViewFooter = LayoutInflater.from(context).inflate(R.layout.my_swipe_refresh_footer, null, false);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		// 初始化ListView对象
		if (mListView == null) {
			getListView();
		}
	}

	/**
	 * 获取ListView对象
	 */
	private void getListView() {
		int childs = getChildCount();
		if (childs > 0) {
			View childView = getChildAt(0);
			if (childView instanceof ListView) {
				mListView = (ListView) childView;
				// 设置滚动监听器给ListView, 使得滚动的情况下也可以自动加载
				mListView.setOnScrollListener(this);
				mListView.addFooterView(mListViewFooter1);
			}
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		final int action = event.getAction();

		switch (action) {
			case MotionEvent.ACTION_DOWN:
				// 按下
				mYDown = (int) event.getRawY();
				break;

			case MotionEvent.ACTION_MOVE:
				// 移动
				mLastY = (int) event.getRawY();
				break;

			case MotionEvent.ACTION_UP:
				// 抬起
				if (canLoad()) {
					loadData();
				}
				break;
			default:
				break;
		}

		return super.dispatchTouchEvent(event);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {

		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				mPrevX = event.getX();
				break;

			case MotionEvent.ACTION_MOVE:
				final float eventX = event.getX();
				float xDiff = Math.abs(eventX - mPrevX);
				// Log.d("refresh" ,"move----" + eventX + "   " + mPrevX + "   " + mTouchSlop);
				// 增加60的容差，让下拉刷新在竖直滑动时就可以触发
				if (xDiff > mTouchSlop + 60) {
					return false;
				}
		}

		return super.onInterceptTouchEvent(event);
	}

	/**
	 * 是否可以加载更多, 条件是到了最底部, listview不在加载中, 且为上拉操作.
	 *
	 * @return
	 */
	private boolean canLoad() {
		if (canload && mListView == null && !isLoading && isPullUp()) {
			return true;
		} else if (canload && mListView != null && mListView.getAdapter() != null && !isFullScreen && !isLoading && isPullUp()) {
			return true;
		} else if (canload && isBottom() && !isLoading && isPullUp()) {
			return true;
		}
		return false;
	}
	/**
	 * 判断是否到了最底部
	 */
	private boolean isBottom() {

		if (mListView != null && mListView.getAdapter() != null && mListView.getLastVisiblePosition() != 0) {
			return mListView.getLastVisiblePosition() == (mListView.getAdapter().getCount() - 1);
		}
		return false;
	}

	private boolean isPullUp() {
		return (mYDown - mLastY) >= mTouchSlop;
	}

	/**
	 * 如果到了最底部,而且是上拉操作.那么执行onLoad方法
	 */
	private void loadData() {
		if (mOnLoadListener != null && !isRefreshing()) {
			// 设置状态
			setLoading(true);
			mOnLoadListener.onLoad();
		}
	}

	public void setLoading(boolean loading) {
		isLoading = loading;
		if (isLoading) {
			if (mListView != null) {
//				mListView.addFooterView(mListViewFooter);
				mListViewFooter1.removeAllViews();
				mListViewFooter1.addView(mListViewFooter,linParam);
				View view = findViewById(R.id.pull_to_refresh_load_progress);
				if (view != null) {
					view.setVisibility(View.VISIBLE);
				}
				TextView text = ((TextView)findViewById(R.id.pull_to_refresh_loadmore_text));
				if (text != null) {
					text.setText("加载中...");
				}
			}
		} else {
			if (mListView != null) {
//                mListView.removeFooterView(mListViewFooter);
				mListViewFooter1.removeAllViews();
			}
			mYDown = 0;
			mLastY = 0;
		}
	}

	/**
	 * 没有更多数据
	 */
	public void setNoMore(){
		mListViewFooter1.removeAllViews();
		mListViewFooter1.addView(mListViewFooter,linParam);
		findViewById(R.id.pull_to_refresh_load_progress).setVisibility(View.INVISIBLE);
		((TextView)findViewById(R.id.pull_to_refresh_loadmore_text)).setText("没有更多数据了...");

	}
	/**
	 * @param loadListener
	 */
	public void setOnLoadListener(OnLoadListener loadListener) {
		mOnLoadListener = loadListener;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (listener != null) {
			listener.onScrollStateChanged(view, scrollState);
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		// 滚动时到了最底部也可以加载更多
		if (totalItemCount <= visibleItemCount) {
			isFullScreen = false;
		} else {
			isFullScreen = true;
		}
		if (canLoad()) {
			loadData();
		}
		if (listener != null) {
			listener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
		}
	}

	public static interface OnLoadListener {
		public void onLoad();
	}

}