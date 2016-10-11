package cn.yhq.page.ui;

import android.os.Bundle;

import java.util.List;

import cn.yhq.dialog.BaseActivity;
import cn.yhq.page.core.IPageDataIntercept;
import cn.yhq.page.core.OnPageListener;
import cn.yhq.page.core.OnPullToRefreshProvider;
import cn.yhq.page.core.PageAction;


public abstract class PageDataActivity<T, I> extends BaseActivity
        implements
        OnPageListener,
        IPageContextProvider<T, I> {
    private PageContext<T, I> mPageContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onViewCreated(savedInstanceState);
        mPageContext = new PageContext<>(this, this);
        mPageContext.addOnPageListener(this);
        mPageContext.onCreated(savedInstanceState);
    }

    public final void initPageData() {
        mPageContext.initPageData();
    }

    public final void refreshPageData() {
        mPageContext.refreshPageData();
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        mPageContext.onSavePageDataState(bundle);
    }

    /**
     * 分页配置
     *
     * @param pageConfig
     */
    @Override
    public void onPageConfig(PageConfig pageConfig) {
    }

    public abstract void onViewCreated(Bundle savedInstanceState);

    @Override
    public void onDestroy() {
        mPageContext.onDestroy();
        super.onDestroy();
    }

    public final PageContext<T, I> getPageContext() {
        return mPageContext;
    }

    public final PageConfig getPageConfig() {
        return mPageContext.getPageConfig();
    }

    @Override
    public void addPageDataIntercepts(List<IPageDataIntercept<I>> pageDataIntercepts) {

    }

    @Override
    public OnPageListener getOnPageListener() {
        return mPageContext.getDefaultOnPageListener();
    }

    @Override
    public OnPullToRefreshProvider getOnPullToRefreshProvider() {
        return mPageContext.getDefaultOnPullToRefreshProvider();
    }

    @Override
    public void onPageCancelRequests() {

    }

    @Override
    public void onPageRequestStart(PageAction pageAction) {

    }

    @Override
    public void onPageLoadComplete(PageAction pageAction, boolean isFromCache, boolean isSuccess) {

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

