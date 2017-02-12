package cn.yhq.page.adapter;

import android.content.Context;

import java.util.List;

import cn.yhq.adapter.list.ListAdapter;
import cn.yhq.page.core.DataAppendMode;
import cn.yhq.page.core.IPageAdapter;
import cn.yhq.page.core.SearchHelper;

/**
 * 数据集是List的适配器
 *
 * @param <T>
 * @author Yanghuiqiang 2015-1-24
 */
public class PageListAdapter<T> extends ListAdapter<T> implements IPageAdapter<T> {
    private List<String> mKeywords;
    private List<T> mCheckedList;

    public PageListAdapter(Context context, List<T> listData) {
        super(context, listData);
    }

    public PageListAdapter(Context context) {
        super(context);
    }

    @Override
    public List<T> getPageListData() {
        return this.getListData();
    }

    @Override
    public DataAppendMode getDataAppendMode() {
        return DataAppendMode.MODE_AFTER;
    }

    /**
     * 清空列表数据
     */
    @Override
    public void clear() {
        mListData.clear();
    }

    @Override
    public int getPageDataCount() {
        return this.getCount();
    }

    @Override
    public void appendBefore(List<T> data) {
        this.mListData.addAll(0, data);
    }

    @Override
    public void appendAfter(List<T> data) {
        this.mListData.addAll(data);
    }

    @Override
    public void setHighlightKeywords(List<String> keywords) {
        this.mKeywords = keywords;
    }

    public CharSequence highlight(String text) {
        return SearchHelper.match(text, mKeywords);
    }

    @Override
    public void setCheckedListData(List<T> data) {
        this.mCheckedList = data;
    }

    public boolean isChecked(int position) {
        return mCheckedList.contains(this.getItem(position));
    }
}
