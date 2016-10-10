package cn.yhq.page.core;

import android.os.Bundle;

import cn.developer.sdk.page2.core.PageException;
import cn.developer.sdk.page2.core.PageManager;

/**
 * Created by 杨慧强 on 2016/2/17.
 */
public interface OnPageListener {

    // 状态恢复
    void onPageRestoreInstanceState(Bundle state);

    // 状态保存
    void onPageSaveInstanceState(Bundle state);

    // 取消请求
    void onPageCancelRequests();

    // 开始请求
    void onPageRequestStart(PageManager.PageRequestType pageRequestType);

    // 请求载入完成，如果没有缓存，则不会回调此方法
    void onPageLoadComplete(PageManager.PageRequestType pageRequestType, boolean isFromCache,
                            boolean isSuccess);

    // 载入缓存，如果有缓存，会回调onPageLoadComplete
    void onPageLoadCache(PageManager.PageRequestType pageRequestType, boolean isHaveCache);

    // 下拉刷新
    void onPageRefresh();

    // 上拉加载
    void onPageLoadMore();

    // exception，会回调onPageLoadComplete方法
    void onPageException(PageManager.PageRequestType pageRequestType, PageException e);
}
