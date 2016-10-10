package cn.yhq.page.list;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.os.Bundle;

import com.markmao.pulltorefresh.widget.XListView;
import com.markmao.pulltorefresh.widget.XListView.IXListViewListener;

import cn.developer.sdk.page2.core.IPullToRefreshListener;

public class PullToRefreshListViewContext extends IPullToRefreshListener {
  private final static SimpleDateFormat mSimpleDateFormat =
      new SimpleDateFormat("MM-dd HH:mm:ss", Locale.CHINA);
  private XListView xListView;

  public PullToRefreshListViewContext(XListView xListView) {
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
    xListView.setRefreshTime(mSimpleDateFormat.format(new Date()));
  }

  @Override
  public void setOnRefreshListener(final OnRefreshListener onRefreshListener) {
    xListView.setXListViewListener(new IXListViewListener() {

      @Override
      public void onRefresh() {
        // 刷新的时候设置为还有下一页数据。是否真的有下一页数据获取到数据后再判断
        xListView.setHasMoreData(true);
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
