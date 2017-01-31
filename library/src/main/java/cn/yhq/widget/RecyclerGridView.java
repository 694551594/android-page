package cn.yhq.widget;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.util.AttributeSet;

/**
 * Created by 杨慧强 on 2016/2/23.
 */
public class RecyclerGridView extends BaseRecyclerView {
    public RecyclerGridView(Context context) {
        this(context, null);
    }

    public RecyclerGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecyclerGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public int getFirstVisiblePosition() {
        int position = ((GridLayoutManager) getLayoutManager()).findFirstVisibleItemPosition();
        return position;
    }

    @Override
    public int getLastVisiblePosition() {
        int position = ((GridLayoutManager) getLayoutManager()).findLastVisibleItemPosition();
        return position;
    }

    public void setup(int spanCount, int orientation, boolean reverseLayout) {
        GridLayoutManager layoutManager =
                new GridLayoutManager(getContext(), spanCount, orientation, reverseLayout);
        this.setLayoutManager(layoutManager);
        // 设置分割线
    }
}
