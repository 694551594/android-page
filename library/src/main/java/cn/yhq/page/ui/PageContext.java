package cn.yhq.page.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.markmao.pulltorefresh.widget.XExpandableListView;
import com.markmao.pulltorefresh.widget.XListView;

import java.util.ArrayList;
import java.util.List;

import cn.yhq.page.core.IPageDataIntercept;
import cn.yhq.page.core.OnPageListener;
import cn.yhq.page.core.OnPullToRefreshProvider;
import cn.yhq.page.core.PageEngine;
import cn.yhq.page.core.PageManager;

/**
 * 是对PageEngine的UI层级的封装了，主要封装一些接口给Activity以及Fragment提供了，此外，PageContext里面提供了一个分页的配置类PageConfig，用于一些分页的基本配置，比如分页大小、是否在初始的时候自动加载数据等等。
 *
 * Created by Yanghuiqiang on 2016/10/11.
 */

public final class PageContext<T, I> {
    private Context mContext;
    private PageEngine<T, I> mPageEngine;
    private PageConfig mPageConfig = new PageConfig();
    private List<IPageDataIntercept<I>> mPageDataIntercepts = new ArrayList<>();
    private List<OnPageListener> mOnPageListeners = new ArrayList<>();

    public PageContext(Context context) {
        this.mContext = context;
    }

    public void initPageContext(IPageContextProvider<T, I> pageContextProvider) {
        pageContextProvider.onPageConfig(mPageConfig);
        pageContextProvider.addPageDataIntercepts(mPageDataIntercepts);
        mOnPageListeners.add(new DefaultPageListener(pageContextProvider));
        OnPullToRefreshProvider onPullToRefreshProvider = pageContextProvider.getOnPullToRefreshProvider();
        if (onPullToRefreshProvider != null) {
            onPullToRefreshProvider.setPullLoadMoreEnable(mPageConfig.pullLoadMoreEnable);
            onPullToRefreshProvider.setPullRefreshEnable(mPageConfig.pullRefreshEnable);
        }
        mPageEngine = new PageEngine.Builder<T, I>(mContext)
                .setPageSize(mPageConfig.pageSize)
                .setPageAdapter(pageContextProvider.getPageAdapter())
                .setPageParser(pageContextProvider.getPageDataParser())
                .setPageRequester(pageContextProvider.getPageRequester())
                .setOnPullToRefreshProvider(pageContextProvider.getOnPullToRefreshProvider())
                .setPageDataIntercept(mPageDataIntercepts)
                .setOnPageListeners(mOnPageListeners)
                .build();
    }

    public final void onCreated(Bundle savedInstanceState) {
        try {
            if (savedInstanceState != null) {
                // 恢复配置信息
                mPageConfig.onRestoreInstanceState(savedInstanceState);
                this.initPageData();
            } else {
                if (mPageConfig.autoInitPageData) {
                    this.initPageData();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public final OnPullToRefreshProvider getDefaultOnPullToRefreshProvider(View pageView) {
        OnPullToRefreshProvider onPullToRefreshProvider = null;
        if (pageView instanceof XListView) {
            onPullToRefreshProvider = new PullToRefreshListViewContext((XListView) pageView);
        } else if (pageView instanceof XExpandableListView) {
            onPullToRefreshProvider = new PullToRefreshExpandableListViewContext((XExpandableListView) pageView);
        }
        return onPullToRefreshProvider;
    }

    public final IPageViewProvider getDefaultPageViewProvider(View pageView) {
        return new PageViewProvider(pageView);
    }

    public final IPageViewManager getDefaultPageViewManager(IPageViewProvider pageViewProvider) {
        IPageViewManager pageViewManager = new PageViewManager(pageViewProvider);
        pageViewManager.setOnReRequestListener(new OnReRequestListener() {
            @Override
            public void onReRequest() {
                initPageData();
            }
        });
        return pageViewManager;
    }

    public final void onSavePageDataState(Bundle savedInstanceState) {
        try {
            // 保存配置信息，配置信息是必须保存的
            mPageConfig.onSaveInstanceState(savedInstanceState);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public final PageManager<T, I> getPageManager() {
        return mPageEngine.getPageManager();
    }

    public final void clearPageData() {
        mPageEngine.clearPageData();
    }

    public final void initPageData() {
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

    public final void addOnPageListener(OnPageListener onPageListener) {
        this.mOnPageListeners.add(onPageListener);
    }

}
