package cn.yhq.page.core;

import android.content.Context;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.yhq.utils.PinyinUtils;

/**
 * Created by Yanghuiqiang on 2017/2/9.
 */

public class DefaultPageSearcher<I> extends PageSearcher<I> {
    private IFilterName<I> filterName;
    private Map<String, Pattern> patterns = new HashMap<>();

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
        boolean isFind = false;
        Pattern pattern = patterns.get(keyword);
        if (pattern == null) {
            pattern = SearchHelper.buildPattern(keyword);
            patterns.put(keyword, pattern);
        }
        Matcher matcher = pattern.matcher(name);
        while (matcher.find()) {
            isFind = true;
            addHighlightKeyword(name.substring(matcher.start(), matcher.end()));
        }
        if (!isFind) {
            String firstName = name.substring(0, 1);
            String pinyin = PinyinUtils.getPinYin(name);
            if (Character.toString(firstName.charAt(0)).matches("[\\u4E00-\\u9FA5]+")) {
                keyword = keyword.toLowerCase(Locale.getDefault());
                pinyin = pinyin.toLowerCase(Locale.getDefault());
                if (pinyin.startsWith(keyword)) {
                    isFind = true;
                    addHighlightKeyword(firstName);
                }
            } else {
                if (name.startsWith(keyword)) {
                    isFind = true;
                    addHighlightKeyword(keyword);
                }
            }
        }
        return isFind;
    }

}
