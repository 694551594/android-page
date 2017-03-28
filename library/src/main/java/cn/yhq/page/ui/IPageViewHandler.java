package cn.yhq.page.ui;

import android.view.View;

/**
 * Created by Administrator on 2017/3/27.
 */

public interface IPageViewHandler<V extends View> {

    void setup(V pageView, View loadingView, View emptyView);

    void showPageLoadingView();

    void showPageEmptyView();

    void showPageView();
}
