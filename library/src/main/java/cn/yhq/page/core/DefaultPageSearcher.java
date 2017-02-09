package cn.yhq.page.core;

import android.content.Context;

import java.util.List;

/**
 * Created by Yanghuiqiang on 2017/2/9.
 */

public abstract class DefaultPageSearcher<T, I> extends PageSearcher<T, I> implements LetterNameGetter<I> {

    public DefaultPageSearcher(Context context) {
        super(context);
    }

    @Override
    public void executeSearch(Context context, PageAction pageAction, List<I> pageData, String keyword, Page<I> page) {
        List<I> filterResult = LetterFilter.filter(pageData, keyword, this);
        this.callSearchResponse(filterResult);
    }
}
