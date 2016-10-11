package cn.yhq.page.http;

import android.os.Bundle;

import cn.yhq.page.core.IPageRequester;
import cn.yhq.page.core.PageAction;
import cn.yhq.page.ui.PageDataFragment;

/**
 * 使用retrofit和okhttp封装的分页列表请求处理框架
 * 
 * @author Yanghuiqiang 2015-10-10
 * 
 * @param <T>
 * @param <I>
 */
public abstract class RestPageDataFragment<T, I> extends PageDataFragment<T, I>
    implements
      RetrofitPageRequester.IPageRequestExecutor<T, I> {
  private RetrofitPageRequester<T, I> mRetrofitPageRequester;

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    mRetrofitPageRequester = new RetrofitPageRequester<>(this.getContext(), this);
    super.onActivityCreated(savedInstanceState);
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
