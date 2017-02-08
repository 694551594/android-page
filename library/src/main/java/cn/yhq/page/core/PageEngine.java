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
    private IPageContext<T, I> mPageContext;
    private OnPageListenerDispatcher mOnPageListenerDispatcher;

    PageEngine(Context context, IPageContext<T, I> pageContext) {
        this.mContext = context;
        this.mPageContext = pageContext;
        this.mOnPageListenerDispatcher = new OnPageListenerDispatcher();
        init();
    }

    private PageManager.IPageDataCallback<I> mPageDataCallback = new PageManager.IPageDataCallback<I>() {

        @Override
        public void onPageDataCallback(PageAction pageAction, List<I> data, boolean haveNextPage,
                                       boolean isFromCache) {
            IPageAdapter<I> pageAdapter = mPageContext.getPageAdapter();
            OnPullToRefreshProvider onPullToRefreshProvider = mPageContext.getOnPullToRefreshProvider();

            // 如果加载缓存数据的时候listview已经适配了数据就不再加载缓存数据了
            if (isFromCache && pageAdapter.getPageDataCount() != 0) {
                return;
            }

            List<I> oldPageData = pageAdapter.getPageListData();

            // 初始化和刷新不会附加数据
            if (pageAction == PageAction.INIT || pageAction == PageAction.REFRESH) {
                if (pageAdapter.getPageDataCount() != 0) {
                    pageAdapter.clear();
                }
            }

            // 判断是否请求到了数据
            int beforeDataSize = pageAdapter.getPageDataCount();

            if (data != null) {
                appendPageData(pageAdapter, data);
            }

            int afterDataSize = pageAdapter.getPageDataCount();

            if (beforeDataSize != afterDataSize) {
                pageAdapter.notifyDataSetChanged();
            } else {
                appendPageData(pageAdapter, oldPageData);
                pageAdapter.notifyDataSetChanged();
            }

            if (onPullToRefreshProvider != null) {
                // 根据数据和分页大小来屏蔽加载更多的功能
                // 如果初始化的时候就把加载更多禁掉了，就说明不会使用加载更多的功能了，所以不加此监听
                if (pageAdapter.getPageDataCount() != 0
                        && pageAdapter.getPageDataCount() >= mPageManager.getPage().pageSize) {
                    onPullToRefreshProvider.setPullLoadMoreEnable(true);
                } else {
                    onPullToRefreshProvider.setPullLoadMoreEnable(false);
                }
                onPullToRefreshProvider.setHaveMoreData(haveNextPage);
                onPullToRefreshProvider.onRefreshComplete(afterDataSize - beforeDataSize, true);
            }

            if (isFromCache) {
                boolean isHaveCache =
                        isFromCache && afterDataSize != 0 && afterDataSize - beforeDataSize != 0;
                mOnPageListenerDispatcher.onPageLoadCache(pageAction, isHaveCache);
                mOnPageListenerDispatcher.onPageLoadComplete(pageAction, isFromCache, true);
            } else {
                mOnPageListenerDispatcher.onPageLoadComplete(pageAction, isFromCache, true);
            }

            mAllPageListData = new ArrayList<>(pageAdapter.getPageListData());

        }

        private void appendPageData(IPageAdapter<I> pageAdapter, List<I> data) {
            if (pageAdapter.getDataAppendMode() == DataAppendMode.MODE_AFTER) {
                pageAdapter.appendAfter(data);
            } else if (pageAdapter.getDataAppendMode() == DataAppendMode.MODE_BEFORE) {
                pageAdapter.appendBefore(data);
            }
        }

        @Override
        public void onException(Context context, PageAction pageAction, Throwable t) {
            Timber.e(t, t.getLocalizedMessage());

            OnPullToRefreshProvider onPullToRefreshProvider = mPageContext.getOnPullToRefreshProvider();

            if (onPullToRefreshProvider != null) {
                onPullToRefreshProvider.onRefreshComplete(0, false);
            }

            mOnPageListenerDispatcher.onPageException(t);
            mOnPageListenerDispatcher.onPageLoadComplete(pageAction, false, false);
        }
    };

    private void init() {
        // 初始化分页管理器
        this.mPageManager = new PageManager<>(mContext, mPageContext.getPageSize());
        // 回调处理
        this.mPageManager.setPageDataCallback(mPageDataCallback);
        // 拦截器
        this.mPageManager.setPageDataIntercepts(mPageContext.getPageDataIntercepts());

        OnPullToRefreshProvider onPullToRefreshProvider = mPageContext.getOnPullToRefreshProvider();
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

    public final void addOnPageListener(OnPageListener listener) {
        this.mOnPageListenerDispatcher.addOnPageListener(listener);
    }

    public final void addPageDataIntercept(IPageDataIntercept<I> intercept) {
        this.mPageManager.addPageDataIntercept(intercept);
    }

    private void prepare() {
        // 数据请求器
        this.mPageManager.setPageRequester(mPageContext.getPageRequester());
        // 数据解析器
        this.mPageManager.setPageDataParser(mPageContext.getPageParser());
    }

    public final PageManager<T, I> getPageManager() {
        return mPageManager;
    }

    public final void clearPageData() {
        IPageAdapter<I> pageAdapter = mPageContext.getPageAdapter();
        pageAdapter.clear();
        pageAdapter.notifyDataSetChanged();
    }

    public final void cancel() {
        mOnPageListenerDispatcher.onPageCancelRequests();
        this.mPageManager.cancel();
    }

    public final void initPageData() {
        mOnPageListenerDispatcher.onPageRequestStart(PageAction.INIT);
        mOnPageListenerDispatcher.onPageInit();
        prepare();
        this.mPageManager.init();
    }

    public final void refreshPageData() {
        mOnPageListenerDispatcher.onPageRequestStart(PageAction.REFRESH);
        mOnPageListenerDispatcher.onPageRefresh();
        prepare();
        mPageManager.refresh();
    }

    public final void loadMorePageData() {
        mOnPageListenerDispatcher.onPageRequestStart(PageAction.LOADMORE);
        mOnPageListenerDispatcher.onPageLoadMore();
        prepare();
        mPageManager.loadMore();
    }

    public final boolean saveState(Bundle state) {
        try {
            mPageManager.saveState(state);
            IPageAdapter<I> pageAdapter = mPageContext.getPageAdapter();
            if (pageAdapter.getPageDataCount() != 0) {
                // 判断是否序列化了。如果序列化就可以直接传值
                if (pageAdapter.getPageListData() instanceof Serializable) {
                    state.putSerializable("PageData", (Serializable) pageAdapter.getPageListData());
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
                IPageAdapter<I> pageAdapter = mPageContext.getPageAdapter();
                pageAdapter.clear();
                pageAdapter.appendAfter(listData);
                pageAdapter.notifyDataSetChanged();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private List<I> mAllPageListData;

    public final void searchPageData(String keyword, LetterNameGetter<I> listener) {
        List<I> result = LetterFilter.filter(mAllPageListData, keyword, listener);
        IPageAdapter<I> pageAdapter = mPageContext.getPageAdapter();
        pageAdapter.clear();
        pageAdapter.appendAfter(result);
        pageAdapter.notifyDataSetChanged();
    }
}
