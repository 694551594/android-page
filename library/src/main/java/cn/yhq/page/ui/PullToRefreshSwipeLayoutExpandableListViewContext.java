package cn.yhq.page.ui;

import android.support.v4.widget.SwipeRefreshLayout;

import com.markmao.pulltorefresh.widget.XExpandableListView;

public class PullToRefreshSwipeLayoutExpandableListViewContext extends PullToRefreshExpandableListViewContext {
    SwipeRefreshLayout swipeRefreshLayout;

    public PullToRefreshSwipeLayoutExpandableListViewContext(SwipeRefreshLayout swipeRefreshLayout, XExpandableListView xListView) {
        super(xListView);
        this.swipeRefreshLayout = swipeRefreshLayout;
    }

    @Override
    public void setPullRefreshEnable(boolean enable) {
        super.setPullRefreshEnable(false);
    }

    @Override
    public boolean isPullRefreshEnable() {
        return false;
    }

    @Override
    public boolean isPullLoadMoreEnable() {
        return true;
    }

    @Override
    public void onRefreshComplete(boolean success) {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void setOnRefreshListener(final OnRefreshListener onRefreshListener) {
        super.setOnRefreshListener(onRefreshListener);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onRefreshListener.onPullToRefresh();
            }
        });
    }
}
