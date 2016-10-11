package cn.yhq.page.adapter;

import android.content.Context;

import java.util.List;

import cn.yhq.adapter.list.ListAdapter;
import cn.yhq.page.core.IPageAdapter;

/**
 * 数据集是List<T>的适配器
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
    public void addAll(List<T> data) {
        this.mListData.addAll(data);
    }

    @Override
    public int getPageDataCount() {
        return this.getCount();
    }

}
