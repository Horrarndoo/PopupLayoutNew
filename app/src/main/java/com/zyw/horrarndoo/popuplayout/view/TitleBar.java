package com.zyw.horrarndoo.popuplayout.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zyw.horrarndoo.popuplayout.R;

/**
 * Created by Horrarndoo on 2017/3/27.
 * 自定义title bar
 */

public class TitleBar extends LinearLayout implements View.OnClickListener {
    private Button btn_back;
    private Button btn_share, btn_search;
    private RelativeLayout rl_title;
    private TextView tv_title;

    private OnBarClicklistener onBarClicklistener;

    /**
     * 设置TitleBar点击事件监听
     *
     * @param onBarClicklistener TitleBar点击事件监听
     */
    public void setOnBarClicklistener(OnBarClicklistener onBarClicklistener) {
        this.onBarClicklistener = onBarClicklistener;
    }

    public TitleBar(Context context) {
        this(context, null);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() != 4) {
            throw new IllegalArgumentException("only can 4 child in this view");
        }

        btn_back = (Button) getChildAt(0);
        rl_title = (RelativeLayout) getChildAt(1);
        tv_title = (TextView) rl_title.getChildAt(0);
        btn_share = (Button) getChildAt(2);
        btn_search = (Button) getChildAt(3);
        btn_back.setOnClickListener(this);
        btn_share.setOnClickListener(this);
        btn_search.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                if (onBarClicklistener != null) {
                    onBarClicklistener.onBackClick();
                }
                break;
            case R.id.btn_share:
                if (onBarClicklistener != null) {
                    onBarClicklistener.onShareClick();
                }
                break;
            case R.id.btn_search:
                if (onBarClicklistener != null) {
                    onBarClicklistener.onSearchClick();
                }
                break;
        }
    }

    public interface OnBarClicklistener {
        /**
         * TitleBar中返回按钮点击监听
         */
        void onBackClick();

        /**
         * TitleBar中分享按钮点击监听
         */
        void onShareClick();

        /**
         * TitleBar中搜索按钮点击监听
         */
        void onSearchClick();
    }

    /**
     * 设置TitleBar title
     *
     * @param title title
     */
    public void setTitleText(String title) {
        tv_title.setText(title);
    }

    /**
     * 显示TitleBar的Text
     */
    public void showTitleText() {
        tv_title.setVisibility(VISIBLE);
    }

    /**
     * 隐藏TitleBar的Text
     */
    public void hideTitleText() {
        tv_title.setVisibility(INVISIBLE);
    }

    /**
     * 设置返回键背景图片
     * @param resId 背景图片资源id
     */
    public void setBackImageResource(int resId) {
        btn_back.setBackgroundResource(resId);
    }
}
