package cn.yhq.page.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.LinearLayout;

import cn.android.developers.sdk.R;
import cn.developer.sdk.page2.core.Debug;
import cn.developer.sdk.page2.core.OnPageListener;
import cn.developer.sdk.page2.core.OnReRequestListener;
import cn.developer.sdk.page2.core.PageException;
import cn.developer.sdk.page2.core.PageManager.PageRequestType;

/**
 * 默认的分页加载监听，实现加载显示正在加载，没有数据的时候显示空视图，点击空视图重新请求数据的功能
 * 
 * @author Yanghuiqiang 2015-5-22
 * 
 */
@SuppressLint("InflateParams")
@Deprecated
public class DefaultPageListener implements OnPageListener, OnClickListener {
  public final static String TAG = "DefaultPageListener";
  private AbsListView mAbsListView;
  private Context mContext;
  private View mLoadingView;
  private View mEmptyView;
  private LayoutInflater inflater;
  private OnReRequestListener mOnReRequestLitener;
  private boolean isListViewAlwaysVisiable;
  private boolean isLoading;

  public DefaultPageListener(AbsListView absListView) {
    this.mAbsListView = absListView;
    this.mContext = mAbsListView.getContext();
    this.inflater = LayoutInflater.from(mContext);
    initView();
  }

  private void initView() {
    mLoadingView = inflater.inflate(R.layout.common_listview_loadingview, null);
    mEmptyView = inflater.inflate(R.layout.common_listview_emptyview, null);
    mEmptyView.setVisibility(View.GONE);
    mLoadingView.setVisibility(View.GONE);
    LinearLayout ll = (LinearLayout) mEmptyView.findViewById(R.id.listview_emptyview);
    ll.setOnClickListener(this);
    LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    ((ViewGroup) mAbsListView.getParent()).addView(mLoadingView, params);
    ((ViewGroup) mAbsListView.getParent()).addView(mEmptyView, params);
    // ((ViewGroup) mAbsListView.getParent()).setBackgroundResource(R.color.white);
    // mAbsListView.setEmptyView(mEmptyView);
    // 如果listview默认是隐藏的，则也不会显示空视图
    // mAbsListView.getEmptyView().setVisibility(mAbsListView.getVisibility());
  }

  @Override
  public void onClick(View v) {
    if (mOnReRequestLitener != null) {
      mOnReRequestLitener.onReRequest();
    }
  }

  public void setOnReRequestLitener(OnReRequestListener mOnReRequestLitener) {
    this.mOnReRequestLitener = mOnReRequestLitener;
  }

  public void onStart() {
    // if (isListViewAlwaysVisiable) {
    // mAbsListView.setVisibility(View.VISIBLE);
    // return;
    // }
    isHaveCache = false;
    isLoadCache = false;
    isException = false;
    isLoading = true;
    // 请求开始的时候必须要把listview显示出来
    // if (!isListViewAlwaysVisiable) {
    mAbsListView.setVisibility(View.VISIBLE);
    mEmptyView.setVisibility(View.GONE);
    mLoadingView.setVisibility(View.VISIBLE);
    mAbsListView.setEmptyView(mLoadingView);
    // }
  }

  public void onComplete() {
    if (isListViewAlwaysVisiable) {
      mEmptyView.setVisibility(View.GONE);
      mLoadingView.setVisibility(View.GONE);
      mAbsListView.setVisibility(View.VISIBLE);
      mAbsListView.setEmptyView(null);
    } else {
      mEmptyView.setVisibility(View.VISIBLE);
      mLoadingView.setVisibility(View.GONE);
      mAbsListView.setVisibility(View.VISIBLE);
      mAbsListView.setEmptyView(mEmptyView);
    }
    isLoading = false;
  }

  private boolean isHaveCache;
  private boolean isLoadCache;
  private boolean isException;

  @Override
  public void onPageCancelRequests() {
    onComplete();
  }

  @Override
  public void onPageRequestStart(PageRequestType pageRequestType) {
    onStart();
  }

  @Override
  public void onPageLoadComplete(PageRequestType pageRequestType, boolean isFromCache,
      boolean success) {
    if (isFromCache && !isHaveCache) {
      return;
    }
    if (success) {
      // 如果请求成功
      onComplete();
    }
    if (isListViewAlwaysVisiable) {
      // mAbsListView.setVisibility(View.VISIBLE);
      onComplete();
    }
  }

  @Override
  public void onPageRefresh() {}

  @Override
  public void onPageLoadMore() {}

  @Override
  public void onPageException(PageRequestType pageRequestType, PageException e) {
    if (!TextUtils.isEmpty(e.getLocalizedMessage())) {
      // Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }
    Debug.debug(TAG, e);
    if (e.getCause() != null) {
      Debug.debug(TAG, e.getCause());
    }
    this.isException = true;
    // 出现异常的时候，如果加载了缓存，而且没有缓存，则显示完成界面
    if (isLoadCache && !isHaveCache) {
      onComplete();
    }
    // 如果有缓存，则会回调onPageLoadComplete，所以这个地方不需要做判断
  }

  @Override
  public void onPageLoadCache(PageRequestType pageRequestType, boolean isHaveCache) {
    this.isLoadCache = true;
    this.isHaveCache = isHaveCache;
    // 加载缓存的时候，如果没有缓存，而且出现了异常，则显示完成界面
    if (!isHaveCache && isException) {
      onComplete();
    }
    // 如果有缓存，则会回调onPageLoadComplete，所以这个地方不需要做判断
  }

  @Override
  public void onPageRestoreInstanceState(Bundle state) {
    this.setListViewAlwaysVisiable(state.getBoolean("isListViewAlwaysVisiable"));
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
    state.putBoolean("isListViewAlwaysVisiable", isListViewAlwaysVisiable);
  }

  public void setListViewAlwaysVisiable(boolean isListViewAlwaysVisiable) {
    this.isListViewAlwaysVisiable = isListViewAlwaysVisiable;
  }

}
