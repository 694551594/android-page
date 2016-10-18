package cn.yhq.page.ui;

import android.support.v4.widget.SwipeRefreshLayout;

import com.markmao.pulltorefresh.widget.XExpandableListView;

public class PullToRefreshExpandableListViewContextWrapper extends PullToRefreshContext<XExpandableListView> {
    private PullToRefreshContext<XExpandableListView> mPullToRefreshContext;

    public PullToRefreshExpandableListViewContextWrapper(XExpandableListView pageView) {
        super(pageView);
        if (pageView.getParent() instanceof SwipeRefreshLayout) {
            mPullToRefreshContext = new PullToRefreshSwipeLayoutExpandableListViewContext((SwipeRefreshLayout) pageView.getParent(), pageView);
        } else {
            mPullToRefreshContext = new PullToRefreshExpandableListViewContext(pageView);
        }
    }

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
