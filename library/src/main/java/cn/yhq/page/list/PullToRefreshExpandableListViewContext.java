package cn.yhq.page.list;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.os.Bundle;

import com.markmao.pulltorefresh.widget.XExpandableListView;
import com.markmao.pulltorefresh.widget.XExpandableListView.IXListViewListener;

import cn.developer.sdk.page2.core.IPullToRefreshListener;

public class PullToRefreshExpandableListViewContext extends IPullToRefreshListener {
  private XExpandableListView xListView;

  public PullToRefreshExpandableListViewContext(XExpandableListView xListView) {
    this.xListView = xListView;
  }

  @Override
  public void setHasMoreData(boolean isHasMoreData) {
    xListView.setHasMoreData(isHasMoreData);
  }

  @Override
  public void pullRefreshEnable(boolean enable) {
    xListView.setPullRefreshEnable(enable);
  }

  @Override
  public void pullLoadMoreEnable(boolean enable) {
    xListView.setPullLoadEnable(enable);
  }

  @Override
  public void onRefreshComplete(boolean success) {
    xListView.stopLoadMore();
    xListView.stopRefresh();
    xListView.setRefreshTime(new SimpleDateFormat("MM-dd HH:mm:ss", Locale.CHINA)
        .format(new Date()));
  }

  @Override
  public void setOnRefreshListener(final OnRefreshListener onRefreshListener) {
    xListView.setXListViewListener(new IXListViewListener() {

      @Override
      public void onRefresh() {
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
    bundle.putParcelable("listview", xListView.onSaveInstanceState());
    bundle.putBundle("pullInfoBundle", xListView.onSavePullInfoState());
    return bundle;
  }

  @Override
  public void onRestoreInstanceState(Bundle state) {
    xListView.onRestoreInstanceState(state.getParcelable("listview"));
    xListView.onRestorePullInfoState(state.getBundle("pullInfoBundle"));
  }

  @Override
  public boolean pullRefreshEnable() {
    return xListView.isEnablePullRefresh();
  }

  @Override
  public boolean pullLoadMoreEnable() {
    return xListView.isEnablePullLoad();
  }
}
