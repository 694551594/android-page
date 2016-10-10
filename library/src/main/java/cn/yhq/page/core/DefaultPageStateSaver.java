package cn.yhq.page.core;

import android.os.Bundle;

/**
 * 默认的分页列表数据状态保存器
 * 
 * @author Yanghuiqiang 2015-5-24
 *
 */
class DefaultPageStateSaver implements IPageStateSaver {
  // 保存列表数据
  private final static String LISTVIEW_ADAPTER = "listview_adapter";
  // 保存分页信息
  private final static String LISTVIEW_PAGE_INFO = "listview_page_info";
  // 保存listview状态
  private final static String LISTVIEW_INFO = "listview_info";
  // 保存分页请求状态，如加载中或者加载完
  private final static String PAGE_REQUEST_INFO = "page_request_info";

  @Override
  public Bundle onSave(PageState pageState) {
    Bundle bundle = new Bundle();
    bundle.putParcelable(LISTVIEW_ADAPTER, pageState.adapterSavedState);
    bundle.putParcelable(LISTVIEW_INFO, pageState.absListViewSavedState);
    bundle.putParcelable(LISTVIEW_PAGE_INFO, pageState.pageInfoSavedState);
    bundle.putParcelable(PAGE_REQUEST_INFO, pageState.pageRequestSavedState);
    return bundle;
  }

  @Override
  public PageState onRestore(Bundle savedInstanceState) {
    PageState pageState = new PageState();
    pageState.adapterSavedState = savedInstanceState.getParcelable(LISTVIEW_ADAPTER);
    pageState.absListViewSavedState = savedInstanceState.getParcelable(LISTVIEW_INFO);
    pageState.pageInfoSavedState = savedInstanceState.getParcelable(LISTVIEW_PAGE_INFO);
    pageState.pageRequestSavedState = savedInstanceState.getParcelable(PAGE_REQUEST_INFO);
    return pageState;
  }
}
