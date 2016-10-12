package cn.yhq.page.ui;

import android.view.View;

/**
 * Created by Yanghuiqiang on 2016/10/11.
 */

public interface IPageViewProvider {
    View getPageView();

    int getPageLoadingView();

    int getPageEmptyView();
}
