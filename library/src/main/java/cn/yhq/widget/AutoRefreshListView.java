package cn.yhq.widget;


import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import cn.yhq.page.R;

/**
 * 滚动到最顶部可以自动刷新数据的Listview
 *
 * @author Yanghuiqiang 2015-3-17
 */
public class AutoRefreshListView extends ListView {
    public final static String TAG = "AutoRefreshListView";

    public AutoRefreshListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    public AutoRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public AutoRefreshListView(Context context) {
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

    private OnScrollListener mOnScrollListener;

    public interface OnAutoRefreshListener {
        void autoRefresh(AutoRefreshListView view);
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

    @Override
    public void setOnScrollListener(OnScrollListener l) {
        this.mOnScrollListener = l;
    }

    private void initView() {
        mHeaderView = LayoutInflater.from(this.getContext()).inflate(R.layout.progressbar, null);
        mHintTextView = (TextView) mHeaderView.findViewById(R.id.no_more_textview);
        mLoadingView = (ProgressBar) mHeaderView.findViewById(R.id.progressbar);
        this.addHeaderView(mHeaderView);
        super.setOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (isEnable && !isRefreshing && scrollState == OnScrollListener.SCROLL_STATE_IDLE
                        && getFirstVisiblePosition() == 0) {
                    refresh();
                }

                if (mOnScrollListener != null) {
                    mOnScrollListener.onScrollStateChanged(view, scrollState);
                }

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                                 int totalItemCount) {
                if (firstVisibleItem == 0 && getFirstVisiblePosition() == 0) {

                }

                if (mOnScrollListener != null) {
                    mOnScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
                }
            }
        });
    }

    public void refreshComplete(int newDataSize) {
        final int headerCount = this.getHeaderViewsCount();
        final int firstVisiblePos = this.getFirstVisiblePosition();
        final int newCursorPosition =
                getPositionInNewCursor(newDataSize + this.getAdapter().getCount(), firstVisiblePos);
        final int offsetY = getOffsetY(firstVisiblePos);
        this.post(new Runnable() {
            @Override
            public void run() {
                setSelectionFromTop(newCursorPosition + headerCount, offsetY);
            }
        });
        isRefreshing = false;
    }

    private int getPositionInNewCursor(int newCursorCount, int firstVisiblePos) {
        if (firstVisiblePos == 0) {
            firstVisiblePos += 1;
        }

        int headerCount = this.getHeaderViewsCount();
        int newCursorPos =
                newCursorCount - this.getAdapter().getCount() + firstVisiblePos - headerCount;

        return newCursorPos;
    }

    private int getOffsetY(int firstVisiblePos) {
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

    public void scrollToBottom() {
        this.post(new Runnable() {
            @Override
            public void run() {
                setSelection(getAdapter().getCount() - 1);
            }
        });
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

        public static final Parcelable.Creator<SavedState> CREATOR =
                new Parcelable.Creator<SavedState>() {
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
