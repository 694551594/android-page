package cn.yhq.page.sample;

import android.content.Context;

import cn.yhq.adapter.core.ViewHolder;
import cn.yhq.adapter.list.ItemViewProvider2;
import cn.yhq.page.adapter.PageListAdapter;
import cn.yhq.page.core.DataAppendMode;
import cn.yhq.page.sample.entity.Tracks;

/**
 * Created by Yanghuiqiang on 2016/10/12.
 */

public class AlbumPageAdapter extends PageListAdapter<Tracks> {

    public AlbumPageAdapter(Context context) {
        super(context);
        this.register(new ItemViewProvider2<Tracks>() {
            @Override
            public int getItemViewLayoutId() {
                return android.R.layout.simple_list_item_1;
            }

            @Override
            public void setupView(ViewHolder viewHolder, int position, Tracks entity) {
                viewHolder.setText(android.R.id.text1, highlight(entity.getTitle()));
            }

            @Override
            public boolean isForProvider(int position, Tracks entity) {
                return true;
            }
        });
    }

    @Override
    public DataAppendMode getDataAppendMode() {
        return DataAppendMode.MODE_BEFORE;
    }
}
