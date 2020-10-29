package com.example.dysmartrefresh;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.SimpleMultiPurposeListener;

public class MainActivity extends AppCompatActivity {
    private SmartRefreshLayout mRefreshLayout;
    private RelativeLayout rl_video_title;
    private RelativeLayout rl_title_shop;
    private FrameLayout fl_title_refresh;
    private TextView tv_stop;
    private GLoadingView loadingview;
    private boolean isRefresh;
    private int mOffset = 0;//下滑距离
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRefreshLayout = (SmartRefreshLayout) findViewById(R.id.refreshLayout);
        rl_video_title = (RelativeLayout) findViewById(R.id.rl_video_title);
        rl_title_shop = (RelativeLayout) findViewById(R.id.rl_title_shop);
        fl_title_refresh = (FrameLayout) findViewById(R.id.fl_title_refresh);
        loadingview = (GLoadingView) findViewById(R.id.loadingview);
        tv_stop=(TextView) findViewById(R.id.tv_stop);
        mRefreshLayout.setEnabled(true);
        mRefreshLayout.setOnMultiPurposeListener(new SimpleMultiPurposeListener() {
            @Override
            public void onHeaderPulling(RefreshHeader header, float percent, int offset, int bottomHeight, int extendHeight) {
                mOffset = offset;
                rl_video_title.setTranslationY(mOffset);
                if (mOffset < 160 ) {
                    rl_title_shop.setVisibility(View.VISIBLE);
                    fl_title_refresh.setVisibility(View.INVISIBLE);
                    rl_title_shop.setAlpha(1 - Math.min(percent, 1));
                } else if (mOffset >= 160) {
                    rl_title_shop.setVisibility(View.INVISIBLE);
                    fl_title_refresh.setVisibility(View.VISIBLE);
                }else if(mOffset==0){
                    rl_title_shop.setVisibility(View.VISIBLE);
                    fl_title_refresh.setVisibility(View.INVISIBLE);
                    rl_title_shop.setAlpha(1 - Math.min(percent, 1));
                }
                Log.e("onHeaderPulling--11--", mOffset + "");
            }

            @Override
            public void onHeaderReleasing(RefreshHeader header, float percent, int offset, int bottomHeight, int extendHeight) {
                if (mOffset == 0) {
                    mOffset = 0;
                } else {
                    mOffset = offset;
                }
                if (!isRefresh) {
                    rl_title_shop.setVisibility(View.VISIBLE);
                    fl_title_refresh.setVisibility(View.INVISIBLE);
                } else {
                    rl_title_shop.setVisibility(View.INVISIBLE);
                    fl_title_refresh.setVisibility(View.VISIBLE);
                }
                rl_video_title.setTranslationY(mOffset);
                Log.e("onHeaderReleasing--22--", mOffset + "");
            }

            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                super.onRefresh(refreshlayout);
                isRefresh = true;
                mOffset = 0;
                rl_video_title.setTranslationY(0);
                rl_title_shop.setVisibility(View.INVISIBLE);
                fl_title_refresh.setVisibility(View.VISIBLE);
                loadingview.start();
                loadingview.setVisibility(View.VISIBLE);
                Log.e("onRefresh--33--", mOffset + "");
            }

            @Override
            public void onHeaderFinish(RefreshHeader header, boolean success) {
                super.onHeaderFinish(header, success);
                isRefresh = false;
                rl_title_shop.setVisibility(View.VISIBLE);
                rl_title_shop.setAlpha(1);
                fl_title_refresh.setVisibility(View.INVISIBLE);
                Log.e("onHeaderFinish-44--", mOffset + "");
                loadingview.stop();
                loadingview.setVisibility(View.GONE);
            }
        });
        tv_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRefreshLayout.setEnableRefresh(true);
                mRefreshLayout.finishRefresh();
            }
        });
    }
}
