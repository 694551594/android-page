package cn.yhq.page.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import cn.yhq.page.core.PageAction;

/**
 * Created by Yanghuiqiang on 2016/10/12.
 */

public class PageViewManager implements IPageViewManager {
    private Context mContext;
    private View mPageView;
    private View mLoadingView;
    private View mEmptyView;
    private ViewGroup mParentView;
    private LayoutInflater mInflater;
    private OnReRequestListener mOnReRequestListener;

    PageViewManager(IPageViewProvider pageViewProvider) {
        this.mPageView = pageViewProvider.getPageView();
        this.mContext = this.mPageView.getContext();
        this.mInflater = LayoutInflater.from(mContext);
        this.mLoadingView = mInflater.inflate(pageViewProvider.getPageLoadingView(), null);
        this.mEmptyView = mInflater.inflate(pageViewProvider.getPageEmptyView(), null);
        this.mEmptyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnReRequestListener != null) {
                    mOnReRequestListener.onReRequest();
                }
            }
        });
        this.mParentView = (ViewGroup) this.mPageView.getParent();
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        this.mParentView.addView(mLoadingView, params);
        this.mParentView.addView(mEmptyView, params);
        this.mPageView.setVisibility(View.VISIBLE);
        this.mEmptyView.setVisibility(View.GONE);
        this.mLoadingView.setVisibility(View.GONE);
    }

    @Override
    public void startPageRequest(PageAction pageAction) {
        if (pageAction == PageAction.INIT) {
            // 请求开始的时候必须要把listview显示出来
            mPageView.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.GONE);
            mLoadingView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void completePageRequest(PageAction pageAction, int count) {
        if (pageAction == PageAction.INIT || pageAction == PageAction.REFRESH) {
            if (count == 0) {
                mEmptyView.setVisibility(View.VISIBLE);
            } else {
                mEmptyView.setVisibility(View.GONE);
            }
            mLoadingView.setVisibility(View.GONE);
            mPageView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void cancelPageRequest(int count) {
        if (count == 0) {
            mEmptyView.setVisibility(View.VISIBLE);
        } else {
            mEmptyView.setVisibility(View.GONE);
        }
        mLoadingView.setVisibility(View.GONE);
        mPageView.setVisibility(View.VISIBLE);
    }

    @Override
    public void setOnReRequestListener(OnReRequestListener onReRequestListener) {
        this.mOnReRequestListener = onReRequestListener;
    }

}
