package cn.yhq.page.core;

import java.util.List;

/**
 * Created by Yanghuiqiang on 2017/2/9.
 */

public interface IPageSearcher<I> {
    void onSearch(PageAction pageAction, List<I> pageData, String keyword, PageManager.IPageDataCallback<I> callback);

    void onCancel();

    String getKeyword();
}
