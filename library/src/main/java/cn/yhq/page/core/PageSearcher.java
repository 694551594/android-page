package cn.yhq.page.core;

import android.content.Context;

import java.util.List;

/**
 * Created by Yanghuiqiang on 2017/2/9.
 */

public abstract class PageSearcher<T, I> implements IPageSearcher<T, I> {
    private Context context;
    private PageManager.IPageDataCallback<I> callback;
    private PageAction pageAction;
    private Page<I> page;
    private String keyword;
    private List<I> pageData;

    public PageSearcher(Context context) {
        this.context = context;
    }

    void setPageData(List<I> pageData) {
        this.pageData = pageData;
    }

    void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public abstract void executeSearch(Context context, PageAction pageAction, List<I> pageData, String keyword, Page<I> page);

    protected void callSearchResponse(List<I> response) {
        this.callback.onPageDataCallback(pageAction, response, page.haveNextPage(), false, true);
    }

    protected void callException(Throwable throwable) {
        this.callback.onException(context, pageAction, throwable);
    }

    @Override
    public final void onSearch(PageAction pageAction, Page<I> page, PageManager.IPageDataCallback<I> callback) {
        this.callback = callback;
        this.pageAction = pageAction;
        this.page = page;
        executeSearch(context, pageAction, pageData, keyword, page);
    }

    @Override
    public void onCancel() {

    }

}
