package cn.yhq.page.adapter;

import android.content.Context;

import java.util.List;

import cn.yhq.adapter.core.ViewHolder;
import cn.yhq.adapter.list.ItemViewProvider2;


public class SimplePageStringListAdapter extends PageListAdapter<String> {

    public SimplePageStringListAdapter(Context context, List<String> listData) {
        super(context, listData);
        this.registerItemViewProvider();
    }

    public SimplePageStringListAdapter(Context context) {
        super(context);
        this.registerItemViewProvider();
    }

    private void registerItemViewProvider() {
        this.register(new ItemViewProvider2<String>() {
            @Override
            public int getItemViewLayoutId() {
                return android.R.layout.simple_list_item_1;
            }

            @Override
            public void setupView(ViewHolder viewHolder, int position, String entity) {
                viewHolder.bindTextData(android.R.id.text1, entity);
            }

            @Override
            public boolean isForProvider(int position, String entity) {
                return true;
            }
        });
    }

}
