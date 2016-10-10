package cn.yhq.page.core;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import cn.android.developers.sdk.R;

/**
 * Created by 杨慧强 on 2016/2/20.
 */
public abstract class PageViewProvider implements IPageViewProvider {
  private Context mContext;
  private View mLoadingView;
  private View mEmptyView;
  private LayoutInflater inflater;
  private View mPageView;

  public PageViewProvider(View pageView) {
    this.mContext = pageView.getContext();
    this.mPageView = pageView;
    this.inflater = LayoutInflater.from(mContext);
    initView();
  }

  private void initView() {
    mLoadingView = inflater.inflate(R.layout.common_listview_loadingview, null);
    mEmptyView = inflater.inflate(R.layout.common_listview_emptyview, null);
  }

  @Override
  public View getLoadingView() {
    return mLoadingView;
  }

  @Override
  public View getEmptyView() {
    return mEmptyView;
  }

  @Override
  public View getPageView() {
    return mPageView;
  }


}
