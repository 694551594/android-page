package cn.yhq.page.ui;

import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.markmao.pulltorefresh.widget.XExpandableListView;
import com.markmao.pulltorefresh.widget.XListView;

import java.util.HashMap;
import java.util.Map;

import cn.yhq.widget.AutoRefreshListView;

/**
 * Created by Yanghuiqiang on 2016/10/18.
 */

public final class PullToRefreshContextFactory {
    private final static Map<Class<? extends View>, Class<? extends PullToRefreshContext>> pullToRefreshContexts = new HashMap();

    static {
        // 注册默认的上拉加载下拉刷新组件
        register(AutoRefreshListView.class, PullToRefreshAutoRefreshListViewContext.class);
        register(XListView.class, PullToRefreshXListViewContextWrapper.class);
        register(XExpandableListView.class, PullToRefreshXExpandableListViewContextWrapper.class);
        register(ListView.class, PullToRefreshSwipeLayoutListViewContext.class);
        register(ExpandableListView.class, PullToRefreshSwipeLayoutExpandableListViewContext.class);
    }

    static PullToRefreshContext getPullToRefreshProvider(View pageView) {
        Class<? extends PullToRefreshContext> pullToRefreshContextClass = pullToRefreshContexts.get(pageView.getClass());
        try {
            if (pullToRefreshContextClass == null) {
                return new PullToRefreshDefaultContext(pageView);
            }
            PullToRefreshContext pullToRefreshContext = (PullToRefreshContext) pullToRefreshContextClass.getConstructors()[0].newInstance(pageView);
            return pullToRefreshContext;
        } catch (Exception e) {
            return new PullToRefreshDefaultContext(pageView);
        }
    }

    public static void register(Class<? extends View> viewClass, Class<? extends PullToRefreshContext> pullToRefreshContextClass) {
        pullToRefreshContexts.put(viewClass, pullToRefreshContextClass);
    }
}
