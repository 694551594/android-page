package cn.yhq.page.list;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.os.Bundle;

import cn.developer.sdk.page2.core.IPullToRefreshListener;
import cn.developer.sdk.recycler.XRecyclerListView;

/**
 * Created by 杨慧强 on 2016/2/22.
 */
public class PullToRefreshRecyclerListViewContext extends IPullToRefreshListener {
  private final static SimpleDateFormat mSimpleDateFormat =
      new SimpleDateFormat("MM-dd HH:mm:ss", Locale.CHINA);
  private XRecyclerListView xRecyclerView;

  public PullToRefreshRecyclerListViewContext(XRecyclerListView xRecyclerView) {
    this.xRecyclerView = xRecyclerView;
  }

  @Override
  public void setHasMoreData(boolean isHasMoreData) {
    xRecyclerView.setHasMoreData(isHasMoreData);
  }

  @Override
  public void pullRefreshEnable(boolean enable) {
    xRecyclerView.setPullRefreshEnable(enable);
  }

  @Override
  public void pullLoadMoreEnable(boolean enable) {
    xRecyclerView.setPullLoadEnable(enable);
  }

  @Override
  public void onRefreshComplete(boolean success) {
    xRecyclerView.stopLoadMore();
    xRecyclerView.stopRefresh();
    xRecyclerView.setRefreshTime(mSimpleDateFormat.format(new Date()));
  }

  @Override
  public void setOnRefreshListener(final OnRefreshListener onRefreshListener) {
    xRecyclerView.setXListViewListener(new XRecyclerListView.IXListViewListener() {

      @Override
      public void onRefresh() {
        // 刷新的时候设置为还有下一页数据。是否真的有下一页数据获取到数据后再判断
        xRecyclerView.setHasMoreData(true);
        onRefreshListener.pullToRefresh();
      }

      @Override
      public void onLoadMore() {
        onRefreshListener.pullToLoadMore();
      }

    });
  }

  @Override
  public Bundle onSaveInstanceState() {
    Bundle bundle = new Bundle();
    bundle.putBundle("pullInfoBundle", xRecyclerView.onSavePullInfoState());
    return bundle;
  }

  @Override
  public void onRestoreInstanceState(Bundle state) {
    xRecyclerView.onRestorePullInfoState(state.getBundle("pullInfoBundle"));
  }

  @Override
  public boolean pullRefreshEnable() {
    return xRecyclerView.isEnablePullRefresh();
  }

  @Override
  public boolean pullLoadMoreEnable() {
    return xRecyclerView.isEnablePullLoad();
  }
}
