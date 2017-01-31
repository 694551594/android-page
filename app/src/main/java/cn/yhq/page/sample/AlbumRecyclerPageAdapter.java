package cn.yhq.page.sample;

import android.content.Context;

import cn.yhq.adapter.recycler.ItemViewProvider2;
import cn.yhq.adapter.recycler.ViewHolder;
import cn.yhq.page.adapter.PageRecyclerListAdapter;
import cn.yhq.page.sample.entity.Tracks;

/**
 * Created by Yanghuiqiang on 2016/10/12.
 */

public class AlbumRecyclerPageAdapter extends PageRecyclerListAdapter<Tracks> {

    public AlbumRecyclerPageAdapter(Context context) {
        super(context);
        this.register(new ItemViewProvider2<Tracks>() {

            @Override
            public boolean isForProvider(int position, Tracks entity) {
                return true;
            }

            @Override
            public void onBindViewHolder(ViewHolder viewHolder, int position, Tracks entity) {
                super.onBindViewHolder(viewHolder, position, entity);
                viewHolder.setText(android.R.id.text1, entity.getTitle());
            }

            @Override
            public int getItemViewLayoutId() {
                return android.R.layout.simple_list_item_1;
            }
        });
    }

}
