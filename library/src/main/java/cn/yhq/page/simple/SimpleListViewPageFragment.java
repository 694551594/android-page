package cn.yhq.page.simple;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import cn.yhq.page.R;
import cn.yhq.page.adapter.PageListAdapter;
import cn.yhq.page.ui.PageDataFragment;

public abstract class SimpleListViewPageFragment<I> extends PageDataFragment<List<I>, I> implements
        AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener {
    protected ListView mPageView;
    protected PageListAdapter<I> mPageAdapter;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.page_listview;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.mPageView = this.getView(view, R.id.listview);
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
