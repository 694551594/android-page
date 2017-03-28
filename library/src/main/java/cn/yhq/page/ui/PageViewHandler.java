package cn.yhq.page.ui;

import android.view.View;

/**
 * Created by Administrator on 2017/3/28.
 */

public class PageViewHandler<V extends View> implements IPageViewHandler<V> {
    protected V mPageView;
    protected View mLoadingView;
    protected View mEmptyView;

    @Override
    public void setup(V pageView, View loadingView, View emptyView) {
        this.mPageView = pageView;
        this.mLoadingView = loadingView;
        this.mEmptyView = emptyView;
    }

    @Override
    public void showPageLoadingView() {

    }

    @Override
    public void showPageEmptyView() {

    }

    @Override
    public void showPageView() {
        this.mPageView.setVisibility(View.VISIBLE);
    }
}
