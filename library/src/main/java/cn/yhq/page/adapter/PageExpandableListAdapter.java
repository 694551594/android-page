package cn.yhq.page.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.DataSetObserver;
import cn.developer.sdk.adapter.expand.BaseExpandableListAdapter;

public abstract class PageExpandableListAdapter<G, C> extends BaseExpandableListAdapter<G, C>
    implements
      IPageListAdapter<G> {
  protected OnListDataChangeListener onListDataListener;
  protected IPageDataParser<List<G>> mPageDataParser;

  public PageExpandableListAdapter(Context context, List<G> listData) {
    super(context, listData);
    init();
  }

  public PageExpandableListAdapter(Context context) {
    super(context);
    init();
  }

  public void setListDataChangeListener(OnListDataChangeListener onListDataListener) {
    this.onListDataListener = onListDataListener;
    onListDataListener.onDataChange(this.getPageDataCount() != 0, this.getPageDataCount());
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

  public String getListItemDataId(G data) {
    return null;
  }

  @Override
  public int getPageDataCount() {
    return getGroupCount();
  }

  @Override
  public int getRealChildrenCount(int i) {
    return getChildrenCount(this.getGroup(i));
  }

  public abstract int getChildrenCount(G group);

  public abstract C getChild(G group, int i);

  @Override
  public int getGroupCount() {
    return mListData.size();
  }

  @Override
  public G getGroup(int groupPosition) {
    if (mListData.isEmpty()) {
      return null;
    }
    return mListData.get(groupPosition);
  }

  @Override
  public List<G> getPageListData() {
    return this.mListData;
  }

  @Override
  public C getChild(int i, int j) {
    return getChild(getGroup(i), j);
  }

  @Override
  public boolean remove(int position) {
    G entity = mListData.get(position);
    return entity == mListData.remove(position);
  }

  @Override
  public void addAll(List<G> data) {
    this.mListData.addAll(data);
  }

  @Override
  public void clear() {
    this.mListData.clear();
  }

  @Override
  protected List<G> newInstance() {
    return new ArrayList<G>();
  }

  @Override
  public void setPageDataParser(IPageDataParser<List<G>> pageDataParser) {
    this.mPageDataParser = pageDataParser;
  }

  @Override
  public String toJson() {
    // 否则只能用json了
    if (mPageDataParser != null) {
      return mPageDataParser.toString(mListData);
    }
    return super.toJson();
  }

  @Override
  public List<G> fromJson(String json) {
    if (mPageDataParser != null) {
      return mPageDataParser.parser(json);
    }
    return super.fromJson(json);
  }

}
