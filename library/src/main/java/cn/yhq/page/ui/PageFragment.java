package cn.yhq.page.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import cn.yhq.base.BaseFragment;
import cn.yhq.page.core.IPageDataIntercept;
import cn.yhq.page.core.OnPageListener;
import cn.yhq.page.core.OnPullToRefreshProvider;
import cn.yhq.page.core.PageAction;
import cn.yhq.page.core.PageSearcher;


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

    /**
     * 此方法是在创建视图后调用的
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPageContext = new PageContext(this.getContext(), this);
        mPageContext.addOnPageListener(this);
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

    public final void attachSearchEditText(final EditText searchEditText) {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                searchPageData(searchEditText.getText().toString());
            }
        });
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

    @Override
    public IPageViewProvider getPageViewProvider() {
        return null;
    }

    @Override
    public OnPullToRefreshProvider getOnPullToRefreshProvider() {
        return null;
    }

    public final void addPageDataIntercept(IPageDataIntercept<I> intercept) {
        this.mPageContext.addPageDataIntercept(intercept);
    }

    @Override
    public PageSearcher<T, I> getPageSearcher() {
        return null;
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
