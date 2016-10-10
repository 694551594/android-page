package cn.yhq.page.http;

import android.content.Context;

import cn.developer.sdk.http.core.CacheStrategy;
import cn.developer.sdk.http.core.HttpRequester;
import cn.developer.sdk.http.core.IHttpRequestListener;
import cn.developer.sdk.http.core.IHttpRequestProvider;
import cn.developer.sdk.http.core.IHttpResponseListener;
import cn.developer.sdk.page2.core.Page;
import cn.developer.sdk.page2.core.PageException;
import cn.developer.sdk.page2.core.PageManager.IPageDataRequester;
import cn.developer.sdk.page2.core.PageManager.IPageDataResponseCallback;
import cn.developer.sdk.page2.core.PageManager.PageRequestType;
import retrofit2.Call;

public final class RestPageDataRequester<T, I> implements IPageDataRequester<T, I> {
  public final static int PAGE_REQUEST_CODE = -1;
  private Call<T> mRestCall;
  private Context mContext;
  private IPageDataRequestExecutor<T, I> mPageDataRequestExecutor;

  public interface IPageDataRequestExecutor<T, I> {
    Call<T> executePageRequest(int pageSize, int currentPage, I mData);
  }

  public RestPageDataRequester(Context context,
      IPageDataRequestExecutor<T, I> pageDataRequestExecutor) {
    this.mContext = context;
    this.mPageDataRequestExecutor = pageDataRequestExecutor;
  }

  @Override
  public void onPageDataRequest(final PageRequestType pageRequestType, final Page<I> page,
      final IPageDataResponseCallback<T> callback) {
    mRestCall = new HttpRequester.Builder<T>(mContext).provider(new IHttpRequestProvider<T>() {

      @Override
      public int getRequestCode() {
        return PAGE_REQUEST_CODE;
      }

      @Override
      public Call<T> execute(int requestCode) {
        if (mPageDataRequestExecutor != null) {
          return mPageDataRequestExecutor.executePageRequest(page.pageSize, page.currentPage,
              page.mData);
        }
        return null;
      }

      @Override
      public CacheStrategy getCacheStrategy() {
        if (pageRequestType == PageRequestType.REFRESH
            || pageRequestType == PageRequestType.LOADMORE) {
          return CacheStrategy.ONLY_NETWORK;
        }
        return CacheStrategy.BOTH;
      }

    }).listener(new IHttpResponseListener<T>() {

      @Override
      public void onResponse(Context context, int requestCode, T response, boolean isFromCache) {
        callback.onPageDataResponse(response, isFromCache);
      }

      @Override
      public void onException(Context context, Throwable t) {
        callback.onPageDataResponseException(new PageException(t));
      }

    }).listener((IHttpRequestListener<T>) null).request();

  }

  @Override
  public void onCancelRequest(PageRequestType pageRequestType, Page<I> page) {
    if (mRestCall == null) {
      return;
    }
    mRestCall.cancel();
  }
}
