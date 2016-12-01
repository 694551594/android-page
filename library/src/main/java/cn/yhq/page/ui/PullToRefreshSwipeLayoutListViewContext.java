package cn.yhq.page.ui;

import com.markmao.pulltorefresh.widget.XListView;

/**
 * Created by Yanghuiqiang on 2016/10/12.
 */

public class PullToRefreshSwipeLayoutListViewContext extends PullToRefreshSwipeLayoutContext<XListView> {

    public PullToRefreshSwipeLayoutListViewContext(XListView pageView) {
        super(pageView);
        this.mPageView.setPullRefreshEnable(false);
    }

    @Override
    public void setOnRefreshListener(final OnRefreshListener onRefreshListener) {
        super.setOnRefreshListener(onRefreshListener);
        this.mPageView.setXListViewListener(new XListView.IXListViewListener() {

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
}
