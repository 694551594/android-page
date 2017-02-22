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
        // this.mParentView.removeView(this.mPageLayout);
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

    @Override
    public void completePageRequest(PageAction pageAction, int count) {
        if (pageAction == PageAction.INIT || pageAction == PageAction.REFRESH || pageAction == PageAction.SEARCH) {
            cancelPageRequest(count);
        }
    }

    @Override
    public void cancelPageRequest(int count) {
        reset();
        if (count == 0) {
            if (this.mEmptyView != null) {
                this.mParentView.addView(this.mEmptyView, mParams);
            }
        } else {
            // this.mParentView.addView(this.mPageLayout, mParams);
            this.mPageLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setOnReRequestListener(OnReRequestListener onReRequestListener) {
        this.mOnReRequestListener = onReRequestListener;
    }

}
