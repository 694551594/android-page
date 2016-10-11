package cn.yhq.page.core;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yanghuiqiang on 2016/10/11.
 */

public final class PageManager<T, I> {
  private IPageRequester<T, I> mPageRequester;
  private IPageResponse<T> mPageResponse;
  private IPageDataParser<T, I> mPageDataParser;
  private IPageDataCallback mPageDataCallback;
  private Page<I> mPage;
  private List<IPageDataIntercept<I>> mPageDataIntercepts = new ArrayList<>();

  interface IPageDataCallback<I> {
    void onPageDataCallback(PageAction pageAction, List<I> data, boolean haveNextPage,
        boolean isFromCache);

    void onException(Context context, Throwable t);
  }

  PageManager(final Context context, int pageSize) {
    initPageInfo(pageSize);
    this.mPageResponse = new IPageResponse<T>() {

      @Override
      public void onResponse(PageAction pageAction, T response, boolean isFromCache) {
        if (pageAction == PageAction.INIT || pageAction == PageAction.REFRESH) {
          mPage.dataSize = mPageDataParser.getPageTotal(response, isFromCache);
          mPage.init();
        }
        List<I> result = mPageDataParser.getPageList(response, isFromCache);
        if (result != null && result.size() != 0) {
          mPage.mData = result.get(result.size() - 1);
        }

        try {
          List<I> data = getDataWithInterceptorChain(result);
          mPageDataCallback.onPageDataCallback(pageAction, data, mPage.haveNextPage(), isFromCache);
          // 最终要适配的数据
        } catch (Exception e) {
          onException(context, e);
        }
      }

      @Override
      public void onException(Context context, Throwable throwable) {
        mPageDataCallback.onException(context, throwable);
      }
    };
  }

  private List<I> getDataWithInterceptorChain(List<I> result) throws Exception {
    IPageDataIntercept.Chain<I> chain = new PageDataIntercept(0, result);
    return chain.handler(result);
  }

  class PageDataIntercept implements IPageDataIntercept.Chain<I> {
    private final int index;
    private List<I> data;

    PageDataIntercept(int index, List<I> data) {
      this.index = index;
      this.data = data;
    }

    @Override
    public List<I> data() {
      return data;
    }

    @Override
    public List<I> handler(List<I> data) throws Exception {
      if (index < mPageDataIntercepts.size()) {
        IPageDataIntercept.Chain chain = new PageDataIntercept(index + 1, data);
        IPageDataIntercept<I> intercept = mPageDataIntercepts.get(index);
        List<I> interceptData = intercept.intercept(chain);

        if (interceptData == null) {
          throw new NullPointerException("intercept " + intercept + " returned null");
        }

        return interceptData;
      }
      return data;
    }
  }

  void setPageDataIntercepts(List<IPageDataIntercept<I>> intercepts) {
    this.mPageDataIntercepts = intercepts;
  }

  void setPageDataCallback(IPageDataCallback pageDataCallback) {
    this.mPageDataCallback = pageDataCallback;
  }

  void setPageRequester(IPageRequester<T, I> pageRequester) {
    this.mPageRequester = pageRequester;
  }

  void setPageDataParser(IPageDataParser<T, I> pageParser) {
    this.mPageDataParser = pageParser;
  }

  void initPageInfo(int pageSize) {
    mPage = new Page<I>();
    mPage.pageSize = pageSize;
    mPage.currentPage = 1;
  }

  public Page<I> getPage() {
    return mPage;
  }

  void init() {
    mPage.reset();
    mPageRequester.onRequest(PageAction.INIT, mPage, mPageResponse);
  }

  void refresh() {
    mPage.reset();
    mPageRequester.onRequest(PageAction.REFRESH, mPage, mPageResponse);
  }

  void loadMore() {
    mPage.next();
    mPageRequester.onRequest(PageAction.LOADMORE, mPage, mPageResponse);
  }

  void cancel() {
    mPageRequester.onCancel();
  }

}
