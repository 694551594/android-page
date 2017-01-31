package cn.yhq.page.pulltorefresh;

import cn.yhq.widget.XRecyclerListView;

/**
 * Created by Yanghuiqiang on 2016/10/12.
 */

public class PullToRefreshSwipeLayoutXRecyclerListViewContext extends PullToRefreshSwipeLayoutContext<XRecyclerListView> {

    public PullToRefreshSwipeLayoutXRecyclerListViewContext(XRecyclerListView pageView) {
        super(pageView);
        this.mPageView.setPullRefreshEnable(false);
    }

    @Override
    public void setOnRefreshListener(final OnRefreshListener onRefreshListener) {
        super.setOnRefreshListener(onRefreshListener);
        this.mPageView.setXListViewListener(new XRecyclerListView.IXListViewListener() {

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
        this.mPageView.stopLoadMore();
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
