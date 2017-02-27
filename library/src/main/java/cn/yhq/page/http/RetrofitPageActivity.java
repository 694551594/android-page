package cn.yhq.page.http;

import cn.yhq.page.core.IPageRequester;
import cn.yhq.page.ui.PageActivity;

public abstract class RetrofitPageActivity<T, I> extends PageActivity<T, I>
        implements
        RetrofitPageRequester.IPageRequestExecutor<T, I> {

    @Override
    public IPageRequester<T, I> getPageRequester() {
        return new RetrofitPageRequester<>(this, this);
    }


}
