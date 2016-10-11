package cn.yhq.page.http;

import android.os.Bundle;

import cn.yhq.page.core.IPageRequester;
import cn.yhq.page.core.PageAction;
import cn.yhq.page.ui.PageDataActivity;

public abstract class RetrofitPageListDataActivity<T, I> extends PageDataActivity<T, I>
    implements
      RetrofitPageRequester.IPageRequestExecutor<T, I> {
  private RetrofitPageRequester<T, I> mRetrofitPageRequester;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    mRetrofitPageRequester = new RetrofitPageRequester<>(this, this);
    super.onCreate(savedInstanceState);
  }

  @Override
  public void onPageLoadComplete(PageAction pageAction, boolean isFromCache, boolean isSuccess) {
    super.onPageLoadComplete(pageAction, isFromCache, isSuccess);
    if (pageAction == PageAction.INIT && isFromCache) {
      this.refreshPageData();
    }
  }

  @Override
  public IPageRequester<T, I> getPageRequester() {
    return mRetrofitPageRequester;
  }


}
