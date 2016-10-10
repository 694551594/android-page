package cn.yhq.page.ui;

import android.os.Bundle;

import java.util.List;

import cn.developer.sdk.page2.adapter.IPageListAdapter;
import cn.developer.sdk.page2.list.DefaultPageListContextProvider.IPageResponseParser;
import cn.developer.sdk.page2.list.IPageListDataParser;

public abstract class PageListDataActivity<T, I> extends PageDataActivity<T, List<I>, I> {
  @Deprecated
  private IPageResponseParser<T, I> mPageResponseParser;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    mPageResponseParser = getPageResponseParser();
    super.onCreate(savedInstanceState);
  }

  @Deprecated
  public IPageResponseParser<T, I> getPageResponseParser() {
    return null;
  }

  public abstract IPageListAdapter<I> getPageAdapter();

  @Override
  public IPageListDataParser<T, I> getPageDataParser() {
    return new IPageListDataParser<T, I>() {

      @Override
      public List<I> getPageList(T data, boolean isFromCache) {
        return mPageResponseParser.toPageListData(data, isFromCache);
      }

      @Override
      public long getPageTotal(T data, boolean isFromCache) {
        return mPageResponseParser.toPageListTotal(data, isFromCache);
      }
    };
  }
}
