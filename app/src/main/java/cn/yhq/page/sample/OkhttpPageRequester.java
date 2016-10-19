package cn.yhq.page.sample;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.http.okhttp.request.RequestCall;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import cn.yhq.page.core.Page;
import cn.yhq.page.core.PageAction;
import cn.yhq.page.core.PageRequester;
import okhttp3.Call;

/**
 * Created by Yanghuiqiang on 2016/10/12.
 */

public abstract class OkHttpPageRequester<T, I> extends PageRequester<T, I> {
    private RequestCall mRequestCall;

    public OkHttpPageRequester(Context context) {
        super(context);
    }

    public abstract RequestCall getRequestCall(int pageSize, int pageIndex, I data);

    @Override
    public void executeRequest(final Context context, PageAction pageAction, Page<I> page) {
        mRequestCall = getRequestCall(page.pageSize, page.currentPage, page.mData);
        mRequestCall
                .execute(new StringCallback() {

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(context, "数据请求失败，请稍后重试", Toast.LENGTH_LONG).show();
                        callException(e);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Type type = ((ParameterizedType) OkHttpPageRequester.this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
                        T entity = new Gson().fromJson(response, type);
                        callNetworkResponse(entity);
                    }
                });
    }

    @Override
    public void onCancel() {
        if (mRequestCall != null) {
            mRequestCall.cancel();
        }
    }
}
