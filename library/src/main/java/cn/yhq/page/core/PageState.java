package cn.yhq.page.core;

import android.os.Bundle;

/**
 * 分页数据状态保存实体
 * 
 * @author Yanghuiqiang 2015-5-22
 * 
 */
public class PageState {
  // adapter状态保存
  public Bundle adapterSavedState;
  // 分页信息
  public Bundle pageInfoSavedState;
  // listview
  public Bundle absListViewSavedState;
  // 分页请求
  public Bundle pageRequestSavedState;
}
