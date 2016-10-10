package cn.yhq.page.core;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Loader;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;

import com.markmao.pulltorefresh.widget.XExpandableListView;
import com.markmao.pulltorefresh.widget.XListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.developer.sdk.page2.adapter.IPageAdapter;
import cn.developer.sdk.page2.adapter.OnListDataChangeListener;
import cn.developer.sdk.page2.list.DefaultPullToRefreshContext;
import cn.developer.sdk.page2.list.PullToRefreshExpandableListViewContext;
import cn.developer.sdk.page2.list.PullToRefreshListViewContext;
import cn.developer.sdk.page2.list.PullToRefreshRecyclerListViewContext;
import cn.developer.sdk.recycler.BaseRecyclerView;
import cn.developer.sdk.recycler.XRecyclerListView;

/**
 * 分页上下文管理（分页框架核心类）
 * <p>
 * 本分页列表管理框架具备以下功能：
 * <p>
 * 分页自动管理；
 * <p>
 * 下拉刷新，上拉加载更多，默认使用xlistview，并支持其他框架扩展；
 * <p>
 * 列表状态保存与恢复， 分页列表request与response的处理。
 * <p>
 * <p>
 * 实现原理：
 * <p>
 * PageManager-负责分页的管理，包括对页数的维护以及对数据的请求，包括初始化、下拉刷新、加载更多等； PageRequester-负责从服务器请求数据，请求到的数据类型为L泛型；
 * PageParser-负责解析请求到的数据，获取到分页数据的总大小与当前页的数据，数据列表类型为泛型L，列表条目类型为泛型I；
 * PageHandler-其实是一个拦截器，负责对解析后的数据作一些特殊处理；
 * PageStateSaver-分页数据的状态保存，负责分页列表数据的状态保存与恢复，对应于Activity或者Fragment的生命周期； PageConfig-负责分页的一些配置；
 * PageViewManager-负责正在加载或者空数据的时候的视图显示，包括点击重新加载的操作
 * 其中PageRequester与PageParser是必须提供的，PageHandler是非必须的。
 *
 * @param <T> 服务器返回的数据类型，如JsonObject
 * @param <L> 要转换成列表的Bean类型，如List
 * @param <I> 要转换成列表的Bean条目的类型，如List<T>里的T
 * @author Yanghuiqiang 2015-5-21
 */
public final class PageContext<T, L, I> implements IPageContext<T, L, I> {
  private static final String TAG = "PageContext";
  private static final String KEY_SAVED_STATE = "saved_state_pagedata";
  // 适配器接口
  private IPageAdapter<L, I> mPageListAdapter;
  // 上拉刷新下拉加载的接口
  private IPullToRefreshListener mPullToRefreshListener;
  // listview组件
  private Context mContext;
  // 分页管理器
  private PageManager<T, L, I> mPageManager;
  // 分页数据载入监听
  private PageManager.IPageDataLoadListener<L> mPageDataLoadListener;
  private PageDataLoader<L, I> mPageDataLoaderTask;
  private Loader.OnLoadCompleteListener<L> mPageDataLoadCompleteListener;
  // 分页数据处理器
  private IPageDataHandler<L> mPageDataHandler;
  // 状态保存
  private PageState mPageState = new PageState();
  // 分页数据状态保存器
  private IPageStateSaver mPageStateSaver;
  // 分页监听事件分发器
  private OnPageListener mOnPageListenerDispatcher;
  // 分页配置
  private PageConfig mPageConfig;

  private static final Map<String, PageContext<?, ?, ?>> pageContexts = new HashMap<>();

  public static class Builder<T, L, I> {
    // 适配器接口
    private IPageAdapter<L, I> mPageListAdapter;
    // 上拉刷新下拉加载的接口
    private IPullToRefreshListener mPullToRefreshListener;
    // listview组件
    private IPageViewProvider mPageViewProvider;
    private PageViewManager mPageViewManager;
    private View mPageView;
    private Context mContext;
    // 分页请求接口
    private PageManager.IPageDataRequester<T, I> mPageDataRequester;
    // 分页处理器
    private IPageDataHandler<L> mPageDataHandler;
    // 分页数据处理过程监听
    private List<OnPageListener> mOnPageListeners = new ArrayList<>();
    // 分页解析器
    private PageManager.IPageDataParser mPageDataParser;
    // 分页配置
    private PageConfig mPageConfig;
    private String mTag;

    private Builder(Context context) {
      this.mContext = context;
      this.mPageConfig = new PageConfig();
    }

    public static <T, L, I> Builder<T, L, I> fromPageContextProvider(Context context,
        IPageContextProvider<T, L, I> pageContextProvider) {
      Builder<T, L, I> builder = new Builder<>(context);
      // 分页配置
      pageContextProvider.onPageConfig(builder.mPageConfig);
      builder.setPageView(pageContextProvider.getPageView())
          .setAdapter(pageContextProvider.getPageAdapter())
          .setPageDataHandler(pageContextProvider.getPageDataHandler())
          .setPageDataParser(pageContextProvider.getPageDataParser())
          .setPageRequester(pageContextProvider.getPageDataRequester());
      // build监听
      pageContextProvider.onPageContextBuild(builder);
      return builder;
    }

    public static <T, L, I> Builder<T, L, I> newBuilder(Context context) {
      Builder<T, L, I> builder = new Builder<>(context);
      return builder;
    }

    public Builder<T, L, I> setPageView(View pageView) {
      this.mPageView = pageView;
      return this;
    }

    public Builder<T, L, I> setPageDataParser(PageManager.IPageDataParser pageDataParser) {
      this.mPageDataParser = pageDataParser;
      return this;
    }

    public Builder<T, L, I> setAdapter(IPageAdapter<L, I> pageListAdapter) {
      this.mPageListAdapter = pageListAdapter;
      return this;
    }

    public Builder<T, L, I> setPullToRefreshListener(IPullToRefreshListener pullToRefreshListener) {
      this.mPullToRefreshListener = pullToRefreshListener;
      return this;
    }

    public Builder<T, L, I> setPageViewProvider(IPageViewProvider pageViewProvider) {
      this.mPageViewProvider = pageViewProvider;
      return this;
    }

    public Builder<T, L, I> setPageRequester(PageManager.IPageDataRequester pageRequester) {
      this.mPageDataRequester = pageRequester;
      return this;
    }

    Builder<T, L, I> setPageConfig(PageConfig pageConfig) {
      this.mPageConfig = pageConfig;
      return this;
    }

    public Builder<T, L, I> setPageDataHandler(IPageDataHandler handler) {
      this.mPageDataHandler = handler;
      return this;
    }

    public Builder<T, L, I> addOnPageListener(OnPageListener onPageListener) {
      this.mOnPageListeners.add(onPageListener);
      return this;
    }

    public Builder<T, L, I> setTag(String tag) {
      this.mTag = tag;
      return this;
    }

    public Builder<T, L, I> setTag(Class<?> tag) {
      this.mTag = tag.getName();
      return this;
    }

    /**
     * 获取默认的下拉刷新组件
     * 
     * @return
     */
    private IPullToRefreshListener getPullToRefreshListener() {
      View pageView = this.mPageView;
      if (pageView instanceof XExpandableListView) {
        return new PullToRefreshExpandableListViewContext((XExpandableListView) pageView);
      } else if (pageView instanceof XListView) {
        return new PullToRefreshListViewContext((XListView) pageView);
      } else if (pageView instanceof XRecyclerListView) {
        return new PullToRefreshRecyclerListViewContext((XRecyclerListView) pageView);
      }
      return new DefaultPullToRefreshContext();
    }

    /**
     * 获取默认的PageView提供组件
     * 
     * @return
     */
    private IPageViewProvider getPageViewProvider() {
      View pageView = this.mPageView;
      if (pageView instanceof AbsListView) {
        return new PageListViewProvider((AbsListView) pageView);
      } else if (pageView instanceof BaseRecyclerView) {
        return new PageRecyclerViewProvider((BaseRecyclerView) pageView);
      }
      return null;
    }

    private void check() {
      checkNullArgs(mContext, "Context不可为空");
      checkNullArgs(mPageView, "PageView不可为空");
      checkNullArgs(mPageListAdapter, "PageListAdapter不可为空");
      checkNullArgs(mPageDataRequester, "PageDataRequester不可为空");
      checkNullArgs(mPageDataParser, "PageDataParser不可为空");
    }

    public PageContext<T, L, I> build() {
      // 参数检测
      check();

      if (this.mPullToRefreshListener == null) {
        this.mPullToRefreshListener = getPullToRefreshListener();
      }

      if (this.mPageViewProvider == null) {
        this.mPageViewProvider = this.getPageViewProvider();
      }

      this.mPageViewManager = new PageViewManager();
      this.mPageViewManager.setPageViewProvider(mPageViewProvider);
      // 是否总是显示Listview
      this.mPageViewManager.setListViewAlwaysVisible(mPageConfig.listViewAlwaysVisiable);
      this.addOnPageListener(mPageViewManager);

      final PageContext<T, L, I> pageContext = new PageContext<>(this);
      this.mPageViewManager.setOnReRequestListener(new OnReRequestListener() {
        @Override
        public void onReRequest() {
          pageContext.initPageData();
        }
      });
      return pageContext;
    }
  }

  PageContext(Builder builder) {
    this.mPageConfig = builder.mPageConfig;
    this.mPageListAdapter = builder.mPageListAdapter;
    this.mPullToRefreshListener = builder.mPullToRefreshListener;
    // 设置滑动监听
    this.mPullToRefreshListener
        .setOnRefreshListener(new IPullToRefreshListener.OnRefreshListener() {

          @Override
          public void pullToRefresh() {
            try {
              forceRefresh();
            } catch (Exception e) {}
          }

          @Override
          public void pullToLoadMore() {
            try {
              if (mOnPageListenerDispatcher != null) {
                mOnPageListenerDispatcher.onPageRequestStart(PageManager.PageRequestType.LOADMORE);
                mOnPageListenerDispatcher.onPageLoadMore();
              }
              if (mPageManager != null) {
                mPageManager.onLoadMore();
              }
            } catch (Exception e) {}
          }

        });
    // 如果pullEnable为false，则上拉和下拉都设置为false
    if (!mPageConfig.pullEnable) {
      mPageConfig.pullRefreshEnable = false;
      mPageConfig.pullLoadMoreEnable = false;
    }
    this.mPullToRefreshListener.pullRefreshEnable(mPageConfig.pullRefreshEnable);
    this.mPullToRefreshListener.pullLoadMoreEnable(mPageConfig.pullLoadMoreEnable);
    this.mContext = builder.mContext;
    // 初始化分页管理器
    this.mPageManager = new PageManager(this.mContext);
    this.mPageManager.initPageInfo(mPageConfig.pageSize);
    // 数据请求器
    this.mPageManager.setPageDataRequester(builder.mPageDataRequester);
    // 数据载入事件监听
    this.mPageDataLoadListener = new PageDataLoadListener();
    this.mPageManager.setPageDataLoadListener(mPageDataLoadListener);
    // 数据解析器
    this.mPageManager.setPageDataParser(builder.mPageDataParser);

    // 根据数据和分页大小来屏蔽加载更多的功能
    // 如果初始化的时候就把加载更多禁掉了，就说明不会使用加载更多的功能了，所以不加此监听
    if (mPullToRefreshListener.pullLoadMoreEnable()) {
      mPageListAdapter.setListDataChangeListener(new OnListDataChangeListener() {

        @Override
        public void onDataChange(boolean haveData, int size) {
          if (haveData && size >= mPageManager.getPage().pageSize) {
            mPullToRefreshListener.pullLoadMoreEnable(true);
          } else {
            mPullToRefreshListener.pullLoadMoreEnable(false);
          }

        }

      });
    }
    // 分页数据处理器
    this.mPageDataHandler = builder.mPageDataHandler;
    // 数据状态保存期
    this.mPageStateSaver = new DefaultPageStateSaver();

    // 分页事件监听分发器
    this.mOnPageListenerDispatcher = new OnPageListenerDispatcher(builder.mOnPageListeners);

    pageContexts.put(builder.mTag, this);

  }

  public static <T, L, I> PageContext<T, L, I> getPageContext(Class<?> contextClass) {
    return (PageContext<T, L, I>) pageContexts.get(contextClass.getName());
  }

  public static <T, L, I> void forceRefresh(Class<?> contextClass) {
    PageContext<T, L, I> pageContext = getPageContext(contextClass);
    if (pageContext != null) {
      pageContext.forceRefresh();
    }
  }

  public static <T, L, I> void initPageData(Class<?> contextClass) {
    PageContext<T, L, I> pageContext = getPageContext(contextClass);
    if (pageContext != null) {
      pageContext.initPageData();
    }
  }

  public void clearPageListData() {
    mPageListAdapter.clear();
    mPageListAdapter.notifyDataSetChanged();
  }

  private static void checkNullArgs(Object o, String message) {
    if (o == null) {
      throw new NullPointerException(message);
    }
  }

  /**
   * 强制刷新列表，请求到的数据是最新，注意，这个方法请求数据的时候即使有缓存也不会加载（回调）
   */
  @Override
  public void forceRefresh() {
    if (mOnPageListenerDispatcher != null) {
      mOnPageListenerDispatcher.onPageRequestStart(PageManager.PageRequestType.REFRESH);
      mOnPageListenerDispatcher.onPageRefresh();
    }
    if (mPageManager != null) {
      mPageManager.onRefresh();
    }
  }

  /**
   * 针对新的okhttp请求方式： 请求数据，可能有缓存，所以调用此方法返回的数据可能不是最新的，如果要获取最新的数据，需要调用forceRefresh
   * 上面的问题已经解决了，现在调用此方法会在加载完缓存后会去重新请求网络上的数据。 当然，如果没有缓存的时候会直接请求网络上的数据。
   */
  @Override
  public void initPageData() {
    // 取消之前的请求
    cancel();

    if (mPageConfig.clearPageDataBeforeRequest) {
      clearPageListData();
    }

    if (mOnPageListenerDispatcher != null) {
      mOnPageListenerDispatcher.onPageRequestStart(PageManager.PageRequestType.INIT);
    }

    // 初始化页
    if (mPageManager != null) {
      mPageManager.onInit();
    }
  }

  @Override
  public void onSavePageData(Bundle savedInstanceState) {
    try {
      if (savedInstanceState == null) {
        return;
      }
      // 保存配置信息，配置信息是必须保存的
      mPageConfig.onSaveInstanceState(savedInstanceState);
      // 数据信息是根据配置信息的配置决定是否保存
      if (!mPageConfig.savedStateEnable) {
        return;
      }
      // 保存数据信息
      if (mPageStateSaver != null) {
        mPageState.adapterSavedState = mPageListAdapter.onSaveInstanceState();
        mPageState.pageInfoSavedState = this.mPageManager.getPage().onSaveInstanceState();
        mPageState.absListViewSavedState = this.mPullToRefreshListener.onSaveInstanceState();
        mPageState.pageRequestSavedState = new Bundle();
        this.mOnPageListenerDispatcher.onPageSaveInstanceState(mPageState.pageRequestSavedState);
        Bundle bundle = mPageStateSaver.onSave(mPageState);
        savedInstanceState.putBundle(KEY_SAVED_STATE, bundle);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void onRestorePageData(Bundle savedInstanceState) {
    try {
      if (savedInstanceState == null) {
        return;
      }
      // 恢复配置信息
      mPageConfig.onRestoreInstanceState(savedInstanceState);
      // 根据配置信息决定是否恢复数据
      if (!mPageConfig.savedStateEnable) {
        return;
      }
      // 恢复数据信息
      if (mPageStateSaver != null) {
        Bundle bundle = savedInstanceState.getBundle(KEY_SAVED_STATE);
        mPageState = mPageStateSaver.onRestore(bundle);
        // 恢复分页信息
        this.mPageListAdapter.onRestoreInstanceState(mPageState.adapterSavedState);
        this.mPageManager.getPage().onRestoreInstanceState(mPageState.pageInfoSavedState);
        this.mPullToRefreshListener.onRestoreInstanceState(mPageState.absListViewSavedState);
        this.mOnPageListenerDispatcher.onPageRestoreInstanceState(mPageState.pageRequestSavedState);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void cancelRequest() {
    if (mPageManager != null) {
      mPageManager.cancelRequests();
    }

    if (mOnPageListenerDispatcher != null) {
      mOnPageListenerDispatcher.onPageCancelRequests();
    }
  }

  @Override
  public void onDestroy() {
    cancel();
    pageContexts.remove(mContext.getClass());
  }

  @Override
  public void onCreated(Bundle savedInstanceState) {
    try {
      // 状态恢复
      this.onRestorePageData(savedInstanceState);

      if (savedInstanceState == null && mPageConfig.autoInitPageData) {
        this.initPageData();
      }

    } catch (Exception e) {
      Debug.debug(TAG, e);
    }
  }

  public void cancel() {
    try {
      cancelRequest();
      cancelPageDataParser();
      cancelPageDataLoader();
    } catch (Exception e) {
      Debug.debug(TAG, e);
    }
  }

  private void cancelPageDataLoader() {
    if (mPageManager != null) {
      mPageManager.cancelPageDataParser();
    }
  }

  private void cancelPageDataParser() {
    try {
      mPageDataLoaderTask.stopLoading();
      mPageDataLoaderTask.unregisterListener(mPageDataLoadCompleteListener);
    } catch (Exception e) {}
  }

  @Override
  public PageManager<T, L, I> getPageManager() {
    return this.mPageManager;
  }

  public PageConfig getPageConfig() {
    return this.mPageConfig;
  }

  static class PageDataLoader<L, I> extends AsyncTaskLoader<L> {
    private IPageDataHandler<L> mPageDataHandler;
    private L mData;
    private boolean isFromCache;
    private PageManager.PageRequestType mPageRequestType;

    public PageDataLoader(Context context, PageManager.PageRequestType pageRequestType, L data,
        boolean isFromCache, IPageDataHandler<L> pageDataHandler) {
      super(context);
      this.mPageRequestType = pageRequestType;
      this.mData = data;
      this.isFromCache = isFromCache;
      this.mPageDataHandler = pageDataHandler;
    }

    @Override
    public L loadInBackground() {
      try {
        if (mData == null) {
          return null;
        }
        if (mPageDataHandler == null) {
          return mData;
        }
        if (isFromCache && mData == null) {
          return mData;
        }
        return mPageDataHandler.handle(mPageRequestType, mData);
      } catch (Exception e) {
        Debug.debug(TAG, e);
        return mData;
      }
    }

    @Override
    protected void onStopLoading() {
      cancelLoad();
    }

    @Override
    protected void onStartLoading() {
      forceLoad();
    }

  }

  class PageDataLoadListener implements PageManager.IPageDataLoadListener<L> {

    @Override
    public void onPageDataLoadException(PageManager.PageRequestType pageRequestType,
        PageException e, boolean isFromCache) {
      try {
        mPullToRefreshListener.onRefreshComplete(true);

        if (mOnPageListenerDispatcher != null) {
          mOnPageListenerDispatcher.onPageException(pageRequestType, e);
          mOnPageListenerDispatcher.onPageLoadComplete(pageRequestType, isFromCache, false);
        }
      } catch (Exception ex) {}
    }

    @Override
    public void onPageDataLoadComplete(final PageManager.PageRequestType pageRequestType,
        final L pageData, final boolean haveNextPage, final boolean isFromCache) {
      /**
       * 分页数据处理任务，自定义的分页处理接口
       *
       */
      mPageDataLoaderTask =
          new PageDataLoader<>(mContext, pageRequestType, pageData, isFromCache, mPageDataHandler);
      mPageDataLoadCompleteListener = new Loader.OnLoadCompleteListener<L>() {

        @Override
        public void onLoadComplete(Loader<L> loader, L data) {
          try {
            onPageDataHandle(pageRequestType, data, haveNextPage, isFromCache);
          } catch (Exception e) {
            Debug.debug(TAG, e);
          }
        }

      };
      mPageDataLoaderTask.registerListener(hashCode(), mPageDataLoadCompleteListener);
      mPageDataLoaderTask.startLoading();
    }

  }

  private void onPageDataHandle(PageManager.PageRequestType pageRequestType, L pageData,
      boolean haveNextPage, boolean isFromCache) {

    // 如果加载缓存数据的时候listview已经适配了数据就不再加载缓存数据了
    if (isFromCache && mPageListAdapter.getPageDataCount() != 0) {
      return;
    }

    L oldPageData = mPageListAdapter.getPageListData();

    // 初始化和刷新不会附加数据
    if (pageRequestType == PageManager.PageRequestType.INIT
        || pageRequestType == PageManager.PageRequestType.REFRESH) {
      if (mPageListAdapter.getPageDataCount() != 0) {
        mPageListAdapter.clear();
      }
    }

    // 判断是否请求到了数据
    int beforeDataSize = mPageListAdapter.getPageDataCount();

    if (pageData != null) {
      mPageListAdapter.addAll(pageData);
    }

    int afterDataSize = mPageListAdapter.getPageDataCount();

    if (beforeDataSize != afterDataSize) {
      mPageListAdapter.notifyDataSetChanged();
    } else {
      mPageListAdapter.addAll(oldPageData);
      mPageListAdapter.notifyDataSetChanged();
    }

    mPullToRefreshListener.setHasMoreData(haveNextPage);
    mPullToRefreshListener.onRefreshComplete(true);

    if (isFromCache) {
      boolean isHaveCache =
          isFromCache && afterDataSize != 0 && afterDataSize - beforeDataSize != 0;
      mOnPageListenerDispatcher.onPageLoadCache(pageRequestType, isHaveCache);
      mOnPageListenerDispatcher.onPageLoadComplete(pageRequestType, isFromCache, true);
    } else {
      mOnPageListenerDispatcher.onPageLoadComplete(pageRequestType, isFromCache, true);
    }

  }

  public static <L> IPageDataHandler<L> getDefaultPageDataHandler() {
    return new IPageDataHandler<L>() {

      @Override
      public L handle(PageManager.PageRequestType pageRequestType, L pageData) {
        return pageData;
      }
    };
  }
}
