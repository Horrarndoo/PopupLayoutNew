package com.zyw.horrarndoo.popuplayout.view;

import android.content.Context;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChildHelper;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by Horrarndoo on 2017/6/30.
 * 自定义scrollview，监听scrollview滑动到顶部事件
 */

public class MyScrollView extends ScrollView implements NestedScrollingChild{
    private boolean isScrollToTop = true;
    private boolean isScrollToBottom = false;
    private OnScrollLimitListener mOnScrollLimitListener;

    private NestedScrollingChildHelper mScrollingChildHelper;

    public MyScrollView(Context context) {
        this(context, null);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        getScrollingChildHelper().setNestedScrollingEnabled(enabled);
    }

    @Override
    public boolean isNestedScrollingEnabled() {
        return getScrollingChildHelper().isNestedScrollingEnabled();
    }

    @Override
    public boolean startNestedScroll(int axes) {
        return getScrollingChildHelper().startNestedScroll(axes);
    }

    @Override
    public void stopNestedScroll() {
        getScrollingChildHelper().stopNestedScroll();
    }

    @Override
    public boolean hasNestedScrollingParent() {
        return getScrollingChildHelper().hasNestedScrollingParent();
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed,
                                        int dyUnconsumed, int[] offsetInWindow) {
        return getScrollingChildHelper().dispatchNestedScroll(dxConsumed, dyConsumed,
                dxUnconsumed, dyUnconsumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        return getScrollingChildHelper().dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return getScrollingChildHelper().dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return getScrollingChildHelper().dispatchNestedPreFling(velocityX, velocityY);
    }

    private NestedScrollingChildHelper getScrollingChildHelper() {
        if (mScrollingChildHelper == null) {
            mScrollingChildHelper = new NestedScrollingChildHelper(this);
            setNestedScrollingEnabled(true);
        }
        return mScrollingChildHelper;
    }

    /**
     * 设置ScrollView滑动到边界监听
     *
     * @param onScrollLimitListener ScrollView滑动到边界监听
     */
    public void setOnScrollLimitListener(OnScrollLimitListener onScrollLimitListener) {
        mOnScrollLimitListener = onScrollLimitListener;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (getScrollY() == 0) {//滑动到顶部
            isScrollToTop = true;
            isScrollToBottom = false;
            isScrollToBottom = false;
        } else if (getScrollY() + getHeight() - getPaddingTop() - getPaddingBottom() ==
                getChildAt(0).getHeight()) {
            // 小心踩坑: 这里不能是 >=
            // 小心踩坑：这里最容易忽视的就是ScrollView上下的padding　
            isScrollToTop = false;
            isScrollToBottom = true;
        } else {
            isScrollToTop = false;
            isScrollToBottom = false;
        }
        notifyScrollChangedListeners();
    }

    /**
     * 回调
     */
    private void notifyScrollChangedListeners() {
        if (isScrollToTop) {
            if (mOnScrollLimitListener != null) {
                mOnScrollLimitListener.onScrollTop();
            }
        } else if (isScrollToBottom) {
            if (mOnScrollLimitListener != null) {
                mOnScrollLimitListener.onScrollBottom();
            }
        } else {
            if (mOnScrollLimitListener != null) {
                mOnScrollLimitListener.onScrollOther();
            }
        }
    }

    /**
     * scrollview滑动到边界监听接口
     */
    public interface OnScrollLimitListener {
        /**
         * 滑动到顶部
         */
        void onScrollTop();

        /**
         * 滑动到顶部和底部之间的位置（既不是顶部也不是底部）
         */
        void onScrollOther();

        /**
         * 滑动到底部
         */
        void onScrollBottom();
    }
}
