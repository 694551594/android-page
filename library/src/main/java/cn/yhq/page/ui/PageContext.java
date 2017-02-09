package cn.yhq.page.ui;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import cn.yhq.page.core.DefaultOnPageListener;
import cn.yhq.page.core.IPageAdapter;
import cn.yhq.page.core.IPageDataIntercept;
import cn.yhq.page.core.IPageDataParser;
import cn.yhq.page.core.IPageRequester;
import cn.yhq.page.core.IPageSearcher;
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

    public PageContext(Builder<T, I> builder) {
        this.mContext = builder.mContext;
        this.mPageConfig = builder.mPageConfig;
        final IPageViewManager pageViewManager = builder.mPageViewManager;
        final IPageAdapter<I> pageAdapter = builder.mPageAdapter;
        pageViewManager.setOnReRequestListener(new OnReRequestListener() {
            @Override
            public void onReRequest() {
                initPageData();
            }
        });
        mPageEngine = new PageEngine(this.mContext,
                mPageConfig.pageSize,
                builder.mPageRequest,
                builder.mPageDataParser,
                builder.mOnPullToRefreshProvider,
                builder.mPageAdapter,
                builder.mPageSearcher
        );
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
    }

    public static class Builder<T, I> {
        private Context mContext;
        private PageConfig mPageConfig = new PageConfig();
        private IPageAdapter<I> mPageAdapter;
        private IPageRequester<T, I> mPageRequest;
        private IPageDataParser<T, I> mPageDataParser;
        private OnPullToRefreshProvider mOnPullToRefreshProvider;
        private IPageViewManager mPageViewManager;
        private IPageViewProvider mPageViewProvider;
        private View mPageView;
        private IPageSearcher<T, I> mPageSearcher;

        public Builder(Context context) {
            this.mContext = context;
        }

        public static <T, I> Builder<T, I> createBuilder(Context context) {
            return new Builder(context);
        }

        public static <T, I> Builder<T, I> createBuilder(Context context, IPageContextProvider<T, I> pageContextProvider) {
            Builder<T, I> builder = new Builder<>(context);
            pageContextProvider.onPageConfig(builder.mPageConfig);
            return builder
                    .setOnPullToRefreshProvider(pageContextProvider.getOnPullToRefreshProvider())
                    .setPageAdapter(pageContextProvider.getPageAdapter())
                    .setPageDataParser(pageContextProvider.getPageDataParser())
                    .setPageRequest(pageContextProvider.getPageRequester())
                    .setPageView(pageContextProvider.getPageView())
                    .setPageViewProvider(pageContextProvider.getPageViewProvider())
                    .setPageViewManager(pageContextProvider.getPageViewManager())
                    .setPageSearcher(pageContextProvider.getPageSearcher());
        }

        public PageContext<T, I> build() {
            if (this.mPageViewProvider == null) {
                this.mPageViewProvider = new PageViewProvider(mPageView);
            }
            if (this.mPageViewManager == null) {
                this.mPageViewManager = new PageViewManager(this.mPageViewProvider);
            }
            if (this.mOnPullToRefreshProvider == null) {
                this.mOnPullToRefreshProvider = PullToRefreshContextFactory.getPullToRefreshProvider(this.mPageView);
            }
            if (this.mPageSearcher == null) {
                this.mPageSearcher = new DefaultPageSearcher<>(mContext);
            }
            return new PageContext<>(this);
        }

        public Builder<T, I> setPageConfig(PageConfig pageConfig) {
            this.mPageConfig = pageConfig;
            return this;
        }

        public Builder<T, I> setPageAdapter(IPageAdapter<I> pageAdapter) {
            this.mPageAdapter = pageAdapter;
            return this;
        }

        public Builder<T, I> setPageRequest(IPageRequester<T, I> pageRequest) {
            this.mPageRequest = pageRequest;
            return this;
        }

        public Builder<T, I> setPageDataParser(IPageDataParser<T, I> pageDataParser) {
            this.mPageDataParser = pageDataParser;
            return this;
        }

        public Builder<T, I> setOnPullToRefreshProvider(OnPullToRefreshProvider onPullToRefreshProvider) {
            this.mOnPullToRefreshProvider = onPullToRefreshProvider;
            return this;
        }

        public Builder<T, I> setPageViewManager(IPageViewManager pageViewManager) {
            this.mPageViewManager = pageViewManager;
            return this;
        }

        public Builder<T, I> setPageViewProvider(IPageViewProvider pageViewProvider) {
            this.mPageViewProvider = pageViewProvider;
            return this;
        }

        public Builder<T, I> setPageView(View pageView) {
            this.mPageView = pageView;
            return this;
        }

        public Builder<T, I> setPageSearcher(IPageSearcher<T, I> mPageSearcher) {
            this.mPageSearcher = mPageSearcher;
            return this;
        }
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
