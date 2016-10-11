package cn.yhq.page.http;

import android.content.Context;

import cn.yhq.http.core.CacheStrategy;
import cn.yhq.http.core.HttpRequester;
import cn.yhq.http.core.IHttpRequestListener;
import cn.yhq.http.core.IHttpRequestProvider;
import cn.yhq.http.core.IHttpResponseListener;
import cn.yhq.page.core.Page;
import cn.yhq.page.core.PageAction;
import cn.yhq.page.core.PageRequester;
import retrofit2.Call;

/**
 * 基于Retrofit的数据请求器
 * <p>
 * Created by Yanghuiqiang on 2016/10/11.
 */

final class RetrofitPageRequester<T, I> extends PageRequester<T, I> {
    private final static int PAGE_REQUEST_CODE = -1;
    private Call<T> mCall;
    private IPageRequestExecutor<T, I> pageRequestExecutor;

    public interface IPageRequestExecutor<T, I> {
        Call<T> executePageRequest(int pageSize, int currentPage, I mData);
    }

    public RetrofitPageRequester(Context context, IPageRequestExecutor<T, I> pageRequestExecutor) {
        super(context);
        this.pageRequestExecutor = pageRequestExecutor;
    }

    @Override
    public void executeRequest(Context context, final PageAction pageAction, final Page<I> page) {
        mCall = new HttpRequester.Builder<T>(context).provider(new IHttpRequestProvider<T>() {

            @Override
            public int getRequestCode() {
                return PAGE_REQUEST_CODE;
            }

            @Override
            public Call<T> execute(int requestCode) {
                return pageRequestExecutor.executePageRequest(page.pageSize, page.currentPage, page.mData);
            }

            @Override
            public CacheStrategy getCacheStrategy() {
                if (pageAction == PageAction.REFRESH || pageAction == PageAction.LOADMORE) {
                    return CacheStrategy.ONLY_NETWORK;
                }
                return CacheStrategy.BOTH;
            }

        }).listener(new IHttpResponseListener<T>() {

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

        }).listener((IHttpRequestListener<T>) null).request();
    }

    @Override
    public void onCancel() {
        if (mCall == null) {
            return;
        }
        mCall.cancel();
    }
}
