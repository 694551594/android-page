package cn.yhq.page.pulltorefresh;

import cn.yhq.widget.OnLoadMoreListener;
import cn.yhq.widget.XRecyclerListView;

/**
 * Created by yanghuijuan on 2017/1/31.
 */

public class PullToRefreshXRecyclerListViewContext extends PullToRefreshContext<XRecyclerListView> {

    public PullToRefreshXRecyclerListViewContext(XRecyclerListView pageView) {
        super(pageView);
    }

    @Override
    public void setOnRefreshListener(final OnRefreshListener onRefreshListener) {
        this.mPageView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                onRefreshListener.onPullToLoadMore();
            }
        });
    }

    @Override
    public void setHaveMoreData(boolean isHaveMoreData) {
        this.mPageView.setHaveMoreData(isHaveMoreData);
    }

    @Override
    public void setPullRefreshEnable(boolean enable) {

    }

    @Override
    public boolean isPullRefreshEnable() {
        return false;
    }

    @Override
    public void onRefreshComplete(int newDataSize, boolean success) {
        this.mPageView.stopLoadMore();
    }

    @Override
    public void setPullLoadMoreEnable(boolean enable) {
        this.mPageView.setLoadMoreEnable(enable);
    }

    @Override
    public boolean isPullLoadMoreEnable() {
        return this.mPageView.isLoadMoreEnable();
    }
}
