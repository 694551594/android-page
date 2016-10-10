package cn.yhq.page.local;

import java.util.List;

import cn.developer.sdk.page2.list.IPageListDataParser;
import cn.developer.sdk.page2.ui.PageListDataActivity;

/**
 * 载入本地的数据的分页列表显示
 * 
 * @author Yanghuiqiang 2015-5-27
 * 
 * @param <I>
 */
public abstract class LocalPageListDataActivity<I> extends PageListDataActivity<List<I>, I> {

  @Override
  public IPageListDataParser<List<I>, I> getPageDataParser() {
    return new IPageListDataParser<List<I>, I>() {

      @Override
      public List<I> getPageList(List<I> data, boolean isFromCache) {
        return data;
      }

      @Override
      public long getPageTotal(List<I> data, boolean isFromCache) {
        return data.size();
      }
    };
  }



}
