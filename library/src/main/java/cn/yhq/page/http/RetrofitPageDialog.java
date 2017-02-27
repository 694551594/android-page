package cn.yhq.page.http;

import android.content.Context;

import cn.yhq.page.core.IPageRequester;
import cn.yhq.page.ui.PageDialog;

/**
 * 使用retrofit和okhttp封装的分页列表请求处理框架
 *
 * @param <T>
 * @param <I>
 * @author Yanghuiqiang 2015-10-10
 */
public abstract class RetrofitPageDialog<T, I> extends PageDialog<T, I>
        implements
        RetrofitPageRequester.IPageRequestExecutor<T, I> {

    public RetrofitPageDialog(Context context) {
        super(context);
    }

    @Override
    public IPageRequester<T, I> getPageRequester() {
        return new RetrofitPageRequester<>(this.getContext(), this);
    }
}
