package cn.yhq.page.adapter;

import android.content.Context;

import java.util.List;

import cn.yhq.adapter.expand.BaseExpandableListAdapter;
import cn.yhq.page.core.DataAppendMode;
import cn.yhq.page.core.IPageAdapter;
import cn.yhq.page.core.SearchHelper;

public abstract class PageExpandableListAdapter<G, C> extends BaseExpandableListAdapter<G, C>
        implements
        IPageAdapter<G> {

    private String mKeyword;

    public PageExpandableListAdapter(Context context, List<G> listData) {
        super(context, listData);
    }

    public PageExpandableListAdapter(Context context) {
        super(context);
    }

    @Override
    public List<G> getPageListData() {
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
        return this.getGroupCount();
    }

    @Override
    public void appendBefore(List<G> data) {
        this.mListData.addAll(0, data);
    }

    @Override
    public void appendAfter(List<G> data) {
        this.mListData.addAll(data);
    }

    @Override
    public DataAppendMode getDataAppendMode() {
        return DataAppendMode.MODE_AFTER;
    }

    @Override
    public void setKeyword(String keyword) {
        this.mKeyword = keyword;
    }

    public CharSequence highlight(String text) {
        return SearchHelper.match(text, mKeyword);
    }
}
