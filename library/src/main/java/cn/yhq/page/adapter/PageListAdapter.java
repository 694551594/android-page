package cn.yhq.page.adapter;

import android.content.Context;

import java.util.List;

import cn.yhq.adapter.list.ListAdapter;
import cn.yhq.page.core.IPageAdapter;

/**
 * 数据集是List的适配器
 *
 * @param <T>
 * @author Yanghuiqiang 2015-1-24
 */
public class PageListAdapter<T> extends ListAdapter<T> implements IPageAdapter<T> {

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

}
