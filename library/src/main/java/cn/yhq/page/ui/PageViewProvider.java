package cn.yhq.page.ui;

import android.view.View;

import cn.yhq.page.R;

/**
 * Created by Yanghuiqiang on 2016/10/12.
 */

class PageViewProvider implements IPageViewProvider {
    private View mPageView;

    public PageViewProvider(View pageView) {
        this.mPageView = pageView;
    }

    @Override
    public View getPageView() {
        return mPageView;
    }

    @Override
    public int getPageLoadingView() {
        return R.layout.loadingview;
    }

    @Override
    public int getPageEmptyView() {
        return R.layout.emptyview;
    }
}
