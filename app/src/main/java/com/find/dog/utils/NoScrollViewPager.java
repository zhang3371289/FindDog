package com.find.dog.utils;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by zhangzhongwei on 2017/6/27.
 */

public class NoScrollViewPager extends ViewPager {

    public void setNeedScroll(boolean needScroll) {
        isNeedScroll = needScroll;
    }

    private boolean isNeedScroll = true;

    public NoScrollViewPager(Context context) {
        super(context);
    }

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (isNeedScroll) {
            return super.onTouchEvent(ev);
        } else {
            return false;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isNeedScroll) {
            return super.onInterceptTouchEvent(ev);
        } else {
            return false;
        }
    }

}