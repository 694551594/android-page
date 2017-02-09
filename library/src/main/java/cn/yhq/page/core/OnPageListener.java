package cn.yhq.page.core;

/**
 * 涵盖了整个请求过程的生命周期。最常用的可能就是onPageLoadComplete方法了，这个方法在数据适配到adapter后回调，也就是说，你可以在这个方法里面拿到最终适配并显示出来的数据。
 *
 * Created by 杨慧强 on 2016/2/17.
 */
public interface OnPageListener {

    // 取消请求
    void onPageCancelRequests();

    // 开始请求
    void onPageRequestStart(PageAction pageAction);

    // 请求载入完成，如果没有缓存，则不会回调此方法
    void onPageLoadComplete(PageAction pageAction, boolean isFromCache, boolean isSuccess);

    // 载入缓存，如果有缓存，会回调onPageLoadComplete
    void onPageLoadCache(PageAction pageAction, boolean isHaveCache);

    // 下拉刷新
    void onPageRefresh();

    // 上拉加载
    void onPageLoadMore();

    void onPageSearch(String keyword);

    // 初始化
    void onPageInit();

    // exception，会回调onPageLoadComplete方法
    void onPageException(Throwable e);
}
