package cn.yhq.page.sample;

import android.content.Context;

import cn.yhq.adapter.core.ViewHolder;
import cn.yhq.adapter.list.ItemViewProvider2;
import cn.yhq.page.adapter.PageListAdapter;

/**
 * Created by Yanghuiqiang on 2016/10/12.
 */

public class SimplePageAdapter extends PageListAdapter<String> {

    public SimplePageAdapter(Context context) {
        super(context);
        this.register(new ItemViewProvider2<String>() {
            @Override
            public int getItemViewLayoutId() {
                return android.R.layout.simple_list_item_1;
            }

            @Override
            public void setupView(ViewHolder viewHolder, int position, String entity) {
                viewHolder.setText(android.R.id.text1, entity);
            }

            @Override
            public boolean isForProvider(int position, String entity) {
                return true;
            }
        });
    }
}
