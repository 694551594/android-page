package cn.yhq.page.ui;

import com.markmao.pulltorefresh.widget.XExpandableListView;

public class PullToRefreshSwipeLayoutXExpandableListViewContext extends PullToRefreshSwipeLayoutContext<XExpandableListView> {

    public PullToRefreshSwipeLayoutXExpandableListViewContext(XExpandableListView pageView) {
        super(pageView);
        this.mPageView.setPullRefreshEnable(false);
    }

    @Override
    public void setOnRefreshListener(final OnRefreshListener onRefreshListener) {
        super.setOnRefreshListener(onRefreshListener);
        mPageView.setXListViewListener(new XExpandableListView.IXListViewListener() {

            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                onRefreshListener.onPullToLoadMore();
            }

        });
    }

    @Override
    public void setHaveMoreData(boolean isHaveMoreData) {
        this.mPageView.setHasMoreData(isHaveMoreData);
    }

    @Override
    public void onRefreshComplete(int newDataSize, boolean success) {
        super.onRefreshComplete(newDataSize, success);
        mPageView.stopLoadMore();
    }

    @Override
    public void setPullLoadMoreEnable(boolean enable) {
        this.mPageView.setPullLoadEnable(enable);
    }

    @Override
    public boolean isPullLoadMoreEnable() {
        return this.mPageView.isEnablePullLoad();
    }
}
