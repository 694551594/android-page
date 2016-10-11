package cn.yhq.page.ui;

import android.os.Bundle;

import java.util.List;

import cn.yhq.dialog.BaseFragment;
import cn.yhq.page.core.IPageDataIntercept;
import cn.yhq.page.core.OnPageListener;
import cn.yhq.page.core.PageAction;


/**
 * 分页列表数据显示的BaseFragment
 * 
 * @author Yanghuiqiang 2015-5-25
 * 
 * @param <T>
 * @param <I>
 */
public abstract class PageDataFragment<T, I> extends BaseFragment
    implements
      OnPageListener,
      IPageContextProvider<T, I> {
  private PageContext<T, I> mPageContext;

  /**
   * 此方法是在创建视图后调用的
   * 
   */
  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    mPageContext = new PageContext<>(this.getContext(), this);
    mPageContext.onCreated(savedInstanceState);
  }

  @Override
  public void onSaveInstanceState(Bundle bundle) {
    super.onSaveInstanceState(bundle);
    mPageContext.onSavePageDataState(bundle);
  }

  public final void initPageData() {
    mPageContext.initPageData();
  }

  public final void refreshPageData() {
    mPageContext.refreshPageData();
  }

  /**
   * 分页配置
   *
   * @param pageConfig
   */
  @Override
  public void onPageConfig(PageConfig pageConfig) {}

  @Override
  public void onDestroy() {
    mPageContext.onDestroy();
    super.onDestroy();
  }

  public final PageContext<T, I> getPageContext() {
    return mPageContext;
  }

  public final PageConfig getPageConfig() {
    return mPageContext.getPageConfig();
  }

  @Override
  public void addPageDataIntercepts(List<IPageDataIntercept<I>> pageDataIntercepts) {

  }

  @Override
  public void onPageCancelRequests() {

  }

  @Override
  public void onPageRequestStart(PageAction pageAction) {

  }

  @Override
  public void onPageLoadComplete(PageAction pageAction, boolean isFromCache, boolean isSuccess) {

  }

  @Override
  public void onPageLoadCache(PageAction pageAction, boolean isHaveCache) {

  }

  @Override
  public void onPageRefresh() {

  }

  @Override
  public void onPageLoadMore() {

  }

  @Override
  public void onPageInit() {

  }

  @Override
  public void onPageException(Throwable e) {

  }
}
