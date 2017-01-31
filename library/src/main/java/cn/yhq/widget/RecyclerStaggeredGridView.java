package cn.yhq.widget;

import android.content.Context;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;

/**
 * Created by yanghuijuan on 2017/1/31.
 */

public class RecyclerStaggeredGridView extends BaseRecyclerView {

    public RecyclerStaggeredGridView(Context context) {
        super(context);
    }

    public RecyclerStaggeredGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RecyclerStaggeredGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public int getLastVisiblePosition() {
        StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) getLayoutManager();
        int[] lastPositions =
                layoutManager.findFirstVisibleItemPositions(new int[layoutManager.getSpanCount()]);
        int position = getMinPositions(lastPositions);
        return position;
    }

    @Override
    public int getFirstVisiblePosition() {
        StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) getLayoutManager();
        int[] lastPositions =
                layoutManager.findLastVisibleItemPositions(new int[layoutManager.getSpanCount()]);
        int position = getMaxPosition(lastPositions);
        return position;
    }

    /**
     * 获得最大的位置
     *
     * @param positions
     * @return
     */
    private int getMaxPosition(int[] positions) {
        int size = positions.length;
        int maxPosition = Integer.MIN_VALUE;
        for (int i = 0; i < size; i++) {
            maxPosition = Math.max(maxPosition, positions[i]);
        }
        return maxPosition;
    }


    /**
     * 获得当前展示最小的position
     *
     * @param positions
     * @return
     */
    private int getMinPositions(int[] positions) {
        int size = positions.length;
        int minPosition = Integer.MAX_VALUE;
        for (int i = 0; i < size; i++) {
            minPosition = Math.min(minPosition, positions[i]);
        }
        return minPosition;
    }

    public void setup(int spanCount, int orientation) {
        this.setLayoutManager(new StaggeredGridLayoutManager(spanCount, orientation));
    }
}
