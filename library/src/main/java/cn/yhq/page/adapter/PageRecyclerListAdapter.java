package cn.yhq.page.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView.AdapterDataObserver;

public class PageRecyclerListAdapter<T> extends RecyclerListAdapter<T>
    implements
      IPageListAdapter<T> {
  public final static String TAG = "PageListDataAdapter";

  protected OnListDataChangeListener onListDataListener;
  protected IPageDataParser<List<T>> mPageDataParser;

  private void init() {
    this.registerAdapterDataObserver(new PageDataSetObserver());
  }

  class PageDataSetObserver extends AdapterDataObserver {

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
  public int getItemCount() {
    return getPageDataCount();
  }

  public PageRecyclerListAdapter(Context context) {
    super(context);
    init();
  }

  @Override
  protected List<T> newInstance() {
    return new ArrayList<T>();
  }

  @Override
  public void addAll(List<T> data) {
    this.mListData.addAll(data);
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
