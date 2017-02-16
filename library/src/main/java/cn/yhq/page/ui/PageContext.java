package cn.yhq.page.ui;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import java.util.List;

import cn.yhq.page.core.DefaultOnPageListener;
import cn.yhq.page.core.DefaultPageSearcher;
import cn.yhq.page.core.IFilterName;
import cn.yhq.page.core.IPageAdapter;
import cn.yhq.page.core.IPageChecker;
import cn.yhq.page.core.IPageDataIntercept;
import cn.yhq.page.core.IPageSearcher;
import cn.yhq.page.core.OnPageCheckedChangeListener;
import cn.yhq.page.core.OnPageCheckedEquals;
import cn.yhq.page.core.OnPageCheckedInitListener;
import cn.yhq.page.core.OnPageListener;
import cn.yhq.page.core.OnPullToRefreshProvider;
import cn.yhq.page.core.PageAction;
import cn.yhq.page.core.PageChecker;
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
    private PageViewManager mPageViewManager;
    private IPageViewProvider mPageViewProvider;
    private IPageAdapter<I> mPageAdapter;
    private View mPageView;

    public PageContext(Context context, IPageContextProvider<T, I> provider) {
        this.mContext = context;
        this.mPageContextProvider = provider;
        provider.onPageConfig(mPageConfig);
        mPageEngine = new PageEngine(this.mContext, mPageConfig.pageSize);
        mPageViewManager = new PageViewManager();
        mPageViewManager.setOnReRequestListener(new OnReRequestListener() {
            @Override
            public void onReRequest() {
                initPageData();
            }
        });
        mPageEngine.addOnPageListener(new DefaultOnPageListener() {
            @Override
            public void onPageCancelRequests() {
                mPageViewManager.cancelPageRequest(mPageAdapter.getPageDataCount());
            }

            @Override
            public void onPageRequestStart(PageAction pageAction) {
                mPageViewManager.startPageRequest(pageAction);
            }

            @Override
            public void onPageLoadComplete(PageAction pageAction, boolean isFromCache, boolean isSuccess) {
                mPageViewManager.completePageRequest(pageAction, mPageAdapter.getPageDataCount());
            }
        });
    }

    private final void prepare() {
        View pageView = mPageContextProvider.getPageView();
        if (mPageView != pageView) {
            mPageView = pageView;
            mPageViewProvider = new PageViewProvider(mPageView);
            mPageViewManager.inflate(mPageViewProvider);
            OnPullToRefreshProvider onPullToRefreshProvider = PullToRefreshContextFactory.getPullToRefreshProvider(mPageView);
            this.setOnPullToRefreshProvider(onPullToRefreshProvider);
        }
        mPageAdapter = mPageContextProvider.getPageAdapter();
        mPageEngine.setPageDataParser(mPageContextProvider.getPageDataParser());
        mPageEngine.setPageRequester(mPageContextProvider.getPageRequester());
        mPageEngine.setPageAdapter(mPageAdapter);
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

    public final void attachSearchEditText(final EditText searchEditText, IFilterName<I> filterName) {
        this.attachSearchEditText(searchEditText, new DefaultPageSearcher<>(searchEditText.getContext(), filterName));
    }

    public final void attachSearchEditText(final EditText searchEditText, IPageSearcher<I> pageSearcher) {
        this.setPageSearcher(pageSearcher);
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

    public final void setOnPullToRefreshProvider(OnPullToRefreshProvider onPullToRefreshProvider) {
        this.mPageEngine.setOnPullToRefreshProvider(onPullToRefreshProvider);
    }

    public final IPageChecker<I> getPageChecker() {
        return mPageEngine.getPageChecker();
    }

    public final void setPageChecker(IPageChecker<I> pageChecker) {
        this.mPageEngine.setPageChecker(pageChecker);
    }

    public final void setPageChecker(int type, final OnPageCheckedChangeListener<I> listener1, OnPageCheckedInitListener<I> listener2) {
        setPageChecker(type, new OnPageCheckedEquals<I>() {
            @Override
            public boolean equals(I t1, I t2) {
                return t1 == t2;
            }
        }, listener1, listener2);
    }

    public final void setPageChecker(int type, final OnPageCheckedChangeListener<I> listener) {
        setPageChecker(type, new OnPageCheckedEquals<I>() {
            @Override
            public boolean equals(I t1, I t2) {
                return t1 == t2;
            }
        }, listener);
    }

    public final void setPageChecker(int type, OnPageCheckedEquals<I> equals, final OnPageCheckedChangeListener<I> listener) {
        setPageChecker(type, equals, listener, new OnPageCheckedInitListener<I>() {
            @Override
            public boolean isEnable(int position, I entity) {
                return true;
            }

            @Override
            public boolean isChecked(int position, I entity) {
                return false;
            }
        });
    }

    public final void setPageChecker(int type, OnPageCheckedEquals<I> equals, final OnPageCheckedChangeListener<I> listener1, OnPageCheckedInitListener<I> listener2) {
        PageChecker<I> pageChecker = new PageChecker<>(type, equals, listener2);
        pageChecker.setOnCheckedChangeListener(new OnPageCheckedChangeListener<I>() {
            @Override
            public void onPageCheckedChanged(List<I> checkedList, int count) {
                mPageAdapter.notifyDataSetChanged();
                listener1.onPageCheckedChanged(checkedList, count);
            }
        });
        this.mPageEngine.setPageChecker(pageChecker);
    }

    public final void setPageSearcher(IPageSearcher<I> pageSearcher) {
        this.mPageEngine.setPageSearcher(pageSearcher);
    }

    public final void setPageViewProvider(IPageViewProvider pageViewProvider) {
        this.mPageViewProvider = pageViewProvider;
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
