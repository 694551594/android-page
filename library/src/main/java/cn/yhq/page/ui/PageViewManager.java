package cn.yhq.page.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import cn.yhq.page.core.PageAction;

/**
 * Created by Yanghuiqiang on 2016/10/12.
 */

class PageViewManager implements IPageViewManager {
    private Context mContext;
    private View mPageView;
    private View mLoadingView;
    private View mEmptyView;
    private OnReRequestListener mOnReRequestListener;

    private IPageViewHandler mPageViewHandler;

    public PageViewManager() {

    }

    public void inflate(IPageViewProvider pageViewProvider, IPageViewHandler pageViewHandler) {
        this.mPageView = pageViewProvider.getPageView();
        this.mPageViewHandler = pageViewHandler;
        this.mContext = this.mPageView.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        if (pageViewProvider.getPageLoadingView() != 0) {
            this.mLoadingView = inflater.inflate(pageViewProvider.getPageLoadingView(), null);
        }
        if (pageViewProvider.getPageEmptyView() != 0) {
            this.mEmptyView = inflater.inflate(pageViewProvider.getPageEmptyView(), null);
            this.mEmptyView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnReRequestListener != null) {
                        mOnReRequestListener.onReRequest();
                    }
                }
            });
        }

        this.mPageViewHandler.setup(mPageView, mLoadingView, mEmptyView);
    }

    @Override
    public void startPageRequest(PageAction pageAction) {
        if (pageAction == PageAction.INIT || pageAction == PageAction.SEARCH) {
            if (this.mLoadingView != null) {
                this.mPageViewHandler.showPageLoadingView();
            }
        }
    }

    private boolean isFromCache;
    private boolean isFromNetwork;

    @Override
    public void completePageRequest(PageAction pageAction, boolean isFromCache, int count) {
        if (!this.isFromCache && isFromCache) {
            this.isFromCache = isFromCache;
        }
        if (!this.isFromNetwork && !isFromCache) {
            this.isFromNetwork = !isFromCache;
        }
        if (this.isFromCache) {
            if (this.isFromNetwork) {
                completePageRequest(count);
            } else {
                if (count != 0) {
                    completePageRequest(count);
                }
            }
        } else {
            if (this.isFromNetwork) {
                if (count != 0) {
                    completePageRequest(count);
                }
            }
        }
    }

    private void completePageRequest(int count) {
        if (count == 0) {
            if (this.mEmptyView != null) {
                this.mPageViewHandler.showPageEmptyView();
            }
        } else {
            this.mPageViewHandler.showPageView();
        }
    }

    @Override
    public void cancelPageRequest(int count) {
        completePageRequest(count);
    }

    @Override
    public void setOnReRequestListener(OnReRequestListener onReRequestListener) {
        this.mOnReRequestListener = onReRequestListener;
    }

}
