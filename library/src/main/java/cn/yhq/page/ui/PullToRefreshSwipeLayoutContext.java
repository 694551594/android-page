package cn.yhq.page.ui;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

/**
 * Created by Yanghuiqiang on 2016/10/12.
 */

public class PullToRefreshSwipeLayoutContext<PageView extends View> extends PullToRefreshContext<PageView> {
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public PullToRefreshSwipeLayoutContext(PageView pageView) {
        this((SwipeRefreshLayout) pageView.getParent(), pageView);
    }

    public PullToRefreshSwipeLayoutContext(SwipeRefreshLayout swipeRefreshLayout, PageView pageView) {
        super(pageView);
        this.mSwipeRefreshLayout = swipeRefreshLayout;
        this.mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
    }

    @Override
    public void setHaveMoreData(boolean isHaveMoreData) {
    }

    @Override
    public void setPullRefreshEnable(boolean enable) {
        this.mSwipeRefreshLayout.setEnabled(enable);
    }

    @Override
    public boolean isPullRefreshEnable() {
        return this.mSwipeRefreshLayout.isEnabled();
    }

    @Override
    public void setPullLoadMoreEnable(boolean enable) {
    }

    @Override
    public boolean isPullLoadMoreEnable() {
        return false;
    }

    @Override
    public void onRefreshComplete(int newDataSize, boolean success) {
        this.mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void setOnRefreshListener(final OnRefreshListener onRefreshListener) {
        this.mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onRefreshListener.onPullToRefresh();
            }
        });
    }
}
