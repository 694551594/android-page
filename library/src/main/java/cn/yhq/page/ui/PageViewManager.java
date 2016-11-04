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

    public PageViewManager(IPageViewProvider pageViewProvider) {
        this.mPageView = pageViewProvider.getPageView();
        this.mContext = this.mPageView.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        this.mLoadingView = inflater.inflate(pageViewProvider.getPageLoadingView(), null);
        this.mEmptyView = inflater.inflate(pageViewProvider.getPageEmptyView(), null);
        this.mEmptyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnReRequestListener != null) {
                    mOnReRequestListener.onReRequest();
                }
            }
        });

        this.mPageLayout = (ViewGroup) this.mPageView.getParent();
        this.mParentView = (ViewGroup) mPageLayout.getParent();

        mParams = this.mPageLayout.getLayoutParams();
    }

    private void reset() {
        this.mParentView.removeView(this.mPageLayout);
        this.mParentView.removeView(this.mEmptyView);
        this.mParentView.removeView(this.mLoadingView);
    }

    @Override
    public void startPageRequest(PageAction pageAction) {
        if (pageAction == PageAction.INIT) {
            reset();
            this.mParentView.removeView(this.mPageLayout);
            this.mParentView.removeView(this.mEmptyView);
            this.mParentView.addView(this.mLoadingView, mParams);
        }
    }

    @Override
    public void completePageRequest(PageAction pageAction, int count) {
        if (pageAction == PageAction.INIT || pageAction == PageAction.REFRESH) {
            cancelPageRequest(count);
        }
    }

    @Override
    public void cancelPageRequest(int count) {
        reset();
        if (count == 0) {
            this.mParentView.removeView(this.mPageLayout);
            this.mParentView.addView(this.mEmptyView, mParams);
            this.mParentView.removeView(this.mLoadingView);
        } else {
            this.mParentView.addView(this.mPageLayout, mParams);
            this.mParentView.removeView(this.mEmptyView);
            this.mParentView.removeView(this.mLoadingView);
        }
    }

    @Override
    public void setOnReRequestListener(OnReRequestListener onReRequestListener) {
        this.mOnReRequestListener = onReRequestListener;
    }

}
