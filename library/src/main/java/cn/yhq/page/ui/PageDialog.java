package cn.yhq.page.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import java.util.List;

import cn.yhq.dialog.core.DialogBuilder;
import cn.yhq.dialog.core.IDialog;
import cn.yhq.page.core.IPageDataIntercept;
import cn.yhq.page.core.OnPageListener;
import cn.yhq.page.core.OnPullToRefreshProvider;
import cn.yhq.page.core.PageAction;

/**
 * Created by Yanghuiqiang on 2016/10/20.
 */

public abstract class PageDialog<T, I> implements OnPageListener, IPageContextProvider<T, I> {
    private PageContext<T, I> mPageContext;
    private Context mContext;

    public PageDialog(Context context) {
        this.mContext = context;
        mPageContext = new PageContext<>(mContext);
        mPageContext.addOnPageListener(this);
        onViewCreated();
        mPageContext.initPageContext(this);
    }

    public final IDialog create() {
        return DialogBuilder.otherDialog(mContext)
                .setContentView(this.getPageView())
                .create();
    }

    public final IDialog show() {
        IDialog dialog = create().show();
        mPageContext.start(null);
        return dialog;
    }

    public final void initPageData() {
        mPageContext.initPageData();
    }

    public final void refreshPageData() {
        mPageContext.refreshPageData();
    }

    public void onSaveInstanceState(Bundle bundle) {
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

    public abstract void onViewCreated();

    public void onDestroy() {
        mPageContext.onDestroy();
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
        return PageContext.getDefaultPullToRefreshProvider(this.getPageView());
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
