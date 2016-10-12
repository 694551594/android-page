package cn.yhq.page.core;

import android.content.Context;

/**
 * Created by Yanghuiqiang on 2016/10/11.
 */

public abstract class PageRequester<T, I> implements IPageRequester<T, I> {
    private Context context;
    private IPageResponse<T> pageResponse;
    private PageAction pageAction;
    private Page<I> page;

    public PageRequester(Context context) {
        this.context = context;
    }

    @Override
    public final void onRequest(PageAction pageAction, Page<I> page, IPageResponse<T> pageResponse) {
        this.page = page;
        this.pageResponse = pageResponse;
        this.pageAction = pageAction;
        executeRequest(context, pageAction, page);
    }

    public abstract void executeRequest(Context context, PageAction pageAction, Page<I> page);

    protected void callNetworkResponse(T response) {
        this.pageResponse.onResponse(pageAction, response, false);
    }

    protected void callCacheResponse(T response) {
        this.pageResponse.onResponse(pageAction, response, true);
    }

    protected void callException(Throwable throwable) {
        this.pageResponse.onException(context, throwable);
    }


}
