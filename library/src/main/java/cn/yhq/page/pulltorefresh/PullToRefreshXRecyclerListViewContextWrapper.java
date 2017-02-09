package cn.yhq.page.pulltorefresh;

import android.support.v4.widget.SwipeRefreshLayout;

import cn.yhq.widget.xrecyclerview.XRecyclerListView;


public class PullToRefreshXRecyclerListViewContextWrapper extends PullToRefreshContextWrapper<XRecyclerListView> {

    public PullToRefreshXRecyclerListViewContextWrapper(XRecyclerListView pageView) {
        super(pageView);
    }

    @Override
    public PullToRefreshContext<XRecyclerListView> getPullToRefreshContext(XRecyclerListView pageView) {
        if (pageView.getParent() instanceof SwipeRefreshLayout) {
            return new PullToRefreshSwipeLayoutXRecyclerListViewContext(pageView);
        } else {
            return new PullToRefreshXRecyclerListViewContext(pageView);
        }
    }
}
