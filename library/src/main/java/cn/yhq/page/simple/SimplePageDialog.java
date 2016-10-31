package cn.yhq.page.simple;

import android.content.Context;

import java.util.List;

import cn.yhq.page.core.IPageDataParser;
import cn.yhq.page.core.IPageRequester;
import cn.yhq.page.ui.PageDataDialog;

/**
 * Created by Yanghuiqiang on 2016/10/21.
 */

public abstract class SimplePageDialog<I> extends PageDataDialog<List<I>, I> {

    public SimplePageDialog(Context context) {
        super(context);
    }

    @Override
    public IPageDataParser<List<I>, I> getPageDataParser() {
        return new SimplePageDataParser();
    }

    @Override
    public IPageRequester<List<I>, I> getPageRequester() {
        return new SimplePageRequester<I>(this.getContext()) {
            @Override
            public List<I> getSimplePageData() {
                return getPageData();
            }
        };
    }

    public abstract List<I> getPageData();

}
