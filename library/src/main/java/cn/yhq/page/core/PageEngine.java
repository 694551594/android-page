package cn.yhq.page.core;

import android.content.Context;
import android.os.Bundle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * 分页管理引擎
 * <p>
 * Created by Yanghuiqiang on 2016/10/11.
 */
public final class PageEngine<T, I> {
    private Context mContext;
    private PageManager<T, I> mPageManager;
    private IPageAdapter<I> mPageAdapter;
    private OnPageListenerDispatcher mOnPageListenerDispatcher;

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
        // 加载更多数据的时候是加载到最前面还是最后面
        private DataAppendMode mDataAppendMode;

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

        public Builder<T, I> setDataAppendMode(DataAppendMode dataAppendMode) {
            this.mDataAppendMode = dataAppendMode;
            return this;
        }

        public PageEngine<T, I> build() {
            PageEngine<T, I> pageEngine = new PageEngine<>(this);
            return pageEngine;
        }
    }

    PageEngine(Builder<T, I> builder) {
        this.mContext = builder.mContext;
        this.mPageAdapter = builder.mPageAdapter;
        // 分页事件监听分发器
        this.mOnPageListenerDispatcher = new OnPageListenerDispatcher(builder.mOnPageListeners);
        this.setupPullToRefreshProvider(builder);
        this.setupPageManager(builder);
    }

    private final void setupPullToRefreshProvider(Builder<T, I> builder) {
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

    private final void setupPageManager(Builder builder) {
        // 初始化分页管理器
        this.mPageManager = new PageManager<>(mContext, builder.mPageSize);
        final OnPullToRefreshProvider onPullToRefreshProvider = builder.mOnPullToRefreshProvider;
        final DataAppendMode dataAppendMode = builder.mDataAppendMode;
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
                    appendData(data);
                }

                int afterDataSize = mPageAdapter.getPageDataCount();

                if (beforeDataSize != afterDataSize) {
                    mPageAdapter.notifyDataSetChanged();
                } else {
                    appendData(oldPageData);
                    mPageAdapter.notifyDataSetChanged();
                }

                if (onPullToRefreshProvider != null) {
                    onPullToRefreshProvider.setHaveMoreData(haveNextPage);
                    onPullToRefreshProvider.onRefreshComplete(afterDataSize - beforeDataSize, true);
                    // 根据数据和分页大小来屏蔽加载更多的功能
                    // 如果初始化的时候就把加载更多禁掉了，就说明不会使用加载更多的功能了，所以不加此监听
                    if (mPageAdapter.getPageDataCount() != 0
                            && mPageAdapter.getPageDataCount() >= mPageManager.getPage().pageSize) {
                        onPullToRefreshProvider.setPullLoadMoreEnable(true);
                    } else {
                        onPullToRefreshProvider.setPullLoadMoreEnable(false);
                    }
                }

                if (isFromCache) {
                    boolean isHaveCache =
                            isFromCache && afterDataSize != 0 && afterDataSize - beforeDataSize != 0;
                    mOnPageListenerDispatcher.onPageLoadCache(pageAction, isHaveCache);
                    mOnPageListenerDispatcher.onPageLoadComplete(pageAction, isFromCache, true);
                } else {
                    mOnPageListenerDispatcher.onPageLoadComplete(pageAction, isFromCache, true);
                }
            }

            private void appendData(List<I> data) {
                if (dataAppendMode == DataAppendMode.MODE_AFTER) {
                    mPageAdapter.appendAfter(data);
                } else if (dataAppendMode == DataAppendMode.MODE_BEFORE) {
                    mPageAdapter.appendBefore(data);
                }
            }

            @Override
            public void onException(Context context, PageAction pageAction, Throwable t) {
                Timber.e(t, t.getLocalizedMessage());

                if (onPullToRefreshProvider != null) {
                    onPullToRefreshProvider.onRefreshComplete(0, false);
                }

                mOnPageListenerDispatcher.onPageException(t);
                mOnPageListenerDispatcher.onPageLoadComplete(pageAction, false, false);
            }
        });

    }

    public final PageManager<T, I> getPageManager() {
        return mPageManager;
    }

    public final void clearPageData() {
        this.mPageAdapter.clear();
        this.mPageAdapter.notifyDataSetChanged();
    }

    public final void cancel() {
        mOnPageListenerDispatcher.onPageCancelRequests();
        this.mPageManager.cancel();
    }

    public final void initPageData() {
        mOnPageListenerDispatcher.onPageRequestStart(PageAction.INIT);
        mOnPageListenerDispatcher.onPageInit();
        this.mPageManager.init();
    }

    public final void refreshPageData() {
        mOnPageListenerDispatcher.onPageRequestStart(PageAction.REFRESH);
        mOnPageListenerDispatcher.onPageRefresh();
        mPageManager.refresh();
    }

    public final void loadMorePageData() {
        mOnPageListenerDispatcher.onPageRequestStart(PageAction.LOADMORE);
        mOnPageListenerDispatcher.onPageLoadMore();
        mPageManager.loadMore();
    }

    public final boolean saveState(Bundle state) {
        try {
            mPageManager.saveState(state);
            if (mPageAdapter.getPageDataCount() != 0) {
                // 判断是否序列化了。如果序列化就可以直接传值
                if (mPageAdapter.getPageListData() instanceof Serializable) {
                    state.putSerializable("PageData", (Serializable) mPageAdapter.getPageListData());
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public final boolean restoreState(Bundle state) {
        try {
            mPageManager.restoreState(state);
            List<I> listData = (List<I>) state.getSerializable("PageData");
            if (listData != null) {
                mPageAdapter.clear();
                mPageAdapter.appendAfter(listData);
                mPageAdapter.notifyDataSetChanged();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
