package cn.yhq.page.http;

import cn.yhq.page.core.IPageRequester;
import cn.yhq.page.core.PageAction;
import cn.yhq.page.ui.PageDataActivity;

public abstract class RetrofitPageDataActivity<T, I> extends PageDataActivity<T, I>
        implements
        RetrofitPageRequester.IPageRequestExecutor<T, I> {

    @Override
    public void onPageLoadComplete(PageAction pageAction, int count, boolean isFromCache, boolean isSuccess) {
        super.onPageLoadComplete(pageAction, count, isFromCache, isSuccess);
        if (pageAction == PageAction.INIT && isFromCache) {
            this.refreshPageData();
        }
    }

    @Override
    public IPageRequester<T, I> getPageRequester() {
        return new RetrofitPageRequester<>(this, this);
    }


}
