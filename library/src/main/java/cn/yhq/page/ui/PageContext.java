package cn.yhq.page.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import cn.yhq.page.core.IPageDataIntercept;
import cn.yhq.page.core.PageEngine;
import cn.yhq.page.core.PageManager;

/**
 * Created by Yanghuiqiang on 2016/10/11.
 */

public class PageContext<T, I> {
  private PageEngine<T, I> mPageEngine;
  private PageConfig mPageConfig = new PageConfig();
  private View mPageView;
  private List<IPageDataIntercept<I>> mPageDataIntercepts = new ArrayList<>();

  public PageContext(Context context, IPageContextProvider<T, I> pageContextProvider) {
    pageContextProvider.onPageConfig(mPageConfig);
    pageContextProvider.addPageDataIntercepts(mPageDataIntercepts);
    mPageEngine = new PageEngine.Builder<T, I>(context)
            .setPageSize(mPageConfig.pageSize)
            .setPageAdapter(pageContextProvider.getPageAdapter())
            .setPageParser(pageContextProvider.getPageDataParser())
            .setPageRequester(pageContextProvider.getPageRequester())
            .setOnPullToRefreshProvider(null)
            .setPageDataIntercept(mPageDataIntercepts)
            .build();
    this.mPageView = pageContextProvider.getPageView();
  }

  public void onCreated(Bundle savedInstanceState) {
    try {
      if (savedInstanceState != null) {
        // 恢复配置信息
        mPageConfig.onRestoreInstanceState(savedInstanceState);
        this.initPageData();
      } else {
        if (mPageConfig.autoInitPageData) {
          this.initPageData();
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void onSavePageDataState(Bundle savedInstanceState) {
    try {
      // 保存配置信息，配置信息是必须保存的
      mPageConfig.onSaveInstanceState(savedInstanceState);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public PageManager<T, I> getPageManager() {
    return mPageEngine.getPageManager();
  }

  public void clearPageData() {
    mPageEngine.clearPageData();
  }

  public void initPageData() {
    if (mPageConfig.clearPageDataBeforeRequest) {
      clearPageData();
    }

    mPageEngine.initPageData();
  }

  public void refreshPageData() {
    mPageEngine.refreshPageData();
  }

  public void onDestroy() {
    mPageEngine.cancel();
  }

  public PageConfig getPageConfig() {
    return this.mPageConfig;
  }
}
