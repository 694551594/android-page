package cn.yhq.page.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.DataSetObserver;

/**
 * 数据集是List<T>的适配器
 * 
 * @author Yanghuiqiang 2015-1-24
 * 
 * @param <T>
 */
public abstract class PageListAdapter<T> extends ListAdapter<T> implements IPageListAdapter<T> {
  public final static String TAG = "PageListDataAdapter";

  protected OnListDataChangeListener onListDataListener;
  protected IPageDataParser<List<T>> mPageDataParser;

  public PageListAdapter(Context context, List<T> listData) {
    super(context, listData);
    init();
  }

  public PageListAdapter(Context context) {
    super(context);
    init();
  }

  private void init() {
    this.registerDataSetObserver(new PageDataSetObserver());
  }

  class PageDataSetObserver extends DataSetObserver {

    @Override
    public void onChanged() {
      super.onChanged();
      if (onListDataListener != null) {
        onListDataListener.onDataChange(getPageDataCount() != 0, getPageDataCount());
      }
    }

  }

  @Override
  public List<T> getPageListData() {
    return this.getListData();
  }

  public void setListDataChangeListener(OnListDataChangeListener onListDataListener) {
    this.onListDataListener = onListDataListener;
    onListDataListener.onDataChange(this.getPageDataCount() != 0, this.getPageDataCount());
  }

  @Override
  public int getCount() {
    return getPageDataCount();
  }

  @Override
  protected List<T> newInstance() {
    return new ArrayList<T>();
  }

  @Override
  public void addAll(List<T> data) {
    this.mListData.addAll(data);
  }

  @Deprecated
  public String getListItemDataId(T data) {
    return null;
  }

  /**
   * 清空列表数据
   * 
   */
  @Override
  public void clear() {
    mListData.clear();
  }

  @Override
  public boolean remove(int position) {
    T entity = mListData.get(position);
    return entity == mListData.remove(position);
  }

  @Override
  public int getPageDataCount() {
    if (mListData == null) {
      return 0;
    }
    return mListData.size();
  }

  @Override
  public T getItem(int position) {
    if (position >= mListData.size()) {
      return null;
    }
    return mListData.get(position);
  }

  @Override
  public void setPageDataParser(IPageDataParser<List<T>> pageDataParser) {
    this.mPageDataParser = pageDataParser;
  }

  @Override
  public String toJson() {
    if (mPageDataParser != null) {
      return mPageDataParser.toString(mListData);
    }
    return super.toJson();
  }

  @Override
  public List<T> fromJson(String json) {
    if (mPageDataParser != null) {
      return mPageDataParser.parser(json);
    }
    return super.fromJson(json);
  }


}
