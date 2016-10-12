package cn.yhq.page.ui;

import cn.yhq.page.R;

/**
 * Created by Yanghuiqiang on 2016/10/12.
 */

public abstract class PageViewProvider implements IPageViewProvider {

    @Override
    public int getPageLoadingView() {
        return R.layout.loadingview;
    }

    @Override
    public int getPageEmptyView() {
        return R.layout.emptyview;
    }
}
