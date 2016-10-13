package cn.yhq.page.core;

import java.util.List;

/**
 * Created by Yanghuiqiang on 2016/10/11.
 *
 * 主要负责解析从服务器请求到的数据，获取要适配到AbsListView或者RecyclerView这些分页列表上面的数据集以及列表数据的总个数。
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
