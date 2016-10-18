package cn.yhq.page.sample;

import android.content.Context;

import cn.yhq.adapter.core.ViewHolder;
import cn.yhq.adapter.list.ItemViewProvider2;
import cn.yhq.page.adapter.PageListAdapter;
import cn.yhq.page.sample.entity.Tracks;

/**
 * Created by Yanghuiqiang on 2016/10/12.
 */

public class AlbumPageAdapter extends PageListAdapter<Tracks> {
    private DataAppendMode dataAppendType;

    public AlbumPageAdapter(Context context) {
        this(context, DataAppendMode.MODE_AFTER);
    }

    public AlbumPageAdapter(Context context, DataAppendMode dataAppendType) {
        super(context);
        this.dataAppendType = dataAppendType;
        this.register(new ItemViewProvider2<Tracks>() {
            @Override
            public int getItemViewLayoutId() {
                return android.R.layout.simple_list_item_1;
            }

            @Override
            public void setupView(ViewHolder viewHolder, int position, Tracks entity) {
                viewHolder.bindTextData(android.R.id.text1, entity.getTitle());
            }

            @Override
            public boolean isForProvider(int position, Tracks entity) {
                return true;
            }
        });
    }

    @Override
    public DataAppendMode getDataAppendMode() {
        return dataAppendType;
    }
}
