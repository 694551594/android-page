package cn.yhq.page.http;

import android.os.Bundle;

import cn.developer.sdk.page2.core.PageManager.IPageDataRequester;
import cn.developer.sdk.page2.core.PageManager.PageRequestType;
import cn.developer.sdk.page2.ui.PageListDataActivity;

public abstract class RestPageListDataActivity<T, I> extends PageListDataActivity<T, I>
    implements
      RestPageDataRequester.IPageDataRequestExecutor<T, I> {
  private RestPageDataRequester<T, I> mRestPageDataRequester;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    mRestPageDataRequester = new RestPageDataRequester<>(this, this);
    super.onCreate(savedInstanceState);
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
