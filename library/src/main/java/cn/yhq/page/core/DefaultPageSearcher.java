package cn.yhq.page.core;

import android.content.Context;

import cn.yhq.utils.PinyinUtils;

/**
 * Created by Yanghuiqiang on 2017/2/9.
 */

public abstract class DefaultPageSearcher<I> extends PageSearcher<I> {

    public DefaultPageSearcher(Context context) {
        super(context);
    }

    @Override
    public boolean filter(String keyword, I entity) {
        String name = getShowName(entity);
        if (name == null) {
            return false;
        }
        if (name.indexOf(keyword) != -1
                || PinyinUtils.getPinYin(name).indexOf(keyword) != -1) {
            return true;
        }
        return false;
    }

    public abstract String getShowName(I entity);
}
