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
 * Created by Yanghuiqiang on 2016/10/11.
 */

public final class PageContext<T, I> {
    private PageEngine<T, I> mPageEngine;
    private PageConfig mPageConfig = new PageConfig();
    private List<IPageDataIntercept<I>> mPageDataIntercepts = new ArrayList<>();
    private List<OnPageListener> mOnPageListeners = new ArrayList<>();
    private View mPageView;

    public PageContext(Context context, IPageContextProvider<T, I> pageContextProvider) {
        mPageView = pageContextProvider.getPageView();
        pageContextProvider.onPageConfig(mPageConfig);
        pageContextProvider.addPageDataIntercepts(mPageDataIntercepts);
        OnPullToRefreshProvider onPullToRefreshProvider = pageContextProvider.getOnPullToRefreshProvider();
        if (onPullToRefreshProvider == null) {
            if (mPageView instanceof XListView) {
                onPullToRefreshProvider = new PullToRefreshListViewContext((XListView) mPageView);
            } else if (mPageView instanceof XExpandableListView) {
                onPullToRefreshProvider = new PullToRefreshExpandableListViewContext((XExpandableListView) mPageView);
            }
        }
        mPageEngine = new PageEngine.Builder<T, I>(context)
                .setPageSize(mPageConfig.pageSize)
                .setPageAdapter(pageContextProvider.getPageAdapter())
                .setPageParser(pageContextProvider.getPageDataParser())
                .setPageRequester(pageContextProvider.getPageRequester())
                .setOnPullToRefreshProvider(onPullToRefreshProvider)
                .setPageDataIntercept(mPageDataIntercepts)
                .setOnPageListeners(mOnPageListeners)
                .build();
    }

    public void onCreated(Bundle savedInstanceState) {
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

    public void onSavePageDataState(Bundle savedInstanceState) {
        try {
            // 保存配置信息，配置信息是必须保存的
            mPageConfig.onSaveInstanceState(savedInstanceState);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public PageManager<T, I> getPageManager() {
        return mPageEngine.getPageManager();
    }

    public void clearPageData() {
        mPageEngine.clearPageData();
    }

    public void initPageData() {
        if (mPageConfig.clearPageDataBeforeRequest) {
            clearPageData();
        }

        mPageEngine.initPageData();
    }

    public void refreshPageData() {
        mPageEngine.refreshPageData();
    }

    public void onDestroy() {
        mPageEngine.cancel();
    }

    public PageConfig getPageConfig() {
        return this.mPageConfig;
    }

    public void addOnPageListener(OnPageListener onPageListener) {
        this.mOnPageListeners.add(onPageListener);
    }
}
