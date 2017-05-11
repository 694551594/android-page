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
        if (emptyView != null) {
            emptyView.setVisibility(View.GONE);
        }
        if (loadingView != null) {
            loadingView.setVisibility(View.GONE);
        }
    }

    @Override
    public void showPageLoadingView() {
        if (mLoadingView != null) {
            mLoadingView.setVisibility(View.VISIBLE);
        }
        if (mEmptyView != null) {
            mEmptyView.setVisibility(View.GONE);
        }
    }

    @Override
    public void showPageEmptyView() {
        if (mLoadingView != null) {
            mLoadingView.setVisibility(View.GONE);
        }
        if (mEmptyView != null) {
            mEmptyView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showPageView() {
        if (mLoadingView != null) {
            mLoadingView.setVisibility(View.GONE);
        }
        if (mEmptyView != null) {
            mEmptyView.setVisibility(View.GONE);
        }
        this.mPageView.setVisibility(View.VISIBLE);
    }
}
