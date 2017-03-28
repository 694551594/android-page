package cn.yhq.page.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;

import cn.yhq.base.BaseFragment;
import cn.yhq.page.core.IFilterName;
import cn.yhq.page.core.IPageChecker;
import cn.yhq.page.core.IPageDataIntercept;
import cn.yhq.page.core.IPageSearcher;
import cn.yhq.page.core.OnPageCheckedChangeListener;
import cn.yhq.page.core.OnPageCheckedEquals;
import cn.yhq.page.core.OnPageCheckedInitListener;
import cn.yhq.page.core.OnPageDataStateSaved;
import cn.yhq.page.core.OnPageListener;
import cn.yhq.page.core.OnPullToRefreshProvider;
import cn.yhq.page.core.PageAction;


/**
 * 分页列表数据显示的BaseFragment
 *
 * @param <T>
 * @param <I>
 * @author Yanghuiqiang 2015-5-25
 */
public abstract class PageFragment<T, I> extends BaseFragment
        implements
        OnPageListener,
        IPageContextProvider<T, I> {
    private PageContext<T, I> mPageContext;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageContext = new PageContext(this.getContext(), this);
        mPageContext.addOnPageListener(this);
    }

    /**
     * 此方法是在创建视图后调用的
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPageContext.start(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        mPageContext.savePageDataState(bundle);
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

    public void setPageViewHandler(IPageViewHandler<? extends View> pageViewHandler) {
        this.mPageContext.setPageViewHandler(pageViewHandler);
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

    public final void setPageChecker(IPageChecker<I> pageChecker) {
        this.mPageContext.setPageChecker(pageChecker);
    }

    public final IPageChecker<I> getPageChecker() {
        return mPageContext.getPageChecker();
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

    public final void setPageChecker(int type, OnPageCheckedChangeListener<I> listener) {
        this.mPageContext.setPageChecker(type, listener);
    }

    public final void setOnPageDataStateSaved(OnPageDataStateSaved<I> onPageDataStateSaved) {
        this.mPageContext.setOnPageDataStateSaved(onPageDataStateSaved);
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
