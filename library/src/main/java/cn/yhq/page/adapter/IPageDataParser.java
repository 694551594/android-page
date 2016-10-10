package cn.yhq.page.adapter;


public interface IPageDataParser<T> {
  public String toString(T entity);

  public T parser(String str);
}
