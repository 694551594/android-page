package cn.yhq.page.ui;

import android.os.Bundle;
import android.widget.EditText;

import cn.yhq.base.BaseActivity;
import cn.yhq.page.core.OnPageCheckedEquals;
import cn.yhq.page.core.IFilterName;
import cn.yhq.page.core.IPageChecker;
import cn.yhq.page.core.IPageDataIntercept;
import cn.yhq.page.core.IPageSearcher;
import cn.yhq.page.core.OnPageCheckedChangeListener;
import cn.yhq.page.core.OnPageCheckedInitListener;
import cn.yhq.page.core.OnPageListener;
import cn.yhq.page.core.OnPullToRefreshProvider;
import cn.yhq.page.core.PageAction;


public abstract class PageActivity<T, I> extends BaseActivity
        implements
        OnPageListener,
        IPageContextProvider<T, I> {
    private PageContext<T, I> mPageContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mPageContext = new PageContext(this, this);
        mPageContext.addOnPageListener(this);
        super.onCreate(savedInstanceState);
        mPageContext.start(savedInstanceState);
    }

    public final void initPageData() {
        mPageContext.initPageData();
    }

    public final void refreshPageData() {
        mPageContext.refreshPageData();
    }

    public final void searchPageData(String keyword) {
        mPageContext.searchPageData(keyword);
    }

    public final void attachSearchEditText(EditText searchEditText, IFilterName<I> filterName) {
        mPageContext.attachSearchEditText(searchEditText, filterName);
    }

    public final void attachSearchEditText(EditText searchEditText, IPageSearcher<I> pageSearcher) {
        mPageContext.attachSearchEditText(searchEditText, pageSearcher);
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        mPageContext.savePageDataState(bundle);
    }

    /**
     * 分页配置
     *
     * @param pageConfig
     */
    @Override
    public void onPageConfig(PageConfig pageConfig) {
    }

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

    public final void addPageDataIntercept(IPageDataIntercept<I> intercept) {
        this.mPageContext.addPageDataIntercept(intercept);
    }

    public final void setOnPullToRefreshProvider(OnPullToRefreshProvider onPullToRefreshProvider) {
        this.mPageContext.setOnPullToRefreshProvider(onPullToRefreshProvider);
    }

    public final void setPageSearcher(IPageSearcher<I> pageSearcher) {
        this.mPageContext.setPageSearcher(pageSearcher);
    }

    public final void setPageViewProvider(IPageViewProvider pageViewProvider) {
        this.mPageContext.setPageViewProvider(pageViewProvider);
    }

    public final IPageChecker<I> getPageChecker() {
        return mPageContext.getPageChecker();
    }

    public final void setPageChecker(int type, OnPageCheckedChangeListener<I> listener) {
        this.mPageContext.setPageChecker(type, listener);
    }

    public final void setPageChecker(int type, OnPageCheckedEquals<I> equals, OnPageCheckedChangeListener<I> listener) {
        this.mPageContext.setPageChecker(type, equals, listener);
    }

    public final void setPageChecker(int type, OnPageCheckedChangeListener<I> listener1, OnPageCheckedInitListener listener2) {
        this.mPageContext.setPageChecker(type, listener1, listener2);
    }

    public final void setPageChecker(int type, OnPageCheckedEquals<I> equals, OnPageCheckedChangeListener<I> listener1, OnPageCheckedInitListener listener2) {
        this.mPageContext.setPageChecker(type, equals, listener1, listener2);
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

    @Override
    public void onPageSearch(String keyword) {

    }
}

