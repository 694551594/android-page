package cn.yhq.page.core;

import java.io.Serializable;

/**
 * 分页配置
 * 
 * @author Yanghuiqiang 2015-5-25
 * 
 */
public class PageConfig implements Serializable {
  // 分页大小
  int pageSize;
  // 是否自动载入数据
  boolean autoInitPageData;
  // 是否进行状态保存与恢复
  boolean savedStateEnable;
  // 是否在请求前清空listview的数据
  boolean clearPageDataBeforeRequest;
  // 下拉刷新是否有效
  boolean pullRefreshEnable;
  // 上拉加载是否有效
  boolean pullLoadMoreEnable;
  // pull（下拉或者上拉）是否有效
  boolean pullEnable;
  // listview是否总是显示
  boolean listViewAlwaysVisiable;

  PageConfig() {
    pageSize = 20;
    autoInitPageData = true;
    savedStateEnable = true;
    clearPageDataBeforeRequest = false;
    pullRefreshEnable = true;
    pullLoadMoreEnable = true;
    listViewAlwaysVisiable = false;
    pullEnable = true;
  }

  public PageConfig setPageSize(int pageSize) {
    this.pageSize = pageSize;
    return this;
  }

  public PageConfig setAutoInitPageData(boolean autoInitPageData) {
    this.autoInitPageData = autoInitPageData;
    return this;
  }

  public PageConfig setSavedStateEnable(boolean savedStateEnable) {
    this.savedStateEnable = savedStateEnable;
    return this;
  }

  public PageConfig setClearPageDataBeforeRequest(boolean clearPageDataBeforeRequest) {
    this.clearPageDataBeforeRequest = clearPageDataBeforeRequest;
    return this;
  }

  public PageConfig setPullRefreshEnable(boolean pullRefreshEnable) {
    this.pullRefreshEnable = pullRefreshEnable;
    return this;
  }

  public PageConfig setPullLoadMoreEnable(boolean pullLoadMoreEnable) {
    this.pullLoadMoreEnable = pullLoadMoreEnable;
    return this;
  }

  public PageConfig setListViewAlwaysVisiable(boolean listViewAlwaysVisiable) {
    this.listViewAlwaysVisiable = listViewAlwaysVisiable;
    return this;
  }

  public PageConfig setPullEnable(boolean pullEnable) {
    this.pullEnable = pullEnable;
    return this;
  }

}
