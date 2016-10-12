package cn.yhq.page.core;

import java.util.List;

/**
 * Created by 杨慧强 on 2016/2/17.
 */
public class OnPageListenerDispatcher implements OnPageListener {
    private List<OnPageListener> mOnPageListeners;

    public OnPageListenerDispatcher(List<OnPageListener> onPageListeners) {
        this.mOnPageListeners = onPageListeners;
    }

    @Override
    public void onPageCancelRequests(int count) {
        for (OnPageListener l : mOnPageListeners) {
            l.onPageCancelRequests(count);
        }
    }

    @Override
    public void onPageRequestStart(PageAction pageAction) {
        for (OnPageListener l : mOnPageListeners) {
            l.onPageRequestStart(pageAction);
        }
    }

    @Override
    public void onPageLoadComplete(PageAction pageAction, int count, boolean isFromCache, boolean isSuccess) {
        for (OnPageListener l : mOnPageListeners) {
            l.onPageLoadComplete(pageAction, count, isFromCache, isSuccess);
        }
    }

    @Override
    public void onPageLoadCache(PageAction pageAction, boolean isHaveCache) {
        for (OnPageListener l : mOnPageListeners) {
            l.onPageLoadCache(pageAction, isHaveCache);
        }
    }

    @Override
    public void onPageRefresh() {
        for (OnPageListener l : mOnPageListeners) {
            l.onPageRefresh();
        }
    }

    @Override
    public void onPageLoadMore() {
        for (OnPageListener l : mOnPageListeners) {
            l.onPageLoadMore();
        }
    }

    @Override
    public void onPageInit() {
        for (OnPageListener l : mOnPageListeners) {
            l.onPageInit();
        }
    }

    @Override
    public void onPageException(Throwable e) {
        for (OnPageListener l : mOnPageListeners) {
            l.onPageException(e);
        }
    }
}
