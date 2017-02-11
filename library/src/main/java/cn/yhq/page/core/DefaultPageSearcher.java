package cn.yhq.page.core;

import android.content.Context;
import android.text.TextUtils;

import java.util.Locale;

import cn.yhq.utils.PinyinUtils;

/**
 * Created by Yanghuiqiang on 2017/2/9.
 */

public class DefaultPageSearcher<I> extends PageSearcher<I> {
    private IFilterName<I> filterName;

    public DefaultPageSearcher(Context context, IFilterName<I> filterName) {
        super(context);
        this.filterName = filterName;
    }

    @Override
    public boolean filter(String keyword, I entity) {
        String name = filterName.getFilterName(entity);
        return filterByName(keyword, entity, name);
    }

    protected boolean filterByName(String keyword, I entity, String name) {
        if (TextUtils.isEmpty(name)) {
            return false;
        }
        return filterByPinyin(keyword, name);
    }

    protected final boolean filterByPinyin(String keyword, String name) {
        if (name.indexOf(keyword) != -1) {
            addHighlightKeyword(keyword);
            return true;
        } else {
            String pinyin = PinyinUtils.getPinYin(name).toLowerCase(Locale.getDefault());
            if (pinyin.startsWith(keyword)) {
                addHighlightKeyword(name.substring(0, 1));
                return true;
            }
        }
        return false;
    }

}
