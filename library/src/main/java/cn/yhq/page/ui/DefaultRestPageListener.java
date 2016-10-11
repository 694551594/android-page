package cn.yhq.page.ui;

import android.widget.AbsListView;
import cn.developer.sdk.page2.core.PageException;
import cn.developer.sdk.page2.core.PageManager.PageRequestType;

/**
 * 默认的分页加载监听，实现加载显示正在加载，没有数据的时候显示空视图，点击空视图重新请求数据的功能
 * 
 * @author Yanghuiqiang 2015-5-22
 * 
 */
@Deprecated
public class DefaultRestPageListener extends DefaultPageListener {

  public DefaultRestPageListener(AbsListView absListView) {
    super(absListView);
  }

  @Override
  public void onPageLoadComplete(PageRequestType pageRequestType, boolean isFromCache,
      boolean success) {
    if (pageRequestType == PageRequestType.INIT || pageRequestType == PageRequestType.REFRESH) {
      onComplete();
    }
    super.onPageLoadComplete(pageRequestType, isFromCache, success);
  }

  @Override
  public void onPageException(PageRequestType pageRequestType, PageException e) {}

  @Override
  public void onPageLoadCache(PageRequestType pageRequestType, boolean isHaveCache) {}

}
