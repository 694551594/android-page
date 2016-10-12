package cn.yhq.page.core;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页管理引擎
 * <p>
 * Created by Yanghuiqiang on 2016/10/11.
 */
public final class PageEngine<T, I> {

    public static class Builder<T, I> {
        private Context mContext;
        // 适配器接口
        private IPageAdapter<I> mPageAdapter;
        // 上拉刷新下拉加载的接口
        private OnPullToRefreshProvider mOnPullToRefreshProvider;
        // 分页请求接口
        private IPageRequester<T, I> mPageRequester;
        // 分页数据处理过程监听
        private List<OnPageListener> mOnPageListeners = new ArrayList<>();
        // 分页解析器
        private IPageDataParser mPageParser;
        // 分页大小
        private int mPageSize;
        // 处理器
        private List<IPageDataIntercept<I>> mPageDataIntercepts = new ArrayList<>();

        public Builder(Context context) {
            this.mContext = context;
            this.mPageSize = 20;
        }

        public Builder<T, I> setPageAdapter(IPageAdapter<I> mPageAdapter) {
            this.mPageAdapter = mPageAdapter;
            return this;
        }

        public Builder<T, I> setOnPullToRefreshProvider(
                OnPullToRefreshProvider onPullToRefreshProvider) {
            this.mOnPullToRefreshProvider = onPullToRefreshProvider;
            return this;
        }

        public Builder<T, I> setPageRequester(IPageRequester<T, I> pageRequester) {
            this.mPageRequester = pageRequester;
            return this;
        }

        public Builder<T, I> addOnPageListeners(OnPageListener onPageListener) {
            this.mOnPageListeners.add(onPageListener);
            return this;
        }

        public Builder<T, I> setPageParser(IPageDataParser pageParser) {
            this.mPageParser = pageParser;
            return this;
        }

        public Builder<T, I> setPageSize(int pageSize) {
            this.mPageSize = pageSize;
            return this;
        }

        public Builder<T, I> addPageDataIntercept(IPageDataIntercept<I> intercept) {
            this.mPageDataIntercepts.add(intercept);
            return this;
        }

        public Builder<T, I> setOnPageListeners(List<OnPageListener> onPageListeners) {
            this.mOnPageListeners = onPageListeners;
            return this;
        }


        public Builder<T, I> setPageDataIntercept(List<IPageDataIntercept<I>> pageDataIntercepts) {
            this.mPageDataIntercepts = pageDataIntercepts;
            return this;
        }

        public PageEngine<T, I> build() {
            PageEngine<T, I> pageEngine = new PageEngine<>(this);
            return pageEngine;
        }
    }

    private Context mContext;
    private PageManager<T, I> mPageManager;
    private IPageAdapter<I> mPageAdapter;
    private OnPageListenerDispatcher mOnPageListenerDispatcher;

    PageEngine(Builder<T, I> builder) {
        this.mContext = builder.mContext;
        this.mPageAdapter = builder.mPageAdapter;
        // 分页事件监听分发器
        this.mOnPageListenerDispatcher = new OnPageListenerDispatcher(builder.mOnPageListeners);
        this.initPullToRefresh(builder);
        this.initPageManager(builder);
    }

    private void initPullToRefresh(Builder<T, I> builder) {
        OnPullToRefreshProvider onPullToRefreshProvider = builder.mOnPullToRefreshProvider;
        if (onPullToRefreshProvider != null) {
            // 设置滑动监听
            onPullToRefreshProvider.setOnRefreshListener(new OnPullToRefreshProvider.OnRefreshListener() {

                @Override
                public void onPullToRefresh() {
                    refreshPageData();
                }

                @Override
                public void onPullToLoadMore() {
                    loadMorePageData();
                }

            });
        }
    }

    private void initPageManager(Builder builder) {
        // 初始化分页管理器
        this.mPageManager = new PageManager<>(mContext, builder.mPageSize);
        final OnPullToRefreshProvider onPullToRefreshProvider = builder.mOnPullToRefreshProvider;
        // 数据请求器
        this.mPageManager.setPageRequester(builder.mPageRequester);
        // 数据解析器
        this.mPageManager.setPageDataParser(builder.mPageParser);
        // 分页处理器
        this.mPageManager.setPageDataIntercepts(builder.mPageDataIntercepts);
        this.mPageManager.setPageDataCallback(new PageManager.IPageDataCallback<I>() {

            @Override
            public void onPageDataCallback(PageAction pageAction, List<I> data, boolean haveNextPage,
                                           boolean isFromCache) {
                // 如果加载缓存数据的时候listview已经适配了数据就不再加载缓存数据了
                if (isFromCache && mPageAdapter.getPageDataCount() != 0) {
                    return;
                }

                List<I> oldPageData = mPageAdapter.getPageListData();

                // 初始化和刷新不会附加数据
                if (pageAction == PageAction.INIT || pageAction == PageAction.REFRESH) {
                    if (mPageAdapter.getPageDataCount() != 0) {
                        mPageAdapter.clear();
                    }
                }

                // 判断是否请求到了数据
                int beforeDataSize = mPageAdapter.getPageDataCount();

                if (data != null) {
                    mPageAdapter.addAll(data);
                }

                int afterDataSize = mPageAdapter.getPageDataCount();

                if (beforeDataSize != afterDataSize) {
                    mPageAdapter.notifyDataSetChanged();
                } else {
                    mPageAdapter.addAll(oldPageData);
                    mPageAdapter.notifyDataSetChanged();
                }

                if (onPullToRefreshProvider != null) {
                    onPullToRefreshProvider.setHaveMoreData(haveNextPage);
                    onPullToRefreshProvider.onRefreshComplete(true);
                    // 根据数据和分页大小来屏蔽加载更多的功能
                    // 如果初始化的时候就把加载更多禁掉了，就说明不会使用加载更多的功能了，所以不加此监听
                    if (onPullToRefreshProvider.isPullLoadMoreEnable()) {
                        if (mPageAdapter.getPageDataCount() != 0
                                && mPageAdapter.getPageDataCount() >= mPageManager.getPage().pageSize) {
                            onPullToRefreshProvider.setPullLoadMoreEnable(true);
                        } else {
                            onPullToRefreshProvider.setPullLoadMoreEnable(false);
                        }
                    }
                }

                if (isFromCache) {
                    boolean isHaveCache =
                            isFromCache && afterDataSize != 0 && afterDataSize - beforeDataSize != 0;
                    mOnPageListenerDispatcher.onPageLoadCache(pageAction, isHaveCache);
                    mOnPageListenerDispatcher.onPageLoadComplete(pageAction, mPageAdapter.getPageDataCount(), isFromCache, true);
                } else {
                    mOnPageListenerDispatcher.onPageLoadComplete(pageAction, mPageAdapter.getPageDataCount(), isFromCache, true);
                }
            }

            @Override
            public void onException(Context context, Throwable t) {
                mOnPageListenerDispatcher.onPageException(t);
            }
        });

    }

    public PageManager<T, I> getPageManager() {
        return mPageManager;
    }

    public void clearPageData() {
        this.mPageAdapter.clear();
        this.mPageAdapter.notifyDataSetChanged();
    }

    public void cancel() {
        mOnPageListenerDispatcher.onPageCancelRequests(this.mPageAdapter.getPageDataCount());
        this.mPageManager.cancel();
    }

    public void initPageData() {
        mOnPageListenerDispatcher.onPageRequestStart(PageAction.INIT);
        mOnPageListenerDispatcher.onPageInit();
        this.mPageManager.init();
    }

    public void refreshPageData() {
        mOnPageListenerDispatcher.onPageRequestStart(PageAction.REFRESH);
        mOnPageListenerDispatcher.onPageRefresh();
        mPageManager.refresh();
    }

    public void loadMorePageData() {
        mOnPageListenerDispatcher.onPageRequestStart(PageAction.LOADMORE);
        mOnPageListenerDispatcher.onPageLoadMore();
        mPageManager.loadMore();
    }

}
