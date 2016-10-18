package cn.yhq.page.ui;

import cn.yhq.widget.AutoRefreshListView;

public class PullToRefreshAutoRefreshListViewContext extends PullToRefreshContext<AutoRefreshListView> {

    public PullToRefreshAutoRefreshListViewContext(AutoRefreshListView autoRefreshListView) {
        super(autoRefreshListView);
    }

    @Override
    public void setHaveMoreData(boolean isHaveMoreData) {
        mPageView.setHaveMoreData(isHaveMoreData);
    }

    @Override
    public void setPullRefreshEnable(boolean enable) {
    }

    @Override
    public void setPullLoadMoreEnable(boolean enable) {
        mPageView.setAutoRefreshEnable(enable);
    }

    @Override
    public void onRefreshComplete(int newDataSize, boolean success) {
        mPageView.refreshComplete(newDataSize);
    }

    @Override
    public void setOnRefreshListener(final OnRefreshListener onRefreshListener) {
        mPageView.setOnAutoRefreshListener(new AutoRefreshListView.OnAutoRefreshListener() {
            @Override
            public void autoRefresh(AutoRefreshListView view) {
                onRefreshListener.onPullToLoadMore();
            }
        });
    }

    @Override
    public boolean isPullRefreshEnable() {
        return false;
    }

    @Override
    public boolean isPullLoadMoreEnable() {
        return mPageView.isAutoRefreshEnable();
    }
}
