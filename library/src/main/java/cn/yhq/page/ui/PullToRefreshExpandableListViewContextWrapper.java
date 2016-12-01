package cn.yhq.page.ui;

import android.support.v4.widget.SwipeRefreshLayout;

import com.markmao.pulltorefresh.widget.XExpandableListView;

public class PullToRefreshExpandableListViewContextWrapper extends PullToRefreshContextWrapper<XExpandableListView> {

    public PullToRefreshExpandableListViewContextWrapper(XExpandableListView pageView) {
        super(pageView);
    }

    @Override
    public PullToRefreshContext<XExpandableListView> getPullToRefreshContext(XExpandableListView pageView) {
        if (pageView.getParent() instanceof SwipeRefreshLayout) {
            return new PullToRefreshSwipeLayoutExpandableListViewContext(pageView);
        } else {
            return new PullToRefreshExpandableListViewContext(pageView);
        }
    }

}
