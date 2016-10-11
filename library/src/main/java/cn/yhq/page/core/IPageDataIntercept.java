package cn.yhq.page.core;

import java.util.List;

/**
 * 数据处理拦截器
 *
 * Created by Yanghuiqiang on 2016/10/11.
 */

public interface IPageDataIntercept<I> {
  List<I> intercept(Chain<List<I>> chain) throws Exception;

  interface Chain<I> {
    List<I> data();

    List<I> handler(List<I> data) throws Exception;
  }
}
