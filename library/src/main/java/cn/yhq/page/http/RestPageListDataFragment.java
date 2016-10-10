package cn.yhq.page.http;

import android.os.Bundle;

import cn.developer.sdk.page2.core.PageManager.IPageDataRequester;
import cn.developer.sdk.page2.core.PageManager.PageRequestType;
import cn.developer.sdk.page2.ui.PageListDataFragment;

/**
 * 使用retrofit和okhttp封装的分页列表请求处理框架
 * 
 * @author Yanghuiqiang 2015-10-10
 * 
 * @param <T>
 * @param <I>
 */
public abstract class RestPageListDataFragment<T, I> extends PageListDataFragment<T, I>
    implements
      RestPageDataRequester.IPageDataRequestExecutor<T, I> {
  private RestPageDataRequester<T, I> mRestPageDataRequester;

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    mRestPageDataRequester = new RestPageDataRequester<>(this.getContext(), this);
    super.onActivityCreated(savedInstanceState);
  }

  @Override
  public IPageDataRequester<T, I> getPageDataRequester() {
    return mRestPageDataRequester;
  }

  @Override
  public void onPageLoadComplete(PageRequestType pageRequestType, boolean isFromCache,
      boolean success) {
    super.onPageLoadComplete(pageRequestType, isFromCache, success);
    if (pageRequestType == PageRequestType.INIT && isFromCache) {
      this.refreshPageData();
    }
  }
}
