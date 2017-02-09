package cn.yhq.page.http;

import android.content.Context;

import cn.yhq.http.core.CacheStrategy;
import cn.yhq.http.core.HttpRequester;
import cn.yhq.http.core.ICall;
import cn.yhq.http.core.IHttpRequestListener;
import cn.yhq.http.core.IHttpResponseListener;
import cn.yhq.page.core.Page;
import cn.yhq.page.core.PageAction;
import cn.yhq.page.core.PageRequester;

import static cn.yhq.http.core.CacheStrategy.BOTH;
import static cn.yhq.http.core.CacheStrategy.ONLY_NETWORK;

/**
 * 基于Retrofit的数据请求器
 * <p>
 * Created by Yanghuiqiang on 2016/10/11.
 */

final class RetrofitPageRequester2<T, I> extends PageRequester<T, I> {
    private HttpRequester<T> mHttpRequester;
    private RetrofitPageRequester.IPageRequestExecutor<T, I> pageRequestExecutor;

    public RetrofitPageRequester2(Context context, RetrofitPageRequester.IPageRequestExecutor<T, I> pageRequestExecutor) {
        super(context);
        this.pageRequestExecutor = pageRequestExecutor;
    }

    @Override
    public void executeRequest(Context context, final PageAction pageAction, final Page<I> page) {
        ICall<T> call = pageRequestExecutor.executePageRequest(page.getPageSize(), page.getCurrentPage(), page.getData());
        CacheStrategy cacheStrategy =
                pageAction == PageAction.REFRESH || pageAction == PageAction.LOADMORE ?
                        ONLY_NETWORK :
                        BOTH;
        mHttpRequester = new HttpRequester.Builder<T>(context)
                .cacheStrategy(cacheStrategy)
                .call(call)
                .listener((IHttpRequestListener) null)
                .listener(new IHttpResponseListener<T>() {
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
                }).build();
        mHttpRequester.request();
    }

    @Override
    public void onCancel() {
        if (mHttpRequester == null) {
            return;
        }
        mHttpRequester.cancel();
    }
}
