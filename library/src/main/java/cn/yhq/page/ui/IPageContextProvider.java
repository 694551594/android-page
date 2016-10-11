package cn.yhq.page.ui;

import android.view.View;

import java.util.List;

import cn.yhq.page.core.IPageAdapter;
import cn.yhq.page.core.IPageDataIntercept;
import cn.yhq.page.core.IPageDataParser;
import cn.yhq.page.core.IPageRequester;
import cn.yhq.page.core.OnPageListener;
import cn.yhq.page.core.OnPullToRefreshProvider;

/**
 * 分页组件提供接口
 *
 * @author Yanghuiqiang 2014-9-4
 */
public interface IPageContextProvider<T, I> {

    /**
     * 获取分页列表界面的适配配器
     *
     * @return
     */
    IPageAdapter<I> getPageAdapter();

    /**
     * 数据请求器
     *
     * @return
     */
    IPageRequester<T, I> getPageRequester();

    /**
     * 数据处理器
     *
     * @return
     */
    void addPageDataIntercepts(List<IPageDataIntercept<I>> pageDataIntercepts);

    /**
     * 获取分页解析器
     *
     * @return
     */
    IPageDataParser<T, I> getPageDataParser();

    /**
     * 分页配置
     *
     * @param pageConfig
     */
    void onPageConfig(PageConfig pageConfig);

    /**
     * 获取pageview，比如listview，gridview，recyclerview等等
     *
     * @return
     */
    View getPageView();


    OnPullToRefreshProvider getOnPullToRefreshProvider();

    OnPageListener getOnPageListener();
}
