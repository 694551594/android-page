package cn.yhq.page.core;



/**
 * page数据处理器接口
 * 
 * @author Yanghuiqiang 2015-5-20
 * 
 * @param <T>
 */
public interface IPageDataHandler<T> {
  public T handle(PageRequestType pageRequestType, T pageData);
}
