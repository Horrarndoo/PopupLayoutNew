package com.zyw.horrarndoo.popuplayout;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.zyw.horrarndoo.popuplayout.utils.UIUtils;
import com.zyw.horrarndoo.popuplayout.view.PopupLayout;

public class MainActivity extends AppCompatActivity {
    PopupLayout popupLayout;
    RelativeLayout[] rlItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        initStatusBar();
        popupLayout = (PopupLayout) findViewById(R.id.popup_layout);
        popupLayout.setOnTitleBarClickListener(new PopupLayout.ITitleClickListener() {
            @Override
            public void onTitleBarClicked(PopupLayout.TitleItemType titleItemType) {
                switch (titleItemType) {
                    case TYPE_TITLE_BACK:
                        popupLayout.dismiss();
                        break;
                    case TYPE_TITLE_SHARE:
                        Toast.makeText(MainActivity.this, "btn_share is clicked.", Toast
                                .LENGTH_SHORT).show();
                        break;
                    case TYPE_TITLE_SEARCH:
                        Toast.makeText(MainActivity.this, "btn_search is clicked.", Toast
                                .LENGTH_SHORT).show();
                        break;
                }
            }
        });

        rlItems = new RelativeLayout[9];
        rlItems[0] = (RelativeLayout) findViewById(R.id.item_1);
        rlItems[1] = (RelativeLayout) findViewById(R.id.item_2);
        rlItems[2] = (RelativeLayout) findViewById(R.id.item_3);
        rlItems[3] = (RelativeLayout) findViewById(R.id.item_4);
        rlItems[4] = (RelativeLayout) findViewById(R.id.item_5);
        rlItems[5] = (RelativeLayout) findViewById(R.id.item_6);
        rlItems[6] = (RelativeLayout) findViewById(R.id.item_7);
        rlItems[7] = (RelativeLayout) findViewById(R.id.item_8);
        rlItems[8] = (RelativeLayout) findViewById(R.id.item_9);

        for (int i = 0; i < 9; i++) {
            final int finalI = i;
            rlItems[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this, "item" + finalI + " is clicked.", Toast
                            .LENGTH_SHORT).show();
                }
            });
        }

    }

    public void showPopupLayout(View view) {
        popupLayout.popup();
    }

    /**
     * 初始化状态栏状态
     * 设置Activity状态栏透明效果
     * 隐藏ActionBar
     */
    private void initStatusBar() {
        //将状态栏设置成透明色
        UIUtils.setBarColor(this, Color.TRANSPARENT);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }
}
