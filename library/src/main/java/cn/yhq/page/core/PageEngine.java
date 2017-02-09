package cn.yhq.page.core;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import java.io.Serializable;
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
    private OnPullToRefreshProvider mOnPullToRefreshProvider;
    private IPageAdapter<I> mPageAdapter;
    private OnPageListenerDispatcher mOnPageListenerDispatcher;
    private IPageRequester<T, I> mPageRequester;
    private IPageDataParser<T, I> mPageDataParser;
    private PageSearchHandler<T, I> mPageSearchHandler;

    public PageEngine(Context context,
                      int pageSize,
                      IPageRequester<T, I> pageRequester,
                      IPageDataParser<T, I> pageDataParser,
                      OnPullToRefreshProvider onPullToRefreshProvider,
                      IPageAdapter<I> pageAdapter,
                      IPageSearcher<T, I> pageSearcher
    ) {
        this.mContext = context;
        this.mOnPageListenerDispatcher = new OnPageListenerDispatcher();

        // 初始化分页管理器
        this.mPageManager = new PageManager<>(mContext, pageSize);
        // 回调处理
        this.mPageManager.setPageDataCallback(mPageDataCallback);
        // 数据请求器
        this.mPageManager.setPageRequester(pageRequester);
        // 数据解析器
        this.mPageManager.setPageDataParser(pageDataParser);

        this.mOnPullToRefreshProvider = onPullToRefreshProvider;
        if (this.mOnPullToRefreshProvider != null) {
            // 设置滑动监听
            this.mOnPullToRefreshProvider.setOnRefreshListener(new OnPullToRefreshProvider.OnRefreshListener() {

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

        this.mPageAdapter = pageAdapter;
        this.mPageRequester = pageRequester;
        this.mPageSearchHandler = new PageSearchHandler<>(pageSearcher);

        this.mPageDataParser = pageDataParser;
    }

    private PageManager.IPageDataCallback<I> mPageDataCallback = new PageManager.IPageDataCallback<I>() {

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
                appendPageData(data);
            }

            int afterDataSize = mPageAdapter.getPageDataCount();

            if (beforeDataSize != afterDataSize) {
                mPageAdapter.notifyDataSetChanged();
            } else {
                appendPageData(oldPageData);
                mPageAdapter.notifyDataSetChanged();
            }

            if (mOnPullToRefreshProvider != null) {
                // 根据数据和分页大小来屏蔽加载更多的功能
                // 如果初始化的时候就把加载更多禁掉了，就说明不会使用加载更多的功能了，所以不加此监听
                if (mPageAdapter.getPageDataCount() != 0
                        && mPageAdapter.getPageDataCount() >= mPageManager.getPage().pageSize) {
                    mOnPullToRefreshProvider.setPullLoadMoreEnable(true);
                } else {
                    mOnPullToRefreshProvider.setPullLoadMoreEnable(false);
                }
                mOnPullToRefreshProvider.setHaveMoreData(haveNextPage);
                mOnPullToRefreshProvider.onRefreshComplete(afterDataSize - beforeDataSize, true);
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

        private void appendPageData(List<I> data) {
            if (mPageAdapter.getDataAppendMode() == DataAppendMode.MODE_AFTER) {
                mPageAdapter.appendAfter(data);
            } else if (mPageAdapter.getDataAppendMode() == DataAppendMode.MODE_BEFORE) {
                mPageAdapter.appendBefore(data);
            }
        }

        @Override
        public void onException(Context context, PageAction pageAction, Throwable t) {
            Timber.e(t, t.getLocalizedMessage());

            if (mOnPullToRefreshProvider != null) {
                mOnPullToRefreshProvider.onRefreshComplete(0, false);
            }

            mOnPageListenerDispatcher.onPageException(t);
            mOnPageListenerDispatcher.onPageLoadComplete(pageAction, false, false);
        }
    };

    public final void addOnPageListener(OnPageListener listener) {
        this.mOnPageListenerDispatcher.addOnPageListener(listener);
    }

    public final void addPageDataIntercept(IPageDataIntercept<I> intercept) {
        this.mPageManager.addPageDataIntercept(intercept);
    }

    public final PageManager<T, I> getPageManager() {
        return mPageManager;
    }

    public final void clearPageData() {
        mPageAdapter.clear();
        mPageAdapter.notifyDataSetChanged();
    }

    public final void cancel() {
        mOnPageListenerDispatcher.onPageCancelRequests();
        this.mPageManager.cancel();
    }

    public final void initPageData() {
        mOnPageListenerDispatcher.onPageRequestStart(PageAction.INIT);
        mOnPageListenerDispatcher.onPageInit();
        this.mPageManager.setPageRequester(mPageRequester);
        this.mPageManager.doAction(PageAction.INIT);
    }

    public final void refreshPageData() {
        mOnPageListenerDispatcher.onPageRequestStart(PageAction.REFRESH);
        mOnPageListenerDispatcher.onPageRefresh();
        this.mPageManager.setPageRequester(mPageRequester);
        this.mPageManager.doAction(PageAction.REFRESH);
    }

    public final void loadMorePageData() {
        mOnPageListenerDispatcher.onPageRequestStart(PageAction.LOADMORE);
        mOnPageListenerDispatcher.onPageLoadMore();
        this.mPageManager.setPageRequester(mPageRequester);
        this.mPageManager.doAction(PageAction.LOADMORE);
    }

    public final void searchPageData(String keyword) {
        if (TextUtils.isEmpty(keyword)) {
            initPageData();
        } else {
            this.mPageSearchHandler.setKeyword(keyword);
            this.mPageSearchHandler.setPageData(this.mPageManager.getPageResponseData());
            this.mPageSearchHandler.setPageDataParser(this.mPageDataParser);
            this.mPageManager.setPageRequester(mPageSearchHandler);
            this.mPageManager.doAction(PageAction.SEARCH);
        }
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
