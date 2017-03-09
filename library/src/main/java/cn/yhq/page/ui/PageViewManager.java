package cn.yhq.page.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.yhq.page.core.PageAction;

/**
 * Created by Yanghuiqiang on 2016/10/12.
 */

class PageViewManager implements IPageViewManager {
    private Context mContext;
    private View mPageView;
    private View mLoadingView;
    private View mEmptyView;
    private ViewGroup mParentView;
    private ViewGroup mPageLayout;
    private OnReRequestListener mOnReRequestListener;
    private ViewGroup.LayoutParams mParams;

    public PageViewManager() {

    }

    public void inflate(IPageViewProvider pageViewProvider) {
        this.mPageView = pageViewProvider.getPageView();
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

        this.mPageLayout = (ViewGroup) this.mPageView.getParent();
        this.mParentView = (ViewGroup) mPageLayout.getParent();

        mParams = this.mPageLayout.getLayoutParams();
    }

    private void reset() {
        this.mPageLayout.setVisibility(View.GONE);
        if (this.mEmptyView != null) {
            this.mParentView.removeView(this.mEmptyView);
        }
        if (this.mLoadingView != null) {
            this.mParentView.removeView(this.mLoadingView);
        }
    }

    @Override
    public void startPageRequest(PageAction pageAction) {
        if (pageAction == PageAction.INIT || pageAction == PageAction.SEARCH) {
            reset();
            if (this.mLoadingView != null) {
                this.mParentView.addView(this.mLoadingView, mParams);
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
        reset();
        if (count == 0) {
            if (this.mEmptyView != null) {
                this.mParentView.addView(this.mEmptyView, mParams);
            }
        } else {
            this.mPageLayout.setVisibility(View.VISIBLE);
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
