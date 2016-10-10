package cn.yhq.page.core;

import android.os.Bundle;

import java.util.List;

/**
 * Created by 杨慧强 on 2016/2/17.
 */
public class OnPageListenerDispatcher implements OnPageListener {
  private List<OnPageListener> mOnPageListeners;

  public OnPageListenerDispatcher(List<OnPageListener> onPageListeners) {
    this.mOnPageListeners = onPageListeners;
  }

  @Override
  public void onPageRestoreInstanceState(Bundle state) {
    int i = 0;
    for (OnPageListener l : mOnPageListeners) {
      Bundle b = state.getBundle("l_" + i++);
      if (b == null) {
        continue;
      }
      l.onPageRestoreInstanceState(b);
    }
  }

  @Override
  public void onPageSaveInstanceState(Bundle state) {
    int i = 0;
    for (OnPageListener l : mOnPageListeners) {
      Bundle b = new Bundle();
      state.putBundle("l_" + i++, b);
      l.onPageSaveInstanceState(b);
    }
  }

  @Override
  public void onPageCancelRequests() {
    for (OnPageListener l : mOnPageListeners) {
      l.onPageCancelRequests();
    }
  }

  @Override
  public void onPageRequestStart(PageManager.PageRequestType pageRequestType) {
    for (OnPageListener l : mOnPageListeners) {
      l.onPageRequestStart(pageRequestType);
    }
  }

  @Override
  public void onPageLoadComplete(PageManager.PageRequestType pageRequestType, boolean isFromCache,
      boolean isSuccess) {
    for (OnPageListener l : mOnPageListeners) {
      l.onPageLoadComplete(pageRequestType, isFromCache, isSuccess);
    }
  }

  @Override
  public void onPageLoadCache(PageManager.PageRequestType pageRequestType, boolean isHaveCache) {
    for (OnPageListener l : mOnPageListeners) {
      l.onPageLoadCache(pageRequestType, isHaveCache);
    }
  }

  @Override
  public void onPageRefresh() {
    for (OnPageListener l : mOnPageListeners) {
      l.onPageRefresh();
    }
  }

  @Override
  public void onPageLoadMore() {
    for (OnPageListener l : mOnPageListeners) {
      l.onPageLoadMore();
    }
  }

  @Override
  public void onPageException(PageManager.PageRequestType pageRequestType, PageException e) {
    for (OnPageListener l : mOnPageListeners) {
      l.onPageException(pageRequestType, e);
    }
  }
}
