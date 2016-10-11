package cn.yhq.page.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.LinearLayout;

import cn.yhq.page.R;
import cn.yhq.page.core.OnPageListener;
import cn.yhq.page.core.PageAction;


/**
 * 默认的分页加载监听，实现加载显示正在加载，没有数据的时候显示空视图，点击空视图重新请求数据的功能
 *
 * @author Yanghuiqiang 2015-5-22
 */
public class DefaultPageListener implements OnPageListener, OnClickListener {
    private PageViewProvider mPageViewProvider;
    private View mPageView;
    private Context mContext;
    private View mLoadingView;
    private View mEmptyView;
    private LayoutInflater inflater;
    private OnReRequestListener mOnReRequestListener;

    public DefaultPageListener(View pageView) {
        this.mPageView = pageView;
        this.mContext = pageView.getContext();
        if (pageView instanceof AbsListView) {
            this.mPageViewProvider = new AbsListViewProvider();
        }
        this.inflater = LayoutInflater.from(mContext);
        initView();
    }

    private void initView() {
        mLoadingView = inflater.inflate(R.layout.loadingview, null);
        mEmptyView = inflater.inflate(R.layout.emptyview, null);
        mEmptyView.setVisibility(View.GONE);
        mLoadingView.setVisibility(View.GONE);
        LinearLayout ll = (LinearLayout) mEmptyView.findViewById(R.id.listview_emptyview);
        ll.setOnClickListener(this);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        ((ViewGroup) this.mPageView.getParent()).addView(mLoadingView, params);
        ((ViewGroup) this.mPageView.getParent()).addView(mEmptyView, params);
    }

    @Override
    public void onClick(View v) {
        if (mOnReRequestListener != null) {
            mOnReRequestListener.onReRequest();
        }
    }

    public void setOnReRequestListener(OnReRequestListener onReRequestListener) {
        this.mOnReRequestListener = onReRequestListener;
    }

    public void onStart() {
        // 请求开始的时候必须要把listview显示出来
        this.mPageView.setVisibility(View.VISIBLE);
        mEmptyView.setVisibility(View.GONE);
        mLoadingView.setVisibility(View.VISIBLE);
        mPageViewProvider.setEmptyView(mPageView, mLoadingView);
    }

    public void onComplete() {
        mEmptyView.setVisibility(View.VISIBLE);
        mLoadingView.setVisibility(View.GONE);
        this.mPageView.setVisibility(View.VISIBLE);
        mPageViewProvider.setEmptyView(mPageView, mEmptyView);
    }

    @Override
    public void onPageCancelRequests() {
        onComplete();
    }

    @Override
    public void onPageRequestStart(PageAction pageAction) {
        onStart();
    }

    @Override
    public void onPageLoadComplete(PageAction pageAction, boolean isFromCache, boolean isSuccess) {
        if (pageAction == PageAction.INIT || pageAction == PageAction.REFRESH) {
            onComplete();
        }
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
