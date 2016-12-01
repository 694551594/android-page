package cn.yhq.page.ui;

import com.markmao.pulltorefresh.widget.XExpandableListView;

public class PullToRefreshSwipeLayoutExpandableListViewContext extends PullToRefreshSwipeLayoutContext<XExpandableListView> {

    public PullToRefreshSwipeLayoutExpandableListViewContext(XExpandableListView pageView) {
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
}
