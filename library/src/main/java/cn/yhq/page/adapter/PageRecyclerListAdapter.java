package cn.yhq.page.adapter;

import android.content.Context;

import java.util.List;

import cn.yhq.adapter.recycler.RecyclerListAdapter;
import cn.yhq.page.core.DataAppendMode;
import cn.yhq.page.core.IPageAdapter;
import cn.yhq.page.core.SearchHelper;

public class PageRecyclerListAdapter<T> extends RecyclerListAdapter<T> implements IPageAdapter<T> {
    private List<String> mKeywords;
    private List<T> mCheckedList;
    private List<T> mDisabledList;

    public PageRecyclerListAdapter(Context context) {
        super(context);
    }

    @Override
    public List<T> getPageListData() {
        return this.getListData();
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
        return this.getItemCount();
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
    public DataAppendMode getDataAppendMode() {
        return DataAppendMode.MODE_AFTER;
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

    @Override
    public void setDisabledListData(List<T> data) {
        this.mDisabledList = data;
    }

    public boolean isChecked(int position) {
        return mCheckedList.contains(this.getItem(position)) || isDisabled(position);
    }

    public boolean isDisabled(int position) {
        return mDisabledList.contains(this.getItem(position));
    }

}
