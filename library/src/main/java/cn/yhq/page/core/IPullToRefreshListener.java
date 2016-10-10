package cn.yhq.page.core;

import android.os.Bundle;

/**
 * 上拉加载下拉刷新的接�?
 * 
 * @author Yanghuiqiang 2014-9-4
 * 
 */
public abstract class IPullToRefreshListener {

  /**
   * 
   * 刷新接口
   * 
   * @author Yanghuiqiang 2014-10-9
   * 
   */
  public interface OnRefreshListener {
    public void pullToRefresh();

    public void pullToLoadMore();
  }

  /**
   * 设置是否有更多数�?
   * 
   * @param isHasMoreData
   */
  public abstract void setHasMoreData(boolean isHasMoreData);

  /**
   * 下拉刷新是否有效
   * 
   * @param enable
   */
  public abstract void pullRefreshEnable(boolean enable);

  public abstract boolean pullRefreshEnable();

  /**
   * 
   * 上拉加载是否有效
   * 
   * @param enable
   */
  public abstract void pullLoadMoreEnable(boolean enable);

  public abstract boolean pullLoadMoreEnable();

  /**
   * 刷新完成
   * 
   * @param success
   */
  public abstract void onRefreshComplete(boolean success);

  /**
   * 设置刷新监听
   * 
   * @param onRefreshListener
   */
  public abstract void setOnRefreshListener(OnRefreshListener onRefreshListener);

  public abstract Bundle onSaveInstanceState();

  public abstract void onRestoreInstanceState(Bundle state);
}
