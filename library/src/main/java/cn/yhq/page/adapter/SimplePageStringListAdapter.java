package cn.yhq.page.adapter;

import android.content.Context;

import java.util.List;

import cn.yhq.adapter.core.ViewHolder;
import cn.yhq.adapter.list.ItemViewProvider2;
import cn.yhq.page.core.SearchHelper;


public class SimplePageStringListAdapter extends PageListAdapter<String> {
    private List<String> mKeywords;

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
                viewHolder.setText(android.R.id.text1, entity);
            }

            @Override
            public boolean isForProvider(int position, String entity) {
                return true;
            }
        });
    }

    @Override
    public void setHighlightKeywords(List<String> keywords) {
        this.mKeywords = keywords;
    }

    public CharSequence highlight(String text) {
        return SearchHelper.match(text, mKeywords);
    }

}
