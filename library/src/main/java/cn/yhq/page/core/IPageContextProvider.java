package cn.yhq.page.core;

import android.view.View;

import cn.yhq.page.adapter.IPageAdapter;


/**
 * 分页组件提供接口
 * 
 * @author Yanghuiqiang 2014-9-4
 * 
 */
public interface IPageContextProvider<T, L, I> {

  /**
   * 获取分页列表界面的适配配器
   *
   * @return
   */
  IPageAdapter<L, I> getPageAdapter();

  /**
   * 数据请求器
   *
   * @return
   */
  IPageDataRequester<T, I> getPageDataRequester();

  /**
   * 数据处理器
   *
   * @return
   */
  IPageDataHandler<L> getPageDataHandler();

  /**
   * 获取分页解析器
   *
   * @return
   */
  PageManager.IPageDataParser<T, L, I> getPageDataParser();

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

  /**
   * pagecontext build监听，可以在这个方法里面自定义一些组件
   *
   * @param builder
     */
  void onPageContextBuild(PageContext.Builder<T, L, I> builder);

}
