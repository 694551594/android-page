package cn.yhq.page.core;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by 杨慧强 on 2016/2/20.
 */
final class PageViewManager implements OnPageListener {
  public final static String TAG = "PageViewManager";
  private IPageViewProvider mPageViewProvider;
  private View mLoadingView;
  private View mEmptyView;
  private View mPageView;
  private OnReRequestListener mOnReRequestListener;
  private boolean isListViewAlwaysVisible;
  private boolean isLoading;

  public PageViewManager() {}

  private void initView() {
    if (mPageViewProvider == null) {
      return;
    }
    mPageView = mPageViewProvider.getPageView();
    mLoadingView = mPageViewProvider.getLoadingView();
    mEmptyView = mPageViewProvider.getEmptyView();
    mEmptyView.setVisibility(View.GONE);
    mLoadingView.setVisibility(View.GONE);
    mEmptyView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (mOnReRequestListener != null) {
          mOnReRequestListener.onReRequest();
        }
      }
    });
    ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT);
    ((ViewGroup) mPageView.getParent()).addView(mLoadingView, params);
    ((ViewGroup) mPageView.getParent()).addView(mEmptyView, params);
  }

  public void setPageViewProvider(IPageViewProvider pageViewProvider) {
    this.mPageViewProvider = pageViewProvider;
    initView();
  }

  public void setOnReRequestListener(OnReRequestListener onReRequestListener) {
    this.mOnReRequestListener = onReRequestListener;
  }

  public void onStart() {
    // 请求开始的时候必须要把listview显示出来
    // if (!isListViewAlwaysVisible) {
    mPageView.setVisibility(View.VISIBLE);
    mEmptyView.setVisibility(View.GONE);
    mLoadingView.setVisibility(View.VISIBLE);
    // mPageView.setEmptyView(mLoadingView);
    mPageViewProvider.setLoadingView(mPageView, mLoadingView);
    // }
  }

  public void onComplete() {
    if (isListViewAlwaysVisible) {
      mEmptyView.setVisibility(View.GONE);
      mLoadingView.setVisibility(View.GONE);
      mPageView.setVisibility(View.VISIBLE);
      // mPageView.setEmptyView(null);
      mPageViewProvider.setEmptyView(mPageView, null);
    } else {
      mEmptyView.setVisibility(View.VISIBLE);
      mLoadingView.setVisibility(View.GONE);
      mPageView.setVisibility(View.VISIBLE);
      // mPageView.setEmptyView(mEmptyView);
      mPageViewProvider.setEmptyView(mPageView, mEmptyView);
    }
    isLoading = false;
  }

  @Override
  public void onPageCancelRequests() {
    onComplete();
  }

  @Override
  public void onPageRequestStart(PageManager.PageRequestType pageRequestType) {
    onStart();
  }

  @Override
  public void onPageLoadComplete(PageManager.PageRequestType pageRequestType, boolean isFromCache,
      boolean success) {
    if (pageRequestType == PageManager.PageRequestType.INIT
        || pageRequestType == PageManager.PageRequestType.REFRESH) {
      onComplete();
    }
  }

  @Override
  public void onPageRefresh() {}

  @Override
  public void onPageLoadMore() {}

  @Override
  public void onPageException(PageManager.PageRequestType pageRequestType, PageException e) {}

  @Override
  public void onPageLoadCache(PageManager.PageRequestType pageRequestType, boolean isHaveCache) {}

  @Override
  public void onPageRestoreInstanceState(Bundle state) {
    this.setListViewAlwaysVisible(state.getBoolean("isListViewAlwaysVisible"));
    // TODO asynctask状态保存的问题，如果发生屏幕旋转等问题的时候会到时asynctask不能继续执行，所以暂时先显示重试的界面。等待状态保存问题解决后再继续。
    // if (state.getBoolean("isLoading")) {
    // onStart();
    // } else {
    // onComplete();
    // }
    onComplete();
  }

  @Override
  public void onPageSaveInstanceState(Bundle state) {
    state.putBoolean("isLoading", isLoading);
    state.putBoolean("isListViewAlwaysVisible", isListViewAlwaysVisible);
  }

  public void setListViewAlwaysVisible(boolean isListViewAlwaysVisible) {
    this.isListViewAlwaysVisible = isListViewAlwaysVisible;
  }
}
