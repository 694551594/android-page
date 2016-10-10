package cn.yhq.page.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;

import cn.developer.sdk.base.BaseActivity;
import cn.developer.sdk.page2.core.IPageContextProvider;
import cn.developer.sdk.page2.core.IPageDataHandler;
import cn.developer.sdk.page2.core.OnPageListener;
import cn.developer.sdk.page2.core.PageConfig;
import cn.developer.sdk.page2.core.PageContext;
import cn.developer.sdk.page2.core.PageException;
import cn.developer.sdk.page2.core.PageManager.PageRequestType;

public abstract class PageDataActivity<T, L, I> extends BaseActivity
    implements
      OnPageListener,
      IPageContextProvider<T, L, I> {
  private PageContext<T, L, I> mPageContext;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    onViewCreated(savedInstanceState);
    mPageContext = PageContext.Builder.fromPageContextProvider(this.getContext(), this)
        .addOnPageListener(this).setTag(this.getClass()).build();
    mPageContext.onCreated(savedInstanceState);
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

  @Override
  public void onPageRestoreInstanceState(Bundle state) {}

  @Override
  public void onPageSaveInstanceState(Bundle state) {}

  @Override
  public void onSaveInstanceState(Bundle bundle) {
    super.onSaveInstanceState(bundle);
    mPageContext.onSavePageData(bundle);
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

  /**
   * 分页配置
   *
   * @param pageConfig
   */
  @Override
  public void onPageConfig(PageConfig pageConfig) {}

  public abstract void onViewCreated(Bundle savedInstanceState);

  public final void cancelRequests() {
    mPageContext.cancelRequest();
  }

  @Override
  public void onDestroy() {
    mPageContext.onDestroy();
    super.onDestroy();
  }

  public final PageContext<T, L, I> getPageContext() {
    return mPageContext;
  }

  public final PageConfig getPageConfig() {
    return mPageContext.getPageConfig();
  }

  @Override
  public void onPageCancelRequests() {}

  @Override
  public void onPageRequestStart(PageRequestType pageRequestType) {}

  @Override
  public void onPageLoadComplete(PageRequestType pageRequestType, boolean isFromCache,
      boolean success) {}

  @Override
  public void onPageLoadCache(PageRequestType pageRequestType, boolean isHaveCache) {}

  @Override
  public void onPageRefresh() {}

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
  public void onPageContextBuild(PageContext.Builder<T, L, I> builder) {}
}

