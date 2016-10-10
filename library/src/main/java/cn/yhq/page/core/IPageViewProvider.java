package cn.yhq.page.core;

import android.view.View;

/**
 * Created by 杨慧强 on 2016/2/20.
 */
public interface IPageViewProvider {

  View getLoadingView();

  View getEmptyView();

  View getPageView();

  void setEmptyView(View pageView, View emptyView);

  void setLoadingView(View pageView, View loadingView);
}
