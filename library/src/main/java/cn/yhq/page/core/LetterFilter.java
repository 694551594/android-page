package cn.yhq.page.core;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cn.yhq.utils.PinyinUtils;


/**
 * 按照拼音过滤类
 *
 * @author Yanghuiqiang 2015-5-27
 */
public class LetterFilter {

    public static <T> List<T> filter(List<T> list,
                                     String keyword,
                                     LetterNameGetter<T> listener) {
        if (list == null) {
            return new ArrayList<>();
        }
        keyword = keyword.toLowerCase(Locale.getDefault());
        List<T> filterDataList = new ArrayList<>();
        if (TextUtils.isEmpty(keyword)) {
            filterDataList = new ArrayList<>(list);
        } else {
            for (int i = 0; i < list.size(); i++) {
                String username = listener.getShowName(list.get(i));
                if (username.indexOf(keyword) != -1
                        || PinyinUtils.getPinYin(username).startsWith(keyword)) {
                    T entity = list.get(i);
                    filterDataList.add(entity);
                }
            }
        }
        return filterDataList;
    }

}
