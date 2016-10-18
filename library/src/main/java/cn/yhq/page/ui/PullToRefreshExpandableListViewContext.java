package cn.yhq.page.ui;

import com.markmao.pulltorefresh.widget.XExpandableListView;
import com.markmao.pulltorefresh.widget.XExpandableListView.IXListViewListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PullToRefreshExpandableListViewContext extends PullToRefreshContext<XExpandableListView> {

    public PullToRefreshExpandableListViewContext(XExpandableListView pageView) {
        super(pageView);
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
        mPageView
                .setRefreshTime(new SimpleDateFormat("MM-dd HH:mm:ss", Locale.CHINA).format(new Date()));
    }

    @Override
    public void setOnRefreshListener(final OnRefreshListener onRefreshListener) {
        mPageView.setXListViewListener(new IXListViewListener() {

            @Override
            public void onRefresh() {
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
