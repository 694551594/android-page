package cn.yhq.page.core;

/**
 * Created by Yanghuiqiang on 2017/2/9.
 */

public interface IPageSearcher<T, I> {
    void onSearch(PageAction pageAction, Page<I> page, PageManager.IPageDataCallback<I> callback);

    void onCancel();
}
