package cn.yhq.page.ui;

import com.markmao.pulltorefresh.widget.XExpandableListView;
import com.markmao.pulltorefresh.widget.XExpandableListView.IXListViewListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cn.yhq.page.core.OnPullToRefreshProvider;

public class PullToRefreshExpandableListViewContext implements OnPullToRefreshProvider {
    private XExpandableListView xListView;

    public PullToRefreshExpandableListViewContext(XExpandableListView xListView) {
        this.xListView = xListView;
    }

    @Override
    public void setHaveMoreData(boolean isHaveMoreData) {
        xListView.setHasMoreData(isHaveMoreData);
    }

    @Override
    public void setPullRefreshEnable(boolean enable) {
        xListView.setPullRefreshEnable(enable);
    }

    @Override
    public void setPullLoadMoreEnable(boolean enable) {
        xListView.setPullLoadEnable(enable);
    }

    @Override
    public void onRefreshComplete(boolean success) {
        xListView.stopLoadMore();
        xListView.stopRefresh();
        xListView
                .setRefreshTime(new SimpleDateFormat("MM-dd HH:mm:ss", Locale.CHINA).format(new Date()));
    }

    @Override
    public void setOnRefreshListener(final OnRefreshListener onRefreshListener) {
        xListView.setXListViewListener(new IXListViewListener() {

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
        return xListView.isEnablePullRefresh();
    }

    @Override
    public boolean isPullLoadMoreEnable() {
        return xListView.isEnablePullLoad();
    }
}
