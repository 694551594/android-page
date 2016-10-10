package cn.yhq.page.ui;

import android.content.Context;
import android.os.Bundle;

import cn.developer.sdk.dialog.DialogBuilder;
import cn.developer.sdk.dialog.IDialogProvider;
import cn.developer.sdk.page2.core.IPageContextProvider;
import cn.developer.sdk.page2.core.IPageDataHandler;
import cn.developer.sdk.page2.core.OnPageListener;
import cn.developer.sdk.page2.core.PageConfig;
import cn.developer.sdk.page2.core.PageContext;
import cn.developer.sdk.page2.core.PageException;
import cn.developer.sdk.page2.core.PageManager;

/**
 * Created by Administrator on 2016/7/25.
 */
public abstract class PageDataDialog<T, L, I>
    implements
      OnPageListener,
      IPageContextProvider<T, L, I> {
  private PageContext<T, L, I> mPageContext;
  private IDialogProvider mDialogProvider;
  private Context mContext;

  public PageDataDialog(Context context) {
    this.mContext = context;
  }

  public void onCreate(Bundle savedInstanceState) {
    onViewCreated(savedInstanceState);
    mPageContext = PageContext.Builder.fromPageContextProvider(this.mContext, this)
        .addOnPageListener(this).setTag(this.getClass()).build();
    mPageContext.onCreated(savedInstanceState);

    DialogBuilder builder = DialogBuilder.builder(this.mContext);
    this.onPageDialogBuild(builder);
    mDialogProvider = builder.setDialogType(DialogBuilder.DIALOG_OTHER)
        .setContentView(this.getPageView()).create();
  }

  public final void show() {
    mDialogProvider.showDialog();
  }

  public final void initPageData() {
    mPageContext.initPageData();
  }

  public final void refreshPageData() {
    mPageContext.forceRefresh();
  }

  @Override
  public void onPageRestoreInstanceState(Bundle state) {}

  @Override
  public void onPageSaveInstanceState(Bundle state) {}

  public void onSaveInstanceState(Bundle bundle) {
    mPageContext.onSavePageData(bundle);
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

  public void onDestroy() {
    mPageContext.onDestroy();
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
  public void onPageRequestStart(PageManager.PageRequestType pageRequestType) {}

  @Override
  public void onPageLoadComplete(PageManager.PageRequestType pageRequestType, boolean isFromCache,
      boolean success) {}

  @Override
  public void onPageLoadCache(PageManager.PageRequestType pageRequestType, boolean isHaveCache) {}

  @Override
  public void onPageRefresh() {}

  @Override
  public void onPageLoadMore() {}

  @Override
  public void onPageException(PageManager.PageRequestType pageRequestType, PageException e) {}

  @Override
  public IPageDataHandler<L> getPageDataHandler() {
    return PageContext.getDefaultPageDataHandler();
  }

  @Override
  public void onPageContextBuild(PageContext.Builder<T, L, I> builder) {}

  public void onPageDialogBuild(DialogBuilder builder) {}
}
