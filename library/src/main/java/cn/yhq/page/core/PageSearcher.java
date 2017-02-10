package cn.yhq.page.core;

import android.content.Context;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Yanghuiqiang on 2017/2/9.
 */

public abstract class PageSearcher<I> implements IPageSearcher<I>, IFilter<I> {
    protected Context context;
    private PageManager.IPageDataCallback<I> callback;
    private PageAction pageAction;
    private List<String> mHighlightKeywords = new ArrayList<>();
    private List<I> pageData;

    public PageSearcher(Context context) {
        this.context = context;
    }

    public void executeSearch(List<I> pageData, String keyword) {
        keyword = keyword.toLowerCase(Locale.getDefault());
        List<I> list = new ArrayList<>();
        if (TextUtils.isEmpty(keyword)) {
            list = new ArrayList<>(pageData);
        } else {
            for (int i = 0; i < pageData.size(); i++) {
                if (this.filter(keyword, pageData.get(i))) {
                    list.add(pageData.get(i));
                }
            }
        }
        this.callSearchResponse(list);
    }

    protected void callSearchResponse(List<I> response) {
        this.callback.onPageDataCallback(pageAction, response, false);
    }

    protected void callException(Throwable throwable) {
        this.callback.onException(context, pageAction, throwable);
    }

    protected void addHighlightKeyword(String keyword) {
        String uk = keyword.toUpperCase(Locale.getDefault());
        String lk = keyword.toLowerCase(Locale.getDefault());
        if (!mHighlightKeywords.contains(uk)) {
            mHighlightKeywords.add(uk);
        }
        if (!mHighlightKeywords.contains(lk)) {
            mHighlightKeywords.add(lk);
        }
    }

    @Override
    public void setPageData(List<I> pageData) {
        this.pageData = pageData;
    }

    @Override
    public final void onSearch(PageAction pageAction, String keyword, PageManager.IPageDataCallback<I> callback) {
        this.callback = callback;
        this.pageAction = pageAction;
        this.mHighlightKeywords.clear();
        if (TextUtils.isEmpty(keyword)) {
            this.callback.onPageDataCallback(PageAction.REFRESH, pageData, false);
        } else {
            executeSearch(pageData, keyword);
        }
    }

    @Override
    public void onCancel() {

    }

    @Override
    public List<String> getHighlightKeywords() {
        return mHighlightKeywords;
    }

}
