package cn.yhq.page.ui;

import android.view.View;

/**
 * 主要提供分页列表视图、正在加载视图以及空视图的布局，如果你想定制正在加载以及空视图的布局，你需要实现此接口。
 * <p>
 * Created by Yanghuiqiang on 2016/10/11.
 */

public interface IPageViewProvider {
    View getPageView();

    int getPageLoadingView();

    int getPageEmptyView();

}
