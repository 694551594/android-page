package cn.yhq.page.ui;

import java.util.List;

import android.content.Context;
import android.os.Bundle;

import cn.developer.sdk.page2.adapter.IPageListAdapter;
import cn.developer.sdk.page2.core.PageManager;
import cn.developer.sdk.page2.list.DefaultPageListContextProvider;
import cn.developer.sdk.page2.list.IPageListDataParser;

/**
 * Created by Administrator on 2016/7/25.
 */
public abstract class PageListDataDialog<T, I> extends PageDataDialog<T, List<I>, I> {
  private DefaultPageListContextProvider.IPageResponseParser<T, I> mPageResponseParser;

  public PageListDataDialog(Context context) {
    super(context);
  }

  public void onCreate(Bundle savedInstanceState) {
    mPageResponseParser = getPageResponseParser();
    super.onCreate(savedInstanceState);
  }

  public abstract DefaultPageListContextProvider.IPageResponseParser<T, I> getPageResponseParser();

  public abstract IPageListAdapter<I> getPageAdapter();

  @Override
  public PageManager.IPageDataParser<T, List<I>, I> getPageDataParser() {
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
