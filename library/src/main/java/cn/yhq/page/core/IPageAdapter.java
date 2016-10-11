package cn.yhq.page.core;

import java.util.List;

/**
 * Created by Yanghuiqiang on 2016/10/11.
 */

public interface IPageAdapter<I> {

  int getPageDataCount();

  void clear();

  void addAll(List<I> data);

  List<I> getPageListData();

  void notifyDataSetChanged();

}
