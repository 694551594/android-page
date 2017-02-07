package cn.yhq.page.core;

/**
 * Created by yanghuijuan on 2017/1/28.
 */

public interface LetterNameGetter<T> {
    String getShowName(T entity);
}
