package cn.yhq.page.core;

import android.view.View;

import cn.developer.sdk.recycler.BaseRecyclerView;

/**
 * Created by 杨慧强 on 2016/2/22.
 */
public class PageRecyclerViewProvider extends PageViewProvider {

  public PageRecyclerViewProvider(BaseRecyclerView pageView) {
    super(pageView);
  }

  @Override
  public void setEmptyView(View pageView, View emptyView) {
    ((BaseRecyclerView) pageView).setEmptyView(emptyView);
  }

  @Override
  public void setLoadingView(View pageView, View loadingView) {
    ((BaseRecyclerView) pageView).setEmptyView(loadingView);
  }
}
