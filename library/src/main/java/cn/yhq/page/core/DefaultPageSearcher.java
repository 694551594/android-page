package cn.yhq.page.core;

import android.content.Context;
import android.text.TextUtils;

import cn.yhq.utils.PinyinUtils;

/**
 * Created by Yanghuiqiang on 2017/2/9.
 */

public class DefaultPageSearcher<I> extends PageSearcher<I> {
    private String mKeyword;
    private IFilterName<I> filterName;

    public DefaultPageSearcher(Context context, IFilterName<I> filterName) {
        super(context);
        this.filterName = filterName;
    }

    @Override
    public boolean filter(String keyword, I entity) {
        String name = filterName.getFilterName(entity);
        if (TextUtils.isEmpty(name)) {
            return handleNullFilterName(keyword, entity);
        }
        if (name.indexOf(keyword) != -1) {
            mKeyword = keyword;
            return true;
        } else {
            String pinyin = PinyinUtils.getPinYin(name);
            if (pinyin.startsWith(keyword)) {
                mKeyword = name.substring(0, 1);
                return true;
            }
        }
        return false;
    }

    @Override
    public String getKeyword() {
        return mKeyword;
    }

    public boolean handleNullFilterName(String keyword, I entity) {
        return false;
    }
}
