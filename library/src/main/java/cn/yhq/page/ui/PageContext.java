package cn.yhq.page.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import cn.yhq.page.core.DefaultOnPageListener;
import cn.yhq.page.core.IPageAdapter;
import cn.yhq.page.core.IPageDataIntercept;
import cn.yhq.page.core.OnPageListener;
import cn.yhq.page.core.OnPullToRefreshProvider;
import cn.yhq.page.core.PageAction;
import cn.yhq.page.core.PageEngine;
import cn.yhq.page.core.PageManager;
import cn.yhq.page.pulltorefresh.PullToRefreshContextFactory;

/**
 * 是对PageEngine的UI层级的封装了，主要封装一些接口给Activity以及Fragment提供了。
 * <p>
 * 此外，PageContext里面提供了一个分页的配置类PageConfig，用于一些分页的基本配置，比如分页大小、是否在初始的时候自动加载数据等等。
 * <p>
 * Created by Yanghuiqiang on 2016/10/11.
 */

public final class PageContext<T, I> {
    private Context mContext;
    private PageEngine<T, I> mPageEngine;
    private PageConfig mPageConfig = new PageConfig();
    private IPageContextProvider<T, I> mPageContextProvider;

    public PageContext(Context context, IPageContextProvider<T, I> provider) {
        this.mContext = context;
        this.mPageContextProvider = provider;
        provider.onPageConfig(mPageConfig);
        mPageEngine = new PageEngine(this.mContext, mPageConfig.pageSize);
    }

    private final void prepare() {
        View pageView = mPageContextProvider.getPageView();
        IPageViewProvider pageViewProvider = mPageContextProvider.getPageViewProvider();
        if (pageViewProvider == null) {
            pageViewProvider = new PageViewProvider(pageView);
        }
        final IPageAdapter<I> pageAdapter = mPageContextProvider.getPageAdapter();
        final PageViewManager pageViewManager = new PageViewManager(pageViewProvider);
        pageViewManager.setOnReRequestListener(new OnReRequestListener() {
            @Override
            public void onReRequest() {
                initPageData();
            }
        });
        mPageEngine.addOnPageListener(new DefaultOnPageListener() {
            @Override
            public void onPageCancelRequests() {
                pageViewManager.cancelPageRequest(pageAdapter.getPageDataCount());
            }

            @Override
            public void onPageRequestStart(PageAction pageAction) {
                pageViewManager.startPageRequest(pageAction);
            }

            @Override
            public void onPageLoadComplete(PageAction pageAction, boolean isFromCache, boolean isSuccess) {
                pageViewManager.completePageRequest(pageAction, pageAdapter.getPageDataCount());
            }
        });
        OnPullToRefreshProvider onPullToRefreshProvider = mPageContextProvider.getOnPullToRefreshProvider();
        if (onPullToRefreshProvider == null) {
            onPullToRefreshProvider = PullToRefreshContextFactory.getPullToRefreshProvider(pageView);
        }
        mPageEngine.setPageDataParser(mPageContextProvider.getPageDataParser());
        mPageEngine.setPageRequester(mPageContextProvider.getPageRequester());
        mPageEngine.setOnPullToRefreshProvider(onPullToRefreshProvider);
        mPageEngine.setPageAdapter(pageAdapter);
        mPageEngine.setPageSearcher(mPageContextProvider.getPageSearcher());
    }

    public final void start(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            boolean success = restorePageDataState(savedInstanceState);
            if (!success) {
                this.initPageData();
            }
        } else {
            if (mPageConfig.autoInitPageData) {
                this.initPageData();
            }
        }
    }

    public final boolean savePageDataState(Bundle savedInstanceState) {
        return mPageEngine.saveState(savedInstanceState);
    }

    public final boolean restorePageDataState(Bundle savedInstanceState) {
        return mPageEngine.restoreState(savedInstanceState);
    }

    public final PageManager<T, I> getPageManager() {
        return mPageEngine.getPageManager();
    }

    public final void clearPageData() {
        mPageEngine.clearPageData();
    }

    public final void initPageData() {
        prepare();

        if (mPageConfig.clearPageDataBeforeRequest) {
            clearPageData();
        }

        mPageEngine.initPageData();
    }

    public final void refreshPageData() {
        mPageEngine.refreshPageData();
    }

    public final void onDestroy() {
        mPageEngine.cancel();
    }

    public final PageConfig getPageConfig() {
        return this.mPageConfig;
    }

    public final void searchPageData(String keyword) {
        mPageEngine.searchPageData(keyword);
    }

    public final void addOnPageListener(OnPageListener listener) {
        this.mPageEngine.addOnPageListener(listener);
    }

    public final void addPageDataIntercept(IPageDataIntercept<I> intercept) {
        this.mPageEngine.addPageDataIntercept(intercept);
    }

}
