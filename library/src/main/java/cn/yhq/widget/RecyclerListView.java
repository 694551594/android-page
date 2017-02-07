package cn.yhq.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import cn.yhq.dialog.utils.DisplayUtils;

/**
 * listview，可以设置方向
 * <p>
 * Created by 杨慧强 on 2016/2/22.
 */
public class RecyclerListView extends BaseRecyclerView {

    public RecyclerListView(Context context) {
        super(context);
        setOrientation(LinearLayoutManager.VERTICAL);
    }

    public RecyclerListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayoutManager.VERTICAL);
    }

    public RecyclerListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setOrientation(LinearLayoutManager.VERTICAL);
    }

    @Override
    public int getFirstVisiblePosition() {
        return ((LinearLayoutManager) this.getLayoutManager()).findFirstVisibleItemPosition();
    }

    @Override
    public int getLastVisiblePosition() {
        return ((LinearLayoutManager) this.getLayoutManager()).findLastVisibleItemPosition();
    }

    public void setOrientation(int orientation) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(orientation);
        this.setLayoutManager(layoutManager);
        this.setDividerDecoration(new DividerDecoration(getContext()));
    }

    private class DividerDecoration extends RecyclerView.ItemDecoration {
        private final int[] ATTRS = new int[]{
                android.R.attr.listDivider
        };

        private Drawable mDivider;
        private int mDividerHeight;

        public DividerDecoration(Context context) {
            final TypedArray a = context.obtainStyledAttributes(ATTRS);
            mDivider = a.getDrawable(0);
            a.recycle();

            mDividerHeight = 1;
        }

        @Override
        public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
            if (mDivider == null) {
                return;
            }
            final int childCount = parent.getChildCount();
            final int width = parent.getWidth();
            for (int childViewIndex = 0; childViewIndex < childCount; childViewIndex++) {
                final View view = parent.getChildAt(childViewIndex);
                int top = (int) ViewCompat.getY(view) + view.getHeight();
                int offset = DisplayUtils.dp2Px(getContext(), 0);
                mDivider.setBounds(0 + offset, top, width - offset, top + mDividerHeight);
                mDivider.draw(c);
            }
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                   RecyclerView.State state) {
            outRect.bottom = 0;
        }

        public void setDivider(Drawable divider) {
            if (divider != null) {
                mDividerHeight = divider.getIntrinsicHeight();
            } else {
                mDividerHeight = 0;
            }
            mDivider = divider;
            invalidateItemDecorations();
        }

        public void setDividerHeight(int dividerHeight) {
            mDividerHeight = dividerHeight;
            invalidateItemDecorations();
        }
    }

}
