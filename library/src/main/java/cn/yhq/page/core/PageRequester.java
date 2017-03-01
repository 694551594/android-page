package cn.yhq.page.core;

import android.content.Context;

/**
 * Created by Yanghuiqiang on 2016/10/11.
 */

public abstract class PageRequester<T, I> implements IPageRequester<T, I> {
    private Context context;
    private IPageResponse<T> pageResponse;
    private PageAction pageAction;

    public PageRequester(Context context) {
        this.context = context;
    }

    @Override
    public final void onRequest(PageAction pageAction, Page<I> page, IPageResponse<T> pageResponse) {
        this.pageResponse = pageResponse;
        this.pageAction = pageAction;
        executeRequest(context, pageAction, page);
    }

    public abstract void executeRequest(Context context, PageAction pageAction, Page<I> page);

    protected final void callResponse(T response) {
        this.callCacheResponse(null);
        this.callNetworkResponse(response);
    }

    protected final void callNetworkResponse(T response) {
        this.pageResponse.onResponse(pageAction, response, false);
    }

    protected final void callCacheResponse(T response) {
        this.pageResponse.onResponse(pageAction, response, true);
    }

    protected final void callException(Throwable throwable) {
        this.pageResponse.onException(context, pageAction, throwable);
    }


}
