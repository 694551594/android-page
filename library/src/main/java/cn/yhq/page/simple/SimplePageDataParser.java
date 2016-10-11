package cn.yhq.page.simple;

import java.util.List;

import cn.yhq.page.core.IPageDataParser;

/**
 * Created by Yanghuiqiang on 2016/10/11.
 */

class SimplePageDataParser<I> implements IPageDataParser<List<I>, I> {

  @Override
  public List<I> getPageList(List<I> data, boolean isFromCache) {
    return data;
  }

  @Override
  public long getPageTotal(List<I> data, boolean isFromCache) {
    return data.size();
  }
}
