package cn.yhq.page.ui;

import android.os.Bundle;
import android.view.View;

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
        mPageContext = new PageContext<>(this);
        mPageContext.addOnPageListener(this);
        onViewCreated(savedInstanceState);
        mPageContext.initPageContext(this);
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

    /**
     * 获取pageview，比如listview，gridview，recyclerview等等
     *
     * @return
     */
    public abstract View getPageView();

    @Override
    public IPageViewManager getPageViewManager() {
        return mPageContext.getDefaultPageViewManager(this.getPageViewProvider());
    }

    @Override
    public IPageViewProvider getPageViewProvider() {
        return mPageContext.getDefaultPageViewProvider(this.getPageView());
    }

    @Override
    public OnPullToRefreshProvider getOnPullToRefreshProvider() {
        return mPageContext.getDefaultOnPullToRefreshProvider(this.getPageView());
    }

    @Override
    public void onPageCancelRequests() {

    }

    @Override
    public void onPageRequestStart(PageAction pageAction) {

    }

    @Override
    public void onPageLoadComplete(PageAction pageAction, int count, boolean isFromCache, boolean isSuccess) {

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

