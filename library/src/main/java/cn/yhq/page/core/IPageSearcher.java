package cn.yhq.page.core;

/**
 * Created by Yanghuiqiang on 2017/2/9.
 */

public interface IPageSearcher<T, I> {
    void onSearch(T pageData, String keyword, Page<I> page, IPageResponse<T> pageResponse);

    void onCancel();
}
