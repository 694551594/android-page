package cn.yhq.page.ui;

import cn.yhq.page.core.OnPageListener;
import cn.yhq.page.core.PageAction;


/**
 * 默认的分页加载监听，实现加载显示正在加载，没有数据的时候显示空视图，点击空视图重新请求数据的功能
 *
 * @author Yanghuiqiang 2015-5-22
 */
final class DefaultPageListener implements OnPageListener {
    private IPageViewManager mPageViewManager;

    public DefaultPageListener(IPageViewManager pageViewManager) {
        this.mPageViewManager = pageViewManager;
    }

    @Override
    public void onPageCancelRequests(int count) {
        mPageViewManager.cancelPageRequest(count);
    }

    @Override
    public void onPageRequestStart(PageAction pageAction) {
        mPageViewManager.startPageRequest(pageAction);
    }

    @Override
    public void onPageLoadComplete(PageAction pageAction, int count, boolean isFromCache, boolean isSuccess) {
        mPageViewManager.completePageRequest(pageAction, count);
    }

    @Override
    public void onPageLoadCache(PageAction pageAction, boolean isHaveCache) {
    }

    @Override
    public void onPageRefresh() {
    }

    @Override
    public void onPageLoadMore() {
    }

    @Override
    public void onPageInit() {

    }

    @Override
    public void onPageException(Throwable e) {
    }

}
