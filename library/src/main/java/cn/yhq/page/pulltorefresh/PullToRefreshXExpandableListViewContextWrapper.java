package cn.yhq.page.pulltorefresh;

import android.support.v4.widget.SwipeRefreshLayout;

import com.markmao.pulltorefresh.widget.XExpandableListView;

public class PullToRefreshXExpandableListViewContextWrapper extends PullToRefreshContextWrapper<XExpandableListView> {

    public PullToRefreshXExpandableListViewContextWrapper(XExpandableListView pageView) {
        super(pageView);
    }

    @Override
    public PullToRefreshContext<XExpandableListView> getPullToRefreshContext(XExpandableListView pageView) {
        if (pageView.getParent() instanceof SwipeRefreshLayout) {
            return new PullToRefreshSwipeLayoutXExpandableListViewContext(pageView);
        } else {
            return new PullToRefreshXExpandableListViewContext(pageView);
        }
    }

}
