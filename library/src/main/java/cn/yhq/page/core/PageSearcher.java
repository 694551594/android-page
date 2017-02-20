package cn.yhq.page.core;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yanghuiqiang on 2017/2/9.
 */

public abstract class PageSearcher<I> implements IPageSearcher<I>, IFilter<I> {
    protected Context mContext;
    private PageManager.IPageDataCallback<I> mCallback;
    private PageAction mPageAction;
    private List<I> mPageData;
    private List<String> mHighlightKeywords = new ArrayList<>();

    public PageSearcher(Context context) {
        this.mContext = context;
    }

    protected final void addHighlightKeyword(String keyword) {
        if (!mHighlightKeywords.contains(keyword)) {
            mHighlightKeywords.add(keyword);
        }
    }

    @Override
    public final List<String> getHighlightKeywords() {
        return mHighlightKeywords;
    }

    protected void executeSearch(List<I> pageData, String keyword) {
        List<I> list;
        if (TextUtils.isEmpty(keyword)) {
            list = handleNullKeyword(pageData);
        } else {
            list = handleNotNullKeyword(pageData, keyword);
        }
        this.callSearchResponse(list);
    }

    protected final void callSearchResponse(List<I> response) {
        this.mCallback.onPageDataCallback(mPageAction, response, false);
    }

    protected final void callException(Throwable throwable) {
        this.mCallback.onException(mContext, mPageAction, throwable);
    }

    public final void setPageData(List<I> mPageData) {
        this.mPageData = mPageData;
    }

    @Override
    public final void onSearch(PageAction pageAction, String keyword, PageManager.IPageDataCallback<I> callback) {
        this.mHighlightKeywords.clear();
        this.mCallback = callback;
        this.mPageAction = pageAction;
        if (TextUtils.isEmpty(keyword)) {
            this.mCallback.onPageDataCallback(PageAction.REFRESH, mPageData, false);
        } else {
            executeSearch(mPageData, keyword);
        }
    }

    @Override
    public void onCancel() {

    }

    protected List<I> handleNotNullKeyword(List<I> pageData, String keyword) {
        List<I> list = new ArrayList<>();
        for (int i = 0; i < pageData.size(); i++) {
            if (this.filter(keyword, pageData.get(i))) {
                list.add(pageData.get(i));
            }
        }
        return list;
    }

    protected List<I> handleNullKeyword(List<I> pageData) {
        return new ArrayList<>(pageData);
    }

    @Override
    public boolean saveState(Bundle state, OnPageDataStateSaved<I> listener) {
        listener.onStateSaved(state, "PageSearcher.mPageData", mPageData);
        return true;
    }

    @Override
    public boolean restoreState(Bundle state, OnPageDataStateSaved<I> listener) {
        mPageData = listener.onStateRestored(state, "PageSearcher.mPageData");
        return true;
    }
}
