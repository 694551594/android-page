package cn.yhq.page.sample;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;

import cn.yhq.base.BaseActivity;

/**
 * Created by Yanghuiqiang on 2016/11/30.
 */

public class SwipeLayoutActivity extends BaseActivity {
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_swipe_layout;
    }

    @Override
    protected void onViewCreated(Bundle savedInstanceState) {
        super.onViewCreated(savedInstanceState);
        mSwipeRefreshLayout = this.getView(R.id.swiperefreshlayout);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(10000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mSwipeRefreshLayout.setRefreshing(false);
                            }
                        });
                    }
                }).start();
            }
        });
    }
}
