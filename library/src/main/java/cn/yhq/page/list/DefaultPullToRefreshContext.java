package cn.yhq.page.list;

import android.os.Bundle;

import cn.yhq.page.core.IPullToRefreshListener;


public class DefaultPullToRefreshContext extends IPullToRefreshListener {

  public DefaultPullToRefreshContext() {}

  @Override
  public void setHasMoreData(boolean isHasMoreData) {
    // TODO Auto-generated method stub

  }

  @Override
  public void pullRefreshEnable(boolean enable) {
    // TODO Auto-generated method stub

  }

  @Override
  public void pullLoadMoreEnable(boolean enable) {
    // TODO Auto-generated method stub

  }

  @Override
  public void onRefreshComplete(boolean success) {
    // TODO Auto-generated method stub

  }

  @Override
  public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
    // TODO Auto-generated method stub

  }

  @Override
  public Bundle onSaveInstanceState() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void onRestoreInstanceState(Bundle state) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public boolean pullRefreshEnable() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean pullLoadMoreEnable() {
    // TODO Auto-generated method stub
    return false;
  }


}
