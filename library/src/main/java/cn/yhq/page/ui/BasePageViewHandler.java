package cn.yhq.page.ui;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2017/3/27.
 */

public abstract class BasePageViewHandler<V extends View> extends PageViewHandler<V> {
    private ViewGroup.LayoutParams mParams;
    private ViewGroup mSwipeRefreshLayout;

    @Override
    public void setup(V pageView, View loadingView, View emptyView) {
        super.setup(pageView, loadingView, emptyView);
        ViewGroup viewGroup = (ViewGroup) pageView.getParent();
        if (viewGroup instanceof SwipeRefreshLayout) {
            mSwipeRefreshLayout = viewGroup;
            viewGroup = (ViewGroup) viewGroup.getParent();
        }
        this.mParams = viewGroup.getLayoutParams();
        if (loadingView != null) {
            viewGroup.addView(loadingView, mParams);
        }
        if (emptyView != null) {
            viewGroup.addView(emptyView, mParams);
        }
    }

    public abstract void setEmptyView(V pageView, View view);

    @Override
    public void showPageLoadingView() {
        super.showPageLoadingView();
        if (mLoadingView != null) {
            setEmptyView(mPageView, mLoadingView);
        }
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void showPageEmptyView() {
        super.showPageEmptyView();
        if (mEmptyView != null) {
            setEmptyView(mPageView, mEmptyView);
        }
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void showPageView() {
        super.showPageView();
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setVisibility(View.VISIBLE);
        }
    }

}
