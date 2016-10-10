package cn.yhq.page.core;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Loader;
import android.content.Loader.OnLoadCompleteListener;


/**
 * 分页管理
 * 
 * @author Yanghuiqiang 2014-9-5
 * 
 */
public final class PageManager<T, L, I> {
  public final static String TAG = "PageManager";
  // 分页信息
  private Page<I> mPage;
  // 请求类型
  private PageRequestType mPageRequestType;
  // 分页请求接口
  private IPageDataRequester<T, I> mPageDataRequester;
  // 分页回调接口
  private IPageDataResponseCallback<T> mPageDataResponseCallback;
  // 分页数据载入接口
  private IPageDataLoadListener<L> mPageDataLoadListener;
  private IPageDataParser<T, L, I> mPageDataParser;

  private Context mContext;

  // 标示是否请求了网络数据
  private boolean isRequestNetwork;


  /**
   * 分页列表数据请求回调
   * 
   * @author Yanghuiqiang 2015-5-24
   * 
   * @param <T>
   */
  public interface IPageDataResponseCallback<T> {

    /**
     * 数据回调
     * 
     * @param response
     * @param isFromCache
     */
    public void onPageDataResponse(T response, boolean isFromCache);

    /**
     * 异常回调
     * 
     * @param e
     */
    public void onPageDataResponseException(PageException e);

  }

  /**
   * 分页列表数据请求器
   * 
   * @author Yanghuiqiang 2015-5-24
   * 
   * @param <T>
   * @param <I>
   */
  public interface IPageDataRequester<T, I> {
    /**
     * 数据请求
     * 
     * @param pageRequestType
     * @param page
     * @param callback
     */
    public void onPageDataRequest(PageRequestType pageRequestType, Page<I> page,
                                  IPageDataResponseCallback<T> callback);

    /**
     * 取消请求
     * 
     * @param pageRequestType
     * @param page
     */
    public void onCancelRequest(PageRequestType pageRequestType, Page<I> page);
  }

  /**
   * 分页列表数据载入监听
   * 
   * @author Yanghuiqiang 2015-5-24
   * 
   * @param <L>
   */
  public interface IPageDataLoadListener<L> {
    // 载入完成
    void onPageDataLoadComplete(PageRequestType pageRequestType, L pageData, boolean haveNextPage,
                                boolean isFromCache);

    // 发生异常
    void onPageDataLoadException(PageRequestType pageRequestType, PageException e,
                                 boolean isFromCache);
  }

  /**
   * 分页列表数据解析器
   * 
   * @author Yanghuiqiang 2015-5-24
   * 
   * @param <T>
   * @param <L>
   * @param <I>
   */
  public interface IPageDataParser<T, L, I> {
    /**
     * 从请求数据中获取item
     * 
     * @param list
     * @param position
     * @return
     */
    public I getPageDataItem(L list, int position);

    /**
     * 从请求数据中获取列表数据的总数
     * 
     * @param list
     * @return
     */
    public int getPageDataSize(L list);

    /**
     * 从请求数据中获取数据的列表
     * 
     * @param data
     * @param isFromCache
     * @return
     */
    public L getPageList(T data, boolean isFromCache);

    /**
     * 从请求中获取总数据的总数
     * 
     * @param data
     * @param isFromCache
     * @return
     */
    public long getPageTotal(T data, boolean isFromCache);

  }

  /** 分页请求类型 **/
  public enum PageRequestType {
    /** 初始化 **/
    INIT,
    /** 刷新 **/
    REFRESH,
    /** 加载更多 **/
    LOADMORE;
  }

  public PageManager(Context context, Page<I> pageInfo) {
    this.mPage = pageInfo;
    this.mContext = context;
  }

  public PageManager(Context context) {
    this.mContext = context;
  }

  public void initPageInfo(Page<I> pageInfo) {
    this.mPage = pageInfo;
  }

  public void setPageDataLoadListener(IPageDataLoadListener<L> listener) {
    this.mPageDataLoadListener = listener;
  }

  public IPageDataLoadListener<L> getPageDataLoadListener() {
    return this.mPageDataLoadListener;
  }

  public void setPageDataRequester(IPageDataRequester<T, I> pageDataRequester) {
    this.mPageDataRequester = pageDataRequester;
  }

  public IPageDataRequester<T, I> getPageDataRequester() {
    return this.mPageDataRequester;
  }

  public void setPageDataParser(IPageDataParser<T, L, I> pageDataItemParser) {
    this.mPageDataParser = pageDataItemParser;
  }

  public void cancelRequests() {
    try {
      if (mPageDataRequester != null) {
        mPageDataRequester.onCancelRequest(mPageRequestType, mPage);
      }
    } catch (Exception e) {
      if (mPageDataLoadListener != null) {
        mPageDataLoadListener.onPageDataLoadException(mPageRequestType,
            new PageException(e.getLocalizedMessage()), false);
      }
    }
  }

  /**
   * 初始化分页信�?
   * 
   */
  public void initPageInfo(int pageSize) {
    mPage = new Page<I>();
    mPage.pageSize = pageSize;
    mPage.currentPage = 1;
  }

  public void initPageInfo() {
    mPage = new Page<I>();
    mPage.currentPage = 1;
  }

  /**
   * 初始化
   *
   */
  public void onInit() {
    try {
      mPageRequestType = PageRequestType.INIT;
      isRequestNetwork = false;
      doRefresh();
    } catch (Exception e) {
      if (mPageDataLoadListener != null) {
        mPageDataLoadListener.onPageDataLoadException(mPageRequestType,
            new PageException(e.getLocalizedMessage()), false);
      }
    }
  }

  public Page<I> getPage() {
    return mPage;
  }

  /**
   * 刷新列表
   * 
   */
  public void onRefresh() {
    try {
      mPageRequestType = PageRequestType.REFRESH;
      isRequestNetwork = false;
      doRefresh();
    } catch (Exception e) {
      if (mPageDataLoadListener != null) {
        mPageDataLoadListener.onPageDataLoadException(mPageRequestType,
            new PageException(e.getLocalizedMessage()), false);
      }
    }
  }

  private void doRefresh() {
    initPageInfo(mPage.pageSize);
    if (mPageDataRequester != null) {
      debug("Refresh PageInfo");
      mPageDataResponseCallback = new PageDataResponseCallback();
      mPageDataRequester.onPageDataRequest(mPageRequestType, mPage, mPageDataResponseCallback);
    }
  }

  public static void debug(String tag, String log) {
    Debug.debug(TAG, tag + ":" + log);
  }

  public static void debug(String log) {
    Debug.debug(TAG, log);
  }

  /**
   * 载入更多
   * 
   */
  public void onLoadMore() {
    try {
      mPageRequestType = PageRequestType.LOADMORE;
      isRequestNetwork = false;
      mPage.next();
      doLoadMore();
    } catch (Exception e) {
      if (mPageDataLoadListener != null) {
        mPageDataLoadListener.onPageDataLoadException(mPageRequestType,
            new PageException(e.getLocalizedMessage()), false);
      }
    }
  }

  private void doLoadMore() {
    if (mPageDataRequester != null) {
      debug("LoadMore PageInfo");
      mPageDataResponseCallback = new PageDataResponseCallback();
      mPageDataRequester.onPageDataRequest(mPageRequestType, mPage, mPageDataResponseCallback);
    }
  }

  public PageRequestType getPageRequestType() {
    return mPageRequestType;
  }

  public IPageDataParser<T, L, I> getPageDataParser() {
    return mPageDataParser;
  }

  private PageDataParserTask<T, L, I> mPageDataParserTask;
  private OnLoadCompleteListener<L> mPageParseCompleteListener;

  public void cancelPageDataParser() {
    try {
      mPageDataParserTask.stopLoading();
      mPageDataParserTask.unregisterListener(mPageParseCompleteListener);
      mPageDataParserTask = null;
    } catch (Exception e) {}
  }

  static class PageDataParserTask<T, L, I> extends AsyncTaskLoader<L> {
    private T mData;
    private boolean isFromCache;
    private PageRequestType mPageRequestType;
    private IPageDataParser<T, L, I> mPageDataParser;
    private Page<I> mPage;

    public PageDataParserTask(Context context, PageRequestType pageRequestType, T data,
        boolean isFromCache, Page<I> page, IPageDataParser<T, L, I> pageDataParser) {
      super(context);
      this.mPageRequestType = pageRequestType;
      this.mData = data;
      this.isFromCache = isFromCache;
      this.mPageDataParser = pageDataParser;
      this.mPage = page;
    }

    @Override
    public L loadInBackground() {
      try {
        if (mPageDataParser == null) {
          return null;
        }
        if (mPageRequestType == PageRequestType.INIT
            || mPageRequestType == PageRequestType.REFRESH) {
          mPage.dataSize = mPageDataParser.getPageTotal(mData, isFromCache);
          mPage.init();
        }
        L result = mPageDataParser.getPageList(mData, isFromCache);
        if (result != null && mPageDataParser.getPageDataSize(result) != 0) {
          mPage.mData =
              mPageDataParser.getPageDataItem(result, mPageDataParser.getPageDataSize(result) - 1);
        }
        return result;
      } catch (Exception e) {
        Debug.debug(TAG, e);
        return null;
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

  class PageDataResponseCallback implements IPageDataResponseCallback<T> {

    public PageDataResponseCallback() {}

    @Override
    public void onPageDataResponse(final T data, final boolean isFromCache) {

      debug("isFromCache - " + isFromCache + "  isRequestNetwork - " + isRequestNetwork);
      if (!isFromCache) {
        isRequestNetwork = true;
      }
      debug("isFromCache - " + isFromCache + "  isRequestNetwork - " + isRequestNetwork);
      // 判断是否已经请求到网络上的数据了，如果请求了则不加缓存
      if (isFromCache && isRequestNetwork) {
        debug("加载缓存的时候请求到了数据，直接返回");
        return;
      }

      debug("isFromCache - " + isFromCache + "  isRequestNetwork - " + isRequestNetwork);

      mPageDataParserTask = new PageDataParserTask<T, L, I>(mContext, mPageRequestType, data,
          isFromCache, mPage, mPageDataParser);
      mPageParseCompleteListener = new OnLoadCompleteListener<L>() {

        @Override
        public void onLoadComplete(Loader<L> loader, L data) {
          try {
            if (mPageDataLoadListener != null) {
              mPageDataLoadListener.onPageDataLoadComplete(mPageRequestType, data,
                  mPage.haveNextPage(), isFromCache);
            }
          } catch (Exception e) {
            Debug.debug(TAG, e);
          }
        }

      };
      mPageDataParserTask.registerListener(hashCode(), mPageParseCompleteListener);
      mPageDataParserTask.startLoading();
    }

    @Override
    public void onPageDataResponseException(final PageException e) {
      if (mPageDataLoadListener != null) {
        mPageDataLoadListener.onPageDataLoadException(mPageRequestType,
            new PageException(e.getLocalizedMessage()), false);
      }
    }

  }

}
