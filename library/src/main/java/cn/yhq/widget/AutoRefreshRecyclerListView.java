package cn.yhq.widget;


import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import cn.yhq.page.R;
import cn.yhq.utils.DisplayUtils;
import cn.yhq.widget.xrecyclerview.RecyclerListView;

/**
 * 滚动到最顶部可以自动刷新数据的Listview
 *
 * @author Yanghuiqiang 2015-3-17
 */
public class AutoRefreshRecyclerListView extends RecyclerListView {
    public final static String TAG = "AutoRefreshListView";

    public AutoRefreshRecyclerListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    public AutoRefreshRecyclerListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public AutoRefreshRecyclerListView(Context context) {
        super(context);
        initView();
    }

    private ProgressBar mLoadingView;
    private TextView mHintTextView;
    private View mHeaderView;
    private OnAutoRefreshListener mOnAutoRefreshListener;
    private boolean isRefreshing;
    private boolean isEnable = true;
    private boolean isHaveMoreData;
    private LinearLayout mFooterLayout;

    public interface OnAutoRefreshListener {
        void autoRefresh(AutoRefreshRecyclerListView view);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            default:
                if (isEnable && !isRefreshing && getFirstVisiblePosition() == 0) {
                    refresh();
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    private void initView() {
        mHeaderView = LayoutInflater.from(this.getContext()).inflate(R.layout.progressbar, null);
        mHintTextView = (TextView) mHeaderView.findViewById(R.id.no_more_textview);
        mLoadingView = (ProgressBar) mHeaderView.findViewById(R.id.progressbar);
        LinearLayout.LayoutParams layoutParams =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        DisplayUtils.dp2Px(getContext(), 48));
        layoutParams.gravity = Gravity.CENTER;
        mFooterLayout = new LinearLayout(getContext());
        mFooterLayout.setLayoutParams(layoutParams);
        LinearLayout.LayoutParams footerParams =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
        footerParams.gravity = Gravity.CENTER;
        mFooterLayout.addView(mHeaderView, footerParams);

        this.addHeaderView(mFooterLayout);
        this.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (isEnable && !isRefreshing && newState == RecyclerView.SCROLL_STATE_IDLE
                        && getFirstVisiblePosition() == 0) {
                    refresh();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

            }
        });
    }

    public void refreshComplete(int newDataSize) {
        int headerCount = this.getHeaderViewsCount();
        int firstVisiblePos = this.getFirstVisiblePosition();
        int newCursorPosition =
                getPositionInNewCursor(newDataSize + this.getAdapter().getItemCount(), firstVisiblePos);
        int offsetY = getOffsetY(firstVisiblePos, newCursorPosition);

        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) this.getLayoutManager();
        linearLayoutManager.scrollToPositionWithOffset(newCursorPosition + headerCount, offsetY);
        isRefreshing = false;
    }

    private int getPositionInNewCursor(int newCursorCount, int firstVisiblePos) {
        if (firstVisiblePos == 0) {
            firstVisiblePos += 1;
        }

        int headerCount = this.getHeaderViewsCount();
        int newCursorPos =
                newCursorCount - this.getAdapter().getItemCount() + firstVisiblePos - headerCount;

        return newCursorPos;
    }

    private int getOffsetY(int firstVisiblePos, int newCursorPosition) {
        int y;

        View firstVisibleItem = null;
        if (firstVisiblePos == 0) {
            firstVisibleItem = this.getChildAt(1);
        } else {
            firstVisibleItem = this.getChildAt(0);
        }

        if (firstVisibleItem == null) {
            return 0;
        }

        y = firstVisibleItem.getTop();

        return y;
    }

    private void refresh() {
        isRefreshing = true;
        if (mOnAutoRefreshListener != null) {
            mOnAutoRefreshListener.autoRefresh(this);
        }
    }

    public void setHaveMoreData(boolean isHaveMoreData) {
        this.isHaveMoreData = isHaveMoreData;
        if (!isHaveMoreData) {
            mHintTextView.setVisibility(View.VISIBLE);
            mLoadingView.setVisibility(View.GONE);
            setAutoRefreshEnable(false);
        } else {
            mHintTextView.setVisibility(View.GONE);
            mLoadingView.setVisibility(View.VISIBLE);
            setAutoRefreshEnable(true);
        }
    }

    public boolean isAutoRefreshEnable() {
        return isEnable;
    }

    public void setAutoRefreshEnable(boolean enable) {
        isEnable = enable;
    }

    public void setOnAutoRefreshListener(OnAutoRefreshListener onAutoRefreshListener) {
        this.mOnAutoRefreshListener = onAutoRefreshListener;
    }

    static class SavedState extends BaseSavedState {

        public SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        public SavedState(Parcel in) {
            super(in);
            boolean[] b = new boolean[3];
            in.readBooleanArray(b);
            this.isHaveMoreData = b[0];
            this.isRefreshing = b[1];
            this.isEnable = b[2];
        }

        boolean isHaveMoreData;
        boolean isRefreshing;
        boolean isEnable;

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeBooleanArray(new boolean[]{isHaveMoreData, isRefreshing, isEnable});
        }

        public static final Creator<SavedState> CREATOR =
                new Creator<SavedState>() {
                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }

                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };

    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.isHaveMoreData = this.isHaveMoreData;
        ss.isRefreshing = this.isRefreshing;
        ss.isEnable = this.isEnable;
        return ss;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof SavedState) {
            SavedState ss = (SavedState) state;
            super.onRestoreInstanceState(ss.getSuperState());
            this.setHaveMoreData(ss.isHaveMoreData);
            this.setAutoRefreshEnable(ss.isEnable);
            this.isRefreshing = ss.isRefreshing;
        } else {
            super.onRestoreInstanceState(state);
        }

    }

}
