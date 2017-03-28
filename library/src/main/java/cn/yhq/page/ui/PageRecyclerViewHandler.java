package cn.yhq.page.ui;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;

import cn.yhq.widget.xrecyclerview.BaseRecyclerView;

/**
 * Created by Administrator on 2017/3/27.
 */

public class PageRecyclerViewHandler extends PageViewHandler<BaseRecyclerView> {
    private ViewGroup.LayoutParams mParams;
    private ViewGroup mSwipeRefreshLayout;

    @Override
    public void setup(BaseRecyclerView pageView, View loadingView, View emptyView) {
        super.setup(pageView, loadingView, emptyView);
        ViewGroup viewGroup = (ViewGroup) pageView.getParent();
        if (viewGroup instanceof SwipeRefreshLayout) {
            mSwipeRefreshLayout = viewGroup;
            viewGroup = (ViewGroup) viewGroup.getParent();
        }
        this.mParams = viewGroup.getLayoutParams();
        viewGroup.addView(loadingView, mParams);
        viewGroup.addView(emptyView, mParams);
        emptyView.setVisibility(View.GONE);
        loadingView.setVisibility(View.GONE);
    }

    @Override
    public void showPageLoadingView() {
        mPageView.setEmptyView(mLoadingView);
        mLoadingView.setVisibility(View.VISIBLE);
        mEmptyView.setVisibility(View.GONE);
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void showPageEmptyView() {
        mPageView.setEmptyView(mEmptyView);
        mEmptyView.setVisibility(View.VISIBLE);
        mLoadingView.setVisibility(View.GONE);
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
