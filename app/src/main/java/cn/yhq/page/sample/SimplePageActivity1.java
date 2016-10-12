package cn.yhq.page.sample;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import cn.yhq.page.core.IPageAdapter;
import cn.yhq.page.simple.SimplePageDataActivity;

/**
 * Created by Yanghuiqiang on 2016/10/12.
 */

public class SimplePageActivity1 extends SimplePageDataActivity<String> {
    private ListView mListView;
    private SimplePageAdapter mPageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        mListView = (ListView) this.findViewById(R.id.list_view);
        mPageAdapter = new SimplePageAdapter(this);
        mListView.setAdapter(mPageAdapter);
    }

    @Override
    public View getPageView() {
        return mListView;
    }

    @Override
    public IPageAdapter<String> getPageAdapter() {
        return mPageAdapter;
    }

    // 如果是非耗时操作，则可以直接返回要适配的数据
    @Override
    public List<String> getPageData() {
        List<String> data = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            data.add("条目" + i);
        }
        return data;
    }

}
