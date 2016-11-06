package cn.yhq.page.sample;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import cn.yhq.page.core.IPageDataIntercept;
import cn.yhq.page.simple.SimpleListViewPageActivity;

/**
 * Created by Yanghuiqiang on 2016/10/12.
 */

public class SimplePageActivity1 extends SimpleListViewPageActivity<String> {

    @Override
    public void onViewCreated(Bundle savedInstanceState) {
        super.onViewCreated(savedInstanceState);
        this.setListAdapter(new SimplePageAdapter(this));
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
    public void addPageDataIntercept(List<IPageDataIntercept<String>> intercepts) {
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
