package cn.yhq.page.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;

import cn.developer.sdk.base.BaseFragment;
import cn.developer.sdk.page2.core.IPageContextProvider;
import cn.developer.sdk.page2.core.IPageDataHandler;
import cn.developer.sdk.page2.core.OnPageListener;
import cn.developer.sdk.page2.core.PageConfig;
import cn.developer.sdk.page2.core.PageContext;
import cn.developer.sdk.page2.core.PageException;
import cn.developer.sdk.page2.core.PageManager.PageRequestType;

/**
 * 分页列表数据显示的BaseFragment
 * 
 * @author Yanghuiqiang 2015-5-25
 * 
 * @param <T>
 * @param <L>
 * @param <I>
 */
public abstract class PageDataFragment<T, L, I> extends BaseFragment
    implements
      OnPageListener,
      IPageContextProvider<T, L, I> {
  private PageContext<T, L, I> mPageContext;

  /**
   * 此方法是在创建视图后调用的
   * 
   */
  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    mPageContext = PageContext.Builder.fromPageContextProvider(this.getContext(), this)
        .addOnPageListener(this).setTag(this.getClass()).build();
    mPageContext.onCreated(savedInstanceState);
  }

  @Override
  public void onSaveInstanceState(Bundle bundle) {
    super.onSaveInstanceState(bundle);
    mPageContext.onSavePageData(bundle);
  }

  public final void initPageData() {
    mPageContext.initPageData();
  }

  public final void refreshPageData() {
    mPageContext.forceRefresh();
  }

  /**
   * 请调用{@link cn.developer.sdk.page2.ui.PageDataActivity}中的refreshPageData()方法 或者调用
   * {@link cn.developer.sdk.page2.ui.PageDataActivity}中的initPageData()方法
   * 
   */
  @Deprecated
  public final void request() {
    initPageData();
  }

  public final void cancelRequests() {
    mPageContext.cancelRequest();
  }

  @Override
  public void onDestroy() {
    mPageContext.onDestroy();
    super.onDestroy();
  }

  /**
   * 使用getPageView代替
   *
   * @return
   */
  @Deprecated
  public AbsListView getAbsListView() {
    return null;
  }

  public final PageContext<T, L, I> getPageContext() {
    return mPageContext;
  }

  public final PageConfig getPageConfig() {
    return mPageContext.getPageConfig();
  }

  /**
   * 分页配置
   *
   * @param pageConfig
   */
  @Override
  public void onPageConfig(PageConfig pageConfig) {}

  @Override
  public void onPageRestoreInstanceState(Bundle state) {}

  @Override
  public void onPageSaveInstanceState(Bundle state) {}

  @Override
  public void onPageCancelRequests() {}

  @Override
  public void onPageRequestStart(PageRequestType pageRequestType) {}

  @Override
  public void onPageLoadComplete(PageRequestType pageRequestType, boolean isFromCache,
      boolean success) {}

  @Override
  public void onPageRefresh() {}

  @Override
  public void onPageLoadCache(PageRequestType pageRequestType, boolean isHaveCache) {}

  @Override
  public void onPageLoadMore() {}

  @Override
  public void onPageException(PageRequestType pageRequestType, PageException e) {}

  @Override
  public IPageDataHandler<L> getPageDataHandler() {
    return PageContext.getDefaultPageDataHandler();
  }

  @Override
  public View getPageView() {
    return this.getAbsListView();
  }

  @Override
  public void onPageContextBuild(PageContext.Builder<T, L, I> builder) {
  }
}
