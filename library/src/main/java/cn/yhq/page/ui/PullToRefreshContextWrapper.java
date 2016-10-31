package cn.yhq.page.ui;

import android.view.View;

public abstract class PullToRefreshContextWrapper<T extends View> extends PullToRefreshContext<T> {
    private PullToRefreshContext<T> mPullToRefreshContext;

    public PullToRefreshContextWrapper(T pageView) {
        super(pageView);
        mPullToRefreshContext = getPullToRefreshContext(pageView);
    }

    public abstract PullToRefreshContext<T> getPullToRefreshContext(T pageView);

    @Override
    public void setHaveMoreData(boolean isHaveMoreData) {
        mPullToRefreshContext.setHaveMoreData(isHaveMoreData);
    }

    @Override
    public void setPullRefreshEnable(boolean enable) {
        mPullToRefreshContext.setPullRefreshEnable(enable);
    }

    @Override
    public void setPullLoadMoreEnable(boolean enable) {
        mPullToRefreshContext.setPullLoadMoreEnable(enable);
    }

    @Override
    public void onRefreshComplete(int newDataSize, boolean success) {
        mPullToRefreshContext.onRefreshComplete(newDataSize, success);
    }

    @Override
    public void setOnRefreshListener(final OnRefreshListener onRefreshListener) {
        mPullToRefreshContext.setOnRefreshListener(onRefreshListener);
    }

    @Override
    public boolean isPullRefreshEnable() {
        return mPullToRefreshContext.isPullRefreshEnable();
    }

    @Override
    public boolean isPullLoadMoreEnable() {
        return mPullToRefreshContext.isPullLoadMoreEnable();
    }
}
