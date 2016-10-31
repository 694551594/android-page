package cn.yhq.page.simple;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import cn.yhq.page.R;
import cn.yhq.page.adapter.PageListAdapter;

/**
 * Created by Yanghuiqiang on 2016/10/21.
 */

public abstract class SimpleListViewPageDialog<I> extends SimplePageDialog<I> implements
        AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener {
    protected ListView mPageView;
    protected PageListAdapter<I> mPageAdapter;

    public SimpleListViewPageDialog(Context context) {
        super(context);
    }

    @Override
    public void onViewCreated() {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.page_listview, null, false);
        mPageView = (ListView) v.findViewById(R.id.listview);
        setListView(this.mPageView);
    }

    public void setListView(ListView listView) {
        this.mPageView = listView;
        this.mPageView.setOnItemClickListener(this);
        this.mPageView.setOnItemLongClickListener(this);
        if (this.mPageAdapter != null) {
            this.mPageView.setAdapter(mPageAdapter);
        }
    }

    public void setListAdapter(PageListAdapter<I> pageListAdapter) {
        this.mPageAdapter = pageListAdapter;
        if (this.mPageView != null) {
            this.mPageView.setAdapter(mPageAdapter);
        }
    }

    @Override
    public PageListAdapter<I> getPageAdapter() {
        return mPageAdapter;
    }

    @Override
    public ListView getPageView() {
        return mPageView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return false;
    }

}
