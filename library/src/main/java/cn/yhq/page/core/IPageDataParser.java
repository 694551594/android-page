package cn.yhq.page.core;

import java.util.List;

/**
 * Created by Yanghuiqiang on 2016/10/11.
 */

public interface IPageDataParser<T, I> {

    /**
     * 从请求数据中获取数据的列表
     *
     * @param data
     * @param isFromCache
     * @return
     */
    List<I> getPageList(T data, boolean isFromCache);

    /**
     * 从请求中获取总数据的总数
     *
     * @param data
     * @param isFromCache
     * @return
     */
    long getPageTotal(T data, boolean isFromCache);

}
