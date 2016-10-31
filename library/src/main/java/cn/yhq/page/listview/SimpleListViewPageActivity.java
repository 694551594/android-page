package cn.yhq.page.listview;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import cn.yhq.page.R;
import cn.yhq.page.adapter.PageListAdapter;
import cn.yhq.page.simple.SimplePageActivity;

public abstract class SimpleListViewPageActivity<I> extends SimplePageActivity<I> implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    private ListView mPageView;
    private PageListAdapter<I> mPageAdapter;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.page_listview;
    }

    @Override
    protected void onViewCreated(Bundle savedInstanceState) {
        super.onViewCreated(savedInstanceState);
        this.mPageView = this.getView(R.id.listview);
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
