package cn.yhq.page.ui;

import android.view.View;

public abstract class PullToRefreshContextWrapper<PageView extends View> extends PullToRefreshContext<PageView> {
    private PullToRefreshContext<PageView> mPullToRefreshContext;

    public PullToRefreshContextWrapper(PageView pageView) {
        super(pageView);
        mPullToRefreshContext = getPullToRefreshContext(pageView);
    }

    public abstract PullToRefreshContext<PageView> getPullToRefreshContext(PageView pageView);

    @Override
    public void setHaveMoreData(boolean isHaveMoreData) {
        if (mPullToRefreshContext != null) {
            mPullToRefreshContext.setHaveMoreData(isHaveMoreData);
        }
    }

    @Override
    public void setPullRefreshEnable(boolean enable) {
        if (mPullToRefreshContext != null) {
            mPullToRefreshContext.setPullRefreshEnable(enable);
        }
    }

    @Override
    public void setPullLoadMoreEnable(boolean enable) {
        if (mPullToRefreshContext != null) {
            mPullToRefreshContext.setPullLoadMoreEnable(enable);
        }
    }

    @Override
    public void onRefreshComplete(int newDataSize, boolean success) {
        if (mPullToRefreshContext != null) {
            mPullToRefreshContext.onRefreshComplete(newDataSize, success);
        }
    }

    @Override
    public void setOnRefreshListener(final OnRefreshListener onRefreshListener) {
        if (mPullToRefreshContext != null) {
            mPullToRefreshContext.setOnRefreshListener(onRefreshListener);
        }
    }

    @Override
    public boolean isPullRefreshEnable() {
        if (mPullToRefreshContext != null) {
            return mPullToRefreshContext.isPullRefreshEnable();
        }
        return false;
    }

    @Override
    public boolean isPullLoadMoreEnable() {
        if (mPullToRefreshContext != null) {
            return mPullToRefreshContext.isPullLoadMoreEnable();
        }
        return false;
    }
}
