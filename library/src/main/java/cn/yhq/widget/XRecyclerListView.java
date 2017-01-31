package cn.yhq.widget;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.markmao.pulltorefresh.widget.XFooterView;
import com.markmao.pulltorefresh.widget.XHeaderView;

import cn.yhq.adapter.recycler.RecyclerAdapter;
import cn.yhq.page.R;

/**
 * Created by 杨慧强 on 2016/2/23.
 */
public class XRecyclerListView extends RecyclerListView {

    public XRecyclerListView(Context context) {
        this(context, null);
    }

    public XRecyclerListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XRecyclerListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initWithContext(context);
    }

    private final static int SCROLL_BACK_HEADER = 0;
    private final static int SCROLL_BACK_FOOTER = 1;

    private final static int SCROLL_DURATION = 400;

    // when pull up >= 50px
    private final static int PULL_LOAD_MORE_DELTA = 50;

    // support iOS like pull
    private final static float OFFSET_RADIO = 1.8f;

    private float mLastY = -1;

    // used for scroll back
    private Scroller mScroller;
    // for mScroller, scroll back from header or footer.
    private int mScrollBack;

    // the interface to trigger refresh and load more.
    private IXListViewListener mListener;

    private XHeaderView mHeader;
    // header view content, use it to calculate the Header's height. And hide it when disable pull
    // refresh.
    private RelativeLayout mHeaderContent;
    private TextView mHeaderTime;
    private int mHeaderHeight;

    private LinearLayout mFooterLayout;
    private XFooterView mFooterView;
    private boolean mIsFooterReady = false;

    private boolean mEnablePullRefresh = true;
    private boolean mPullRefreshing = false;

    private boolean mEnablePullLoad = true;
    private boolean mEnableAutoLoad = false;
    private boolean mPullLoading = false;

    private boolean mHasMoreData = false;

    // total list items, used to detect is at the bottom of ListView
    private int mTotalItemCount;

    private void initWithContext(Context context) {
        mScroller = new Scroller(context, new DecelerateInterpolator());
        this.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    if (mHasMoreData && mEnableAutoLoad
                            && getLastVisiblePosition() == getAdapter().getItemCount() - 1) {
                        startLoadMore();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                // send to user's listener
                mTotalItemCount = getAdapter().getItemCount();
            }
        });

        // init header view
        mHeader = new XHeaderView(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        mHeader.setLayoutParams(params);
        mHeaderContent = (RelativeLayout) mHeader.findViewById(R.id.header_content);
        mHeaderTime = (TextView) mHeader.findViewById(R.id.header_hint_time);
        addHeaderView(mHeader);

        // init footer view
        mFooterView = new XFooterView(context);
        mFooterLayout = new LinearLayout(context);
        mFooterLayout.setOrientation(LinearLayout.VERTICAL);
        mFooterLayout.setLayoutParams(params);
        mFooterLayout.addView(mFooterView);

        // init header height
        ViewTreeObserver observer = mHeader.getViewTreeObserver();
        if (null != observer) {
            observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                // @TargetApi(Builder.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onGlobalLayout() {
                    mHeaderHeight = mHeaderContent.getHeight();
                    ViewTreeObserver observer = getViewTreeObserver();

                    if (null != observer) {
                        observer.removeGlobalOnLayoutListener(this);
                        // if (Builder.VERSION.SDK_INT < Builder.VERSION_CODES.JELLY_BEAN) {
                        // observer.removeGlobalOnLayoutListener(this);
                        // } else {
                        // observer.removeOnGlobalLayoutListener(this);
                        // }
                    }
                }
            });
        }

        setPullLoadEnable(false);
    }

    @Override
    public void setAdapter(RecyclerAdapter adapter) {
        if (adapter instanceof RecyclerAdapter) {
            super.setAdapter(adapter);
        }
        // make sure XFooterView is the last footer view, and only add once.
        if (!mIsFooterReady) {
            mIsFooterReady = true;
            addFooterView(mFooterLayout);
        }
    }

    @Override
    public void setAdapter(Adapter adapter) {
        // make sure XFooterView is the last footer view, and only add once.
        if (!mIsFooterReady) {
            mIsFooterReady = true;
            addFooterView(mFooterLayout);
        }
        super.setAdapter(adapter);
    }

    /**
     * Enable or disable pull down refresh feature.
     *
     * @param enable
     */
    public void setPullRefreshEnable(boolean enable) {
        mEnablePullRefresh = enable;

        // disable, hide the content
        mHeaderContent.setVisibility(enable ? View.VISIBLE : View.INVISIBLE);
    }

    /**
     * Enable or disable pull up load more feature.
     *
     * @param enable
     */
    public void setPullLoadEnable(boolean enable) {
        mEnablePullLoad = enable;

        if (!mEnablePullLoad) {
            mFooterView.setBottomMargin(0);
            mFooterView.hide();
            mFooterView.setPadding(0, 0, 0, mFooterView.getHeight() * (-1));
            mFooterView.setOnClickListener(null);
            mFooterView.setVisibility(View.GONE);
            // this.removeFooterView(mFooterView);
        } else {
            mPullLoading = false;
            mFooterView.setPadding(0, 0, 0, 0);
            mFooterView.show();
            if (mHasMoreData) {
                mFooterView.setState(XFooterView.STATE_NORMAL);
            } else {
                mFooterView.setState(XFooterView.STATE_NOMORE);
            }
            // both "pull up" and "click" will invoke load more.
            mFooterView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mHasMoreData) {
                        startLoadMore();
                    }
                }
            });
            // this.addFooterView(mFooterView);
            mFooterView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Enable or disable auto load more feature when scroll to bottom.
     *
     * @param enable
     */
    public void setAutoLoadEnable(boolean enable) {
        mEnableAutoLoad = enable;
    }

    /**
     * Stop refresh, reset header view.
     */
    public void stopRefresh() {
        if (mPullRefreshing) {
            mPullRefreshing = false;
            resetHeaderHeight();
        }
    }

    /**
     * Stop load more, reset footer view.
     */
    public void stopLoadMore() {
        if (mPullLoading) {
            mPullLoading = false;
            mFooterView.setState(XFooterView.STATE_NORMAL);
        }
    }

    /**
     * Set last refresh time
     *
     * @param time
     */
    public void setRefreshTime(String time) {
        mHeaderTime.setText(time);
    }

    /**
     * Set listener.
     *
     * @param listener
     */
    public void setXListViewListener(IXListViewListener listener) {
        mListener = listener;
    }

    /**
     * Auto call back refresh.
     */
    public void autoRefresh() {
        mHeader.setVisibleHeight(mHeaderHeight);

        if (mEnablePullRefresh && !mPullRefreshing) {
            // update the arrow image not refreshing
            if (mHeader.getVisibleHeight() > mHeaderHeight) {
                mHeader.setState(XHeaderView.STATE_READY);
            } else {
                mHeader.setState(XHeaderView.STATE_NORMAL);
            }
        }

        mPullRefreshing = true;
        mHeader.setState(XHeaderView.STATE_REFRESHING);
        refresh();
    }

    private void updateHeaderHeight(float delta) {
        mHeader.setVisibleHeight((int) delta + mHeader.getVisibleHeight());

        if (mEnablePullRefresh && !mPullRefreshing) {
            // update the arrow image unrefreshing
            if (mHeader.getVisibleHeight() > mHeaderHeight) {
                mHeader.setState(XHeaderView.STATE_READY);
            } else {
                mHeader.setState(XHeaderView.STATE_NORMAL);
            }
        }

        // scroll to top each time
        // setSelection(0);
    }

    private void resetHeaderHeight() {
        int height = mHeader.getVisibleHeight();
        if (height == 0) return;

        // refreshing and header isn't shown fully. do nothing.
        if (mPullRefreshing && height <= mHeaderHeight) return;

        // default: scroll back to dismiss header.
        int finalHeight = 0;
        // is refreshing, just scroll back to show all the header.
        if (mPullRefreshing && height > mHeaderHeight) {
            finalHeight = mHeaderHeight;
        }

        mScrollBack = SCROLL_BACK_HEADER;
        mScroller.startScroll(0, height, 0, finalHeight - height, SCROLL_DURATION);

        // trigger computeScroll
        invalidate();
    }

    private void updateFooterHeight(float delta) {
        int height = mFooterView.getBottomMargin() + (int) delta;

        if (mEnablePullLoad && !mPullLoading) {
            if (!mHasMoreData) {
                mFooterView.setState(XFooterView.STATE_NOMORE);
                return;
            }
            if (height > PULL_LOAD_MORE_DELTA) {
                // height enough to invoke load more.
                mFooterView.setState(XFooterView.STATE_READY);
            } else {
                mFooterView.setState(XFooterView.STATE_NORMAL);
            }
        }

        mFooterView.setBottomMargin(height);

        // scroll to bottom
        // setSelection(mTotalItemCount - 1);
    }

    private void resetFooterHeight() {
        int bottomMargin = mFooterView.getBottomMargin();

        if (bottomMargin > 0) {
            mScrollBack = SCROLL_BACK_FOOTER;
            mScroller.startScroll(0, bottomMargin, 0, -bottomMargin, SCROLL_DURATION);
            invalidate();
        }
    }

    private void startLoadMore() {
        mPullLoading = true;
        mFooterView.setState(XFooterView.STATE_LOADING);
        loadMore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mLastY == -1) {
            mLastY = ev.getRawY();
        }

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = ev.getRawY();
                break;

            case MotionEvent.ACTION_MOVE:
                final float deltaY = ev.getRawY() - mLastY;
                mLastY = ev.getRawY();

                if (getFirstVisiblePosition() == 0 && (mHeader.getVisibleHeight() > 0 || deltaY > 0)) {
                    // the first item is showing, header has shown or pull down.
                    updateHeaderHeight(deltaY / OFFSET_RADIO);

                } else if (getLastVisiblePosition() == mTotalItemCount - 1
                        && (mFooterView.getBottomMargin() > 0 || deltaY < 0)) {
                    // last item, already pulled up or want to pull up.
                    updateFooterHeight(-deltaY / OFFSET_RADIO);
                }
                break;

            default:
                // reset
                mLastY = -1;
                if (getFirstVisiblePosition() == 0) {
                    // invoke refresh
                    if (mEnablePullRefresh && mHeader.getVisibleHeight() > mHeaderHeight) {
                        mPullRefreshing = true;
                        mHeader.setState(XHeaderView.STATE_REFRESHING);
                        refresh();
                    }

                    resetHeaderHeight();

                } else if (getLastVisiblePosition() == mTotalItemCount - 1) {
                    // invoke load more.
                    if (mHasMoreData && mEnablePullLoad
                            && mFooterView.getBottomMargin() > PULL_LOAD_MORE_DELTA) {
                        startLoadMore();
                    }
                    resetFooterHeight();
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            if (mScrollBack == SCROLL_BACK_HEADER) {
                mHeader.setVisibleHeight(mScroller.getCurrY());
            } else {
                mFooterView.setBottomMargin(mScroller.getCurrY());
            }

            postInvalidate();
        }

        super.computeScroll();
    }

    private void refresh() {
        if (mEnablePullRefresh && null != mListener) {
            mListener.onRefresh();
        }
    }

    private void loadMore() {
        if (mHasMoreData && mEnablePullLoad && null != mListener) {
            mListener.onLoadMore();
        }
    }

    public boolean isEnablePullRefresh() {
        return mEnablePullRefresh;
    }

    public boolean isEnablePullLoad() {
        return mEnablePullLoad;
    }

    /**
     * Implements this interface to get refresh/load more event.
     *
     * @author markmjw
     */
    public interface IXListViewListener {
        void onRefresh();

        void onLoadMore();
    }

    public void setHasMoreData(boolean isHasMoreData) {
        if (!isHasMoreData) {
            mFooterView.setState(XFooterView.STATE_NOMORE);
            mHasMoreData = false;
        } else {
            mFooterView.setState(XFooterView.STATE_NORMAL);
            mHasMoreData = true;
        }

    }

    public Bundle onSavePullInfoState() {
        Bundle bundle = new Bundle();
        bundle.putBoolean("isHaveMoreData", this.mHasMoreData);
        bundle.putBoolean("refreshEnable", this.mEnablePullRefresh);
        bundle.putBoolean("loadMoreEnable", this.mEnablePullLoad);
        bundle.putString("refreshTime", mHeaderTime.getText().toString());
        return bundle;
    }

    public Bundle onRestorePullInfoState(Bundle bundle) {
        this.setHasMoreData(bundle.getBoolean("isHaveMoreData"));
        this.setPullLoadEnable(bundle.getBoolean("loadMoreEnable"));
        this.setPullRefreshEnable(bundle.getBoolean("refreshEnable"));
        this.setRefreshTime(bundle.getString("refreshTime"));
        return bundle;
    }

}
