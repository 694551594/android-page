package cn.yhq.page.core;

import android.content.Context;

/**
 * Created by Yanghuiqiang on 2017/2/9.
 */

public abstract class PageSearcher<T, I> implements IPageSearcher<T, I> {
    private Context context;
    private IPageResponse<T> pageResponse;

    public PageSearcher(Context context) {
        this.context = context;
    }

    public abstract void executeSearch(Context context, T pageData, String keyword, Page<I> page);

    protected void callSearchResponse(T response) {
        this.pageResponse.onResponse(PageAction.SEARCH, response, false);
    }

    protected void callException(Throwable throwable) {
        this.pageResponse.onException(context, PageAction.SEARCH, throwable);
    }

    @Override
    public void onSearch(T pageData, String keyword, Page<I> page, IPageResponse<T> pageResponse) {
        this.pageResponse = pageResponse;
        executeSearch(context, pageData, keyword, page);
    }

    @Override
    public void onCancel() {

    }

}
