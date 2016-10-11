package cn.yhq.page.ui;

import com.markmao.pulltorefresh.widget.XListView;
import com.markmao.pulltorefresh.widget.XListView.IXListViewListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cn.yhq.page.core.OnPullToRefreshProvider;

public class PullToRefreshListViewContext implements OnPullToRefreshProvider {
  private final static SimpleDateFormat mSimpleDateFormat =
      new SimpleDateFormat("MM-dd HH:mm:ss", Locale.CHINA);
  private XListView xListView;

  public PullToRefreshListViewContext(XListView xListView) {
    this.xListView = xListView;
  }

  @Override
  public void setHaveMoreData(boolean isHaveMoreData) {
    xListView.setHasMoreData(isHaveMoreData);
  }

  @Override
  public void setPullRefreshEnable(boolean enable) {
    xListView.setPullRefreshEnable(enable);
  }

  @Override
  public void setPullLoadMoreEnable(boolean enable) {
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
        onRefreshListener.onPullToRefresh();
      }

      @Override
      public void onLoadMore() {
        onRefreshListener.onPullToLoadMore();
      }

    });
  }

  @Override
  public boolean isPullRefreshEnable() {
    return xListView.isEnablePullRefresh();
  }

  @Override
  public boolean isPullLoadMoreEnable() {
    return xListView.isEnablePullLoad();
  }
}
