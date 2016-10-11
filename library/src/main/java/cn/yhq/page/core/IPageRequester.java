package cn.yhq.page.core;

/**
 * 数据请求器，负责数据的请求与回调
 *
 * Created by Yanghuiqiang on 2016/10/11.
 */

public interface IPageRequester<T, I> {

  void onRequest(PageAction pageAction, Page<I> page, IPageResponse<T> pageResponse);

  void onCancel();

}
