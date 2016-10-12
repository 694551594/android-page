package cn.yhq.page.core;

/**
 * Created by 杨慧强 on 2016/2/17.
 */
public interface OnPageListener {

    // 取消请求
    void onPageCancelRequests(int count);

    // 开始请求
    void onPageRequestStart(PageAction pageAction);

    // 请求载入完成，如果没有缓存，则不会回调此方法
    void onPageLoadComplete(PageAction pageAction, int count, boolean isFromCache, boolean isSuccess);

    // 载入缓存，如果有缓存，会回调onPageLoadComplete
    void onPageLoadCache(PageAction pageAction, boolean isHaveCache);

    // 下拉刷新
    void onPageRefresh();

    // 上拉加载
    void onPageLoadMore();

    // 初始化
    void onPageInit();

    // exception，会回调onPageLoadComplete方法
    void onPageException(Throwable e);
}
