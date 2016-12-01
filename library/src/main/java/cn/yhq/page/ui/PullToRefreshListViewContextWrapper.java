package cn.yhq.page.ui;

import android.support.v4.widget.SwipeRefreshLayout;

import com.markmao.pulltorefresh.widget.XListView;

public class PullToRefreshListViewContextWrapper extends PullToRefreshContextWrapper<XListView> {

    public PullToRefreshListViewContextWrapper(XListView pageView) {
        super(pageView);
    }

    @Override
    public PullToRefreshContext<XListView> getPullToRefreshContext(XListView pageView) {
        if (pageView.getParent() instanceof SwipeRefreshLayout) {
            return new PullToRefreshSwipeLayoutListViewContext(pageView);
        } else {
            return new PullToRefreshListViewContext(pageView);
        }
    }
}
