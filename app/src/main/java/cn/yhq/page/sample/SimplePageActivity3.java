package cn.yhq.page.sample;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import cn.yhq.page.core.IPageAdapter;
import cn.yhq.page.core.IPageDataIntercept;
import cn.yhq.page.simple.SimplePageActivity;

/**
 * Created by Yanghuiqiang on 2016/10/12.
 */

public class SimplePageActivity3 extends SimplePageActivity<String> {
    private ListView mListView;
    private SimplePageAdapter mPageAdapter;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void onViewCreated(Bundle savedInstanceState) {
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

    @Override
    public void addPageDataIntercepts(List<IPageDataIntercept<String>> intercepts) {
        intercepts.add(new IPageDataIntercept<String>() {
            @Override
            public List<String> intercept(Chain<String> chain) throws Exception {
                List<String> data = chain.data();
                data.add(0, "拦截器增加的条目1");
                return chain.handle(data);
            }
        });
        intercepts.add(new IPageDataIntercept<String>() {
            @Override
            public List<String> intercept(Chain<String> chain) throws Exception {
                List<String> data = chain.data();
                data.add(0, "拦截器增加的条目2");
                return chain.handle(data);
            }
        });
    }
}
