package cn.yhq.page.core;

import java.util.List;

/**
 * Created by Yanghuiqiang on 2017/2/8.
 */

public interface IPageContext<T, I> {
    int getPageSize();

    // 分页请求接口
    IPageRequester<T, I> getPageRequester();

    // 分页解析器
    IPageDataParser<T, I> getPageParser();

    // 拦截器
    List<IPageDataIntercept<I>> getPageDataIntercepts();

    // 上拉加载下拉刷新
    OnPullToRefreshProvider getOnPullToRefreshProvider();

    // UI适配器
    IPageAdapter<I> getPageAdapter();

}
