package cn.yhq.page.http;

import android.content.Context;

import cn.yhq.http.core.CacheStrategy;
import cn.yhq.http.core.ICall;
import cn.yhq.http.core.IHttpResponseListener;
import cn.yhq.page.core.Page;
import cn.yhq.page.core.PageAction;
import cn.yhq.page.core.PageRequester;

import static cn.yhq.http.core.CacheStrategy.FIRST_CACHE_THEN_REQUEST;
import static cn.yhq.http.core.CacheStrategy.ONLY_NETWORK;

/**
 * 基于Retrofit的数据请求器
 * <p>
 * Created by Yanghuiqiang on 2016/10/11.
 */

final class RetrofitPageRequester<T, I> extends PageRequester<T, I> {
    private ICall<T> mCall;
    private IPageRequestExecutor<T, I> pageRequestExecutor;

    public interface IPageRequestExecutor<T, I> {
        ICall<T> executePageRequest(int pageSize, int currentPage, I mData);
    }

    public RetrofitPageRequester(Context context, IPageRequestExecutor<T, I> pageRequestExecutor) {
        super(context);
        this.pageRequestExecutor = pageRequestExecutor;
    }

    @Override
    public void executeRequest(Context context, final PageAction pageAction, final Page<I> page) {
        mCall = pageRequestExecutor.executePageRequest(page.getPageSize(), page.getCurrentPage(), page.getData());
        CacheStrategy cacheStrategy =
                pageAction == PageAction.REFRESH || pageAction == PageAction.LOADMORE ?
                        ONLY_NETWORK :
                        FIRST_CACHE_THEN_REQUEST;
        mCall.cacheStrategy(cacheStrategy).execute(context, null, new IHttpResponseListener<T>() {
            @Override
            public void onResponse(Context context, int requestCode, T response, boolean isFromCache) {
                if (isFromCache) {
                    callCacheResponse(response);
                } else {
                    callNetworkResponse(response);
                }
            }

            @Override
            public void onException(Context context, Throwable t) {
                callException(t);
            }
        });
    }

    @Override
    public void onCancel() {
        if (mCall == null) {
            return;
        }
        mCall.cancel();
    }
}
