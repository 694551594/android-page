package cn.yhq.page.pulltorefresh;

import android.view.View;

import cn.yhq.page.core.OnPullToRefreshProvider;

/**
 * Created by Yanghuiqiang on 2016/10/18.
 */

public abstract class PullToRefreshContext<T extends View> implements OnPullToRefreshProvider {
    protected T mPageView;

    public PullToRefreshContext(T pageView) {
        this.mPageView = pageView;
    }
}
