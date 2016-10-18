package cn.yhq.widget;


import android.content.Context;
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
        int headerCount = this.getHeaderViewsCount();
        int firstVisiblePos = this.getFirstVisiblePosition();
        int newCursorPosition =
                getPositionInNewCursor(newDataSize + this.getAdapter().getCount(), firstVisiblePos);
        int offsetY = getOffsetY(firstVisiblePos, newCursorPosition);

        this.setSelectionFromTop(newCursorPosition + headerCount, offsetY);
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
        if (!isHaveMoreData) {
            mHintTextView.setVisibility(View.VISIBLE);
            mLoadingView.setVisibility(View.GONE);
            setAutoRefreshEnable(false);
        } else {
            mHintTextView.setVisibility(View.GONE);
            mLoadingView.setVisibility(View.VISIBLE );
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

}
