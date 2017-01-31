package cn.yhq.page.pulltorefresh;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cn.yhq.widget.XRecyclerListView;

public class PullToRefreshXRecyclerListViewContext extends PullToRefreshContext<XRecyclerListView> {
    private final static SimpleDateFormat mSimpleDateFormat =
            new SimpleDateFormat("MM-dd HH:mm:ss", Locale.CHINA);

    public PullToRefreshXRecyclerListViewContext(XRecyclerListView xListView) {
        super(xListView);
    }

    @Override
    public void setHaveMoreData(boolean isHaveMoreData) {
        mPageView.setHasMoreData(isHaveMoreData);
    }

    @Override
    public void setPullRefreshEnable(boolean enable) {
        mPageView.setPullRefreshEnable(enable);
    }

    @Override
    public void setPullLoadMoreEnable(boolean enable) {
        mPageView.setPullLoadEnable(enable);
    }

    @Override
    public void onRefreshComplete(int newDataSize, boolean success) {
        mPageView.stopLoadMore();
        mPageView.stopRefresh();
        mPageView.setRefreshTime(mSimpleDateFormat.format(new Date()));
    }

    @Override
    public void setOnRefreshListener(final OnRefreshListener onRefreshListener) {
        mPageView.setXListViewListener(new XRecyclerListView.IXListViewListener() {

            @Override
            public void onRefresh() {
                // 刷新的时候设置为还有下一页数据。是否真的有下一页数据获取到数据后再判断
                mPageView.setHasMoreData(true);
                onRefreshListener.onPullToRefresh();
            }

            @Override
            public void onLoadMore() {
                onRefreshListener.onPullToLoadMore();
            }

        });
    }

    @Override
    public boolean isPullRefreshEnable() {
        return mPageView.isEnablePullRefresh();
    }

    @Override
    public boolean isPullLoadMoreEnable() {
        return mPageView.isEnablePullLoad();
    }
}
