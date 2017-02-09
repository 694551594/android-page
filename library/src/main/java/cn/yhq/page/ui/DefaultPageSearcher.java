package cn.yhq.page.ui;

import android.content.Context;

import cn.yhq.page.core.Page;
import cn.yhq.page.core.PageSearcher;

/**
 * Created by Yanghuiqiang on 2017/2/9.
 */

public class DefaultPageSearcher<T, I> extends PageSearcher<T, I> {

    public DefaultPageSearcher(Context context) {
        super(context);
    }

    @Override
    public void executeSearch(Context context, T pageData, String keyword, Page<I> page) {

    }

}
