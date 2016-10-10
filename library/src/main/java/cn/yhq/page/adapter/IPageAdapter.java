package cn.yhq.page.adapter;


import cn.yhq.page.core.ISavedState;

/**
 * 分页列表数据Adapter底层接口
 * 
 * @author Yanghuiqiang 2015-1-24
 * 
 */
public interface IPageAdapter<L, I> extends ISavedState {

  public void notifyDataSetChanged();

  public boolean remove(int position);

  public int getPageDataCount();

  public void setListDataChangeListener(OnListDataChangeListener onListDataListener);

  public void clear();

  public void addAll(L data);

  public L getPageListData();

  public void setPageDataParser(IPageDataParser<L> pageDataParser);
}
