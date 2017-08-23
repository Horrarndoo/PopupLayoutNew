package com.zyw.horrarndoo.popuplayout.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.NestedScrollingParent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.zyw.horrarndoo.popuplayout.R;
import com.zyw.horrarndoo.popuplayout.utils.UIUtils;

import static android.content.ContentValues.TAG;
import static com.zyw.horrarndoo.popuplayout.view.PopupLayout.TitleItemType.TYPE_TITLE_BACK;
import static com.zyw.horrarndoo.popuplayout.view.PopupLayout.TitleItemType.TYPE_TITLE_SEARCH;
import static com.zyw.horrarndoo.popuplayout.view.PopupLayout.TitleItemType.TYPE_TITLE_SHARE;

/**
 * Created by Horrarndoo on 2017/6/30.
 * 弹窗layout
 */

public class PopupLayout extends LinearLayout implements NestedScrollingParent {
    private View darkView;//暗色背景区域
    private LinearLayout contentView;//内容区域
    private MyScrollView myScrollView;
    private TitleBar titleBar;
    private int mOrginY;
    private int mDragRange;//拖拽距离范围，拖拽距离范围内松手不处理，超出拖拽范围contentView消失
    private boolean mIsScrollInTop = true;//scrollView滑动到顶部
    private int mDarkViewHeight;

    public PopupLayout(Context context) {
        this(context, null);
    }

    public PopupLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PopupLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(LinearLayout.VERTICAL);
    }

    public enum TitleItemType {
        TYPE_TITLE_BACK,
        TYPE_TITLE_SHARE,
        TYPE_TITLE_SEARCH
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        Log.e("tag", "onFinishInflate");
        if (getChildCount() != 2) {
            throw new IllegalArgumentException("only can 2 child in this view");
        } else {
            if (getChildAt(0) != null) {
                darkView = getChildAt(0);
            } else {
                throw new IllegalArgumentException("child(0) can not be null");
            }

            if (getChildAt(1) instanceof ViewGroup) {
                contentView = (LinearLayout) getChildAt(1);
                if (contentView.getChildAt(0) instanceof TitleBar) {
                    titleBar = (TitleBar) contentView.getChildAt(0);
                }

                if (contentView.getChildAt(1) instanceof MyScrollView) {
                    myScrollView = (MyScrollView) contentView.getChildAt(1);
                    myScrollView.setOverScrollMode(View.OVER_SCROLL_NEVER);
                    myScrollView.setOnScrollLimitListener(new MyScrollView.OnScrollLimitListener() {
                        @Override
                        public void onScrollTop() {
                            mIsScrollInTop = true;
                        }

                        @Override
                        public void onScrollOther() {
                            mIsScrollInTop = false;
                            if (myScrollView.getScrollY() > mOrginY) {//上滑超过一定距离，显示title
                                titleBar.showTitleText();
                            } else {
                                titleBar.hideTitleText();
                            }
                        }

                        @Override
                        public void onScrollBottom() {
                            //Log.e("tag", "myScrollView is scroll in bottom.");
                            mIsScrollInTop = false;
                        }
                    });
                }
            } else {
                throw new IllegalArgumentException("child(1) must be extends ViewGroup");
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.e("tag", "onSizeChanged");
        mOrginY = titleBar.getMeasuredHeight() + UIUtils.getStatusBarHeight(contentView);
        mDragRange = titleBar.getMeasuredHeight();
        mDarkViewHeight = darkView.getMeasuredHeight();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.e("tag", "onMeasure");
        ViewGroup.LayoutParams params = contentView.getLayoutParams();
        params.height = darkView.getMeasuredHeight() - mOrginY;
        setMeasuredDimension(getMeasuredWidth(), contentView.getMeasuredHeight() + darkView
                .getMeasuredHeight());
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Log.e("tag", "onLayout");
    }


    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        Log.e(TAG, "onStartNestedScroll");
        return true;
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int nestedScrollAxes) {
        Log.e(TAG, "onNestedScrollAccepted");
    }

    @Override
    public void onStopNestedScroll(View target) {
        Log.e(TAG, "onStopNestedScroll");
        if (mDarkViewHeight - mOrginY - getScrollY() > mDragRange) {//向下拖拽，超出拖拽限定距离
            dismiss();
        } else if (mDarkViewHeight - mOrginY - getScrollY() > 0) {//向下拖拽，但是没有超出拖拽限定距离
            springback();
        }
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int
            dyUnconsumed) {
        Log.e(TAG, "onNestedScroll");
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        boolean patchDown = dy < 0 && mIsScrollInTop;//下滑
        boolean patchUp = dy > 0 && getScrollY() < (mDarkViewHeight - UIUtils.getStatusBarHeight
                (target));//上滑

        if (patchDown || patchUp) {
            scrollBy(0, dy);
            consumed[1] = dy;
        }
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        return true;
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        //不做拦截 可以传递给子View
        return false;
    }

    @Override
    public int getNestedScrollAxes() {
        Log.e(TAG, "getNestedScrollAxes");
        return 0;
    }

    /**
     * 设置TitleBar的title
     *
     * @param title 要显示的title
     */
    public void setTitleBarText(String title) {
        titleBar.setTitleText(title);
    }

    /**
     * 显示TitleBar的Text
     */
    public void showTitleBarText() {
        titleBar.showTitleText();
    }

    /**
     * 隐藏TitleBar的Text
     */
    public void hideTitleBarText() {
        titleBar.hideTitleText();
    }

    /**
     * 设置TitleBar的返回键背景图片
     *
     * @param resId 返回键背景图片资源id
     */
    public void setTitleBarBackImage(int resId) {
        titleBar.setBackImageResource(resId);
    }

    /**
     * 设置TitleBar控件点击事件监听
     *
     * @param i TitleBar控件点击事件监听
     */
    public void setOnTitleBarClickListener(final ITitleClickListener i) {
        if (i == null)
            throw new IllegalArgumentException("ITitleClickListener can not be null.");

        titleBar.setOnBarClicklistener(new TitleBar.OnBarClicklistener() {
            @Override
            public void onBackClick() {
                i.onTitleBarClicked(TYPE_TITLE_BACK);
            }

            @Override
            public void onShareClick() {
                i.onTitleBarClicked(TYPE_TITLE_SHARE);
            }

            @Override
            public void onSearchClick() {
                i.onTitleBarClicked(TYPE_TITLE_SEARCH);
            }
        });
    }

    /**
     * 弹出内容区域
     */
    public void popup() {
        setVisibility(VISIBLE);
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, mDarkViewHeight - mOrginY);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int top = (int) animation.getAnimatedValue();
                scrollTo(0, top);
            }
        });
        valueAnimator.setDuration(300);
        valueAnimator.start();
    }

    /**
     * 隐藏内容区域
     */
    public void dismiss() {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(getScrollY(), 0);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int top = (int) animation.getAnimatedValue();
                scrollTo(0, top);
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                setVisibility(INVISIBLE);
                destoryCache();//dismiss时销毁数据和重置界面
            }
        });
        valueAnimator.setDuration(300);
        valueAnimator.start();
    }

    /**
     * 滑动到指定位置
     *
     * @param y 指定位置y坐标
     */
    private void smoothScrollTo(int y) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(getScrollY(), darkView.getHeight() - y);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int top = (int) animation.getAnimatedValue();
                scrollTo(0, top);
            }
        });
        valueAnimator.setDuration(100);
        valueAnimator.start();
    }

    /**
     * 销毁缓存数据，重置界面
     */
    private void destoryCache() {
        mIsScrollInTop = true;
        myScrollView.scrollTo(0, 0);
        darkView.setBackgroundResource(R.color.dark);//没有拖动到顶部时darkview背景设置暗色
        titleBar.setBackImageResource(R.mipmap.close);
        titleBar.hideTitleText();
    }

    /**
     * 回弹
     */
    private void springback() {
        smoothScrollTo(mOrginY);
    }

    public interface ITitleClickListener {
        /**
         * TitleBar控件点击事件
         *
         * @param titleItemType TitleBar点击的控件类型
         */
        void onTitleBarClicked(TitleItemType titleItemType);
    }

    @Override
    public void scrollTo(int x, int y) {
        if (y >= mDarkViewHeight - UIUtils.getStatusBarHeight(this)) {
            y = mDarkViewHeight - UIUtils.getStatusBarHeight(this);
            darkView.setBackgroundColor(Color.WHITE);//拖动到顶部时darkview背景设置白色
            titleBar.setBackImageResource(R.mipmap.back);
        } else {
            darkView.setBackgroundResource(R.color.dark);//没有拖动到顶部时darkview背景设置暗色
            titleBar.setBackImageResource(R.mipmap.close);
        }

        if (y != getScrollY()) {
            super.scrollTo(x, y);
        }
    }
}
