package cn.yhq.page.http;

import cn.yhq.page.core.IPageRequester;
import cn.yhq.page.core.PageAction;
import cn.yhq.page.ui.PageDataFragment;

/**
 * 使用retrofit和okhttp封装的分页列表请求处理框架
 *
 * @param <T>
 * @param <I>
 * @author Yanghuiqiang 2015-10-10
 */
public abstract class RetrofitPageDataFragment<T, I> extends PageDataFragment<T, I>
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
        return new RetrofitPageRequester<>(this.getContext(), this);
    }
}
