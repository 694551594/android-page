package cn.yhq.page.core;

import android.view.View;
import android.widget.AbsListView;

/**
 * Created by 杨慧强 on 2016/2/20.
 */
public final class PageListViewProvider extends PageViewProvider {

  public PageListViewProvider(AbsListView pageView) {
    super(pageView);
  }

  @Override
  public void setEmptyView(View pageView, View emptyView) {
    ((AbsListView) pageView).setEmptyView(emptyView);
  }

  @Override
  public void setLoadingView(View pageView, View loadingView) {
    ((AbsListView) pageView).setEmptyView(loadingView);
  }
}
