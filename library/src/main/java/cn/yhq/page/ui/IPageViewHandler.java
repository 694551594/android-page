package cn.yhq.page.ui;

import android.view.View;

/**
 * Created by Administrator on 2017/3/27.
 */

public interface IPageViewHandler<V> {

    void setup(V pageView, View loadingView, View emptyView);

    void showPageLoadingView(V pageView, View loadingView, View emptyView);

    void showPageEmptyView(V pageView, View loadingView, View emptyView);

    void showPageView(V pageView);
}
