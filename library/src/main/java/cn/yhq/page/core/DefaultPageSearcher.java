package cn.yhq.page.core;

import android.content.Context;

import cn.yhq.utils.PinyinUtils;

/**
 * Created by Yanghuiqiang on 2017/2/9.
 */

public abstract class DefaultPageSearcher<I> extends PageSearcher<I> {
    private String mKeyword;

    public DefaultPageSearcher(Context context) {
        super(context);
    }

    @Override
    public boolean filter(String keyword, I entity) {
        String name = getShowName(entity);
        if (name == null) {
            return false;
        }
        if (name.indexOf(keyword) != -1) {
            mKeyword = keyword;
            return true;
        } else if (PinyinUtils.getPinYin(name).startsWith(keyword)) {
            mKeyword = name.substring(0, 1);
            return true;
        }
        return false;
    }

    @Override
    public String getKeyword() {
        return mKeyword;
    }

    public abstract String getShowName(I entity);
}
