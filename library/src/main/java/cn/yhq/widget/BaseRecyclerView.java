package cn.yhq.widget;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import cn.yhq.adapter.recycler.OnRecyclerViewItemClickListener;
import cn.yhq.adapter.recycler.OnRecyclerViewItemLongClickListener;
import cn.yhq.adapter.recycler.RecyclerAdapter;


/**
 * 可以添加HeaderView和FooterView以及EmptyView的RecyclerView
 * <p>
 * Created by 杨慧强 on 2016/2/22.
 */
public abstract class BaseRecyclerView extends RecyclerView {
    private View mEmptyView;
    private OnRecyclerViewItemClickListener mOnItemClickListener;
    private OnRecyclerViewItemLongClickListener mOnItemLongClickListener;
    private RecyclerAdapter mRecyclerAdapter;
    private WrapAdapter mWrapAdapter;
    private ArrayList<View> mHeaderViews = new ArrayList<>();
    private ArrayList<View> mFooterViews = new ArrayList<>();
    private static final int TYPE_HEADER = 1001;
    private static final int TYPE_NORMAL = 1000;
    private static final int TYPE_FOOTER = 1002;
    private RecyclerView.ItemDecoration mDividerDecoration;

    private AdapterDataObserver mEmptyObserver = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            if (mRecyclerAdapter != null && mEmptyView != null) {
                if (mRecyclerAdapter.getItemCount() == 0) {
                    mEmptyView.setVisibility(View.VISIBLE);
                    BaseRecyclerView.this.setVisibility(View.GONE);
                } else {
                    mEmptyView.setVisibility(View.GONE);
                    BaseRecyclerView.this.setVisibility(View.VISIBLE);
                }
            }
        }
    };

    public BaseRecyclerView(Context context) {
        this(context, null);
        init();
    }

    public BaseRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        init();
    }

    public BaseRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        this.setItemAnimator(new DefaultItemAnimator());
    }

    public abstract int getFirstVisiblePosition();

    public abstract int getLastVisiblePosition();

    @Override
    public void setAdapter(Adapter adapter) {
        if (adapter instanceof RecyclerAdapter) {
            setAdapter(adapter);
        } else {
            super.setAdapter(adapter);
        }
    }

    private OnRecyclerViewItemClickListener mOnRecyclerViewItemClickListener =
            new OnRecyclerViewItemClickListener() {
                @Override
                public void onRecyclerViewItemClick(View itemView, int position) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onRecyclerViewItemClick(itemView, position);
                    }
                }
            };

    private OnRecyclerViewItemLongClickListener mOnRecyclerViewItemLongClickListener =
            new OnRecyclerViewItemLongClickListener() {

                @Override
                public boolean onRecyclerViewItemLongClick(View itemView, int position) {
                    if (mOnItemLongClickListener != null) {
                        return mOnItemLongClickListener.onRecyclerViewItemLongClick(itemView, position);
                    }
                    return false;
                }
            };

    @Override
    public Adapter getAdapter() {
        return mRecyclerAdapter;
    }

    public void setAdapter(RecyclerAdapter adapter) {
        Adapter oldAdapter = getAdapter();
        if (oldAdapter != null && mEmptyObserver != null) {
            oldAdapter.unregisterAdapterDataObserver(mEmptyObserver);
        }
        this.mRecyclerAdapter = adapter;
        this.mRecyclerAdapter.setOnRecyclerViewItemClickListener(mOnRecyclerViewItemClickListener);
        this.mRecyclerAdapter
                .setOnRecyclerViewItemLongClickListener(mOnRecyclerViewItemLongClickListener);
        this.mWrapAdapter = new WrapAdapter(this.mHeaderViews, this.mFooterViews, mRecyclerAdapter);
        this.mWrapAdapter.registerAdapterDataObserver(mEmptyObserver);
        // this.mWrapAdapter.setOnRecyclerViewItemClickListener(mOnRecyclerViewItemClickListener);
        // this.mWrapAdapter.setOnRecyclerViewItemLongClickListener(mOnRecyclerViewItemLongClickListener);
        super.setAdapter(mWrapAdapter);
        mEmptyObserver.onChanged();
    }

    public void setEmptyView(View emptyView) {
        this.mEmptyView = emptyView;
        mEmptyObserver.onChanged();
    }

    public void setOnRecyclerViewItemLongClickListener(
            OnRecyclerViewItemLongClickListener onRecyclerViewItemLongClickListener) {
        this.mOnItemLongClickListener = onRecyclerViewItemLongClickListener;
    }

    public void setOnRecyclerViewItemClickListener(
            OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        this.mOnItemClickListener = onRecyclerViewItemClickListener;
    }

    public void addHeaderView(View view) {
        mHeaderViews.add(view);
        if (this.mWrapAdapter != null) {
            this.mWrapAdapter.notifyDataSetChanged();
        }
    }

    public int getHeaderViewsCount() {
        return mHeaderViews.size();
    }

    public void removeHeaderView(View view) {
        mHeaderViews.remove(view);
        if (this.mWrapAdapter != null) {
            this.mWrapAdapter.notifyDataSetChanged();
        }
    }

    public void removeFooterView(View view) {
        mFooterViews.remove(view);
        if (this.mWrapAdapter != null) {
            this.mWrapAdapter.notifyDataSetChanged();
        }
    }

    public int getFooterViewsCount() {
        return mFooterViews.size();
    }

    public void addFooterView(View view) {
        mFooterViews.add(view);
        if (this.mWrapAdapter != null) {
            this.mWrapAdapter.notifyDataSetChanged();
        }
    }

    public View getEmptyView() {
        return this.mEmptyView;
    }

    private class WrapAdapter extends Adapter<cn.yhq.adapter.recycler.ViewHolder> {
        private RecyclerAdapter mAdapter;
        private ArrayList<View> mHeaderViews;
        private ArrayList<View> mFooterViews;
        private int mHeaderPosition = 0;
        private int mFooterPosition = 0;

        public WrapAdapter(ArrayList<View> headerViews, ArrayList<View> footerViews,
                           RecyclerAdapter adapter) {
            this.mAdapter = adapter;
            this.mHeaderViews = headerViews;
            this.mFooterViews = footerViews;
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
            LayoutManager manager = recyclerView.getLayoutManager();
            if (manager instanceof GridLayoutManager) {
                final GridLayoutManager gridManager = ((GridLayoutManager) manager);
                gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        return (isHeader(position) || isFooter(position)) ? gridManager.getSpanCount() : 1;
                    }
                });
            }
        }

        @Override
        public void onViewAttachedToWindow(cn.yhq.adapter.recycler.ViewHolder holder) {
            super.onViewAttachedToWindow(holder);
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams
                    && (isHeader(holder.getLayoutPosition()) || isFooter(holder.getLayoutPosition()))) {
                StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
                p.setFullSpan(true);
            }
        }

        @Override
        public cn.yhq.adapter.recycler.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == TYPE_HEADER) {
                return new cn.yhq.adapter.recycler.ViewHolder(mHeaderViews.get(mHeaderPosition++));
            } else if (viewType == TYPE_FOOTER) {
                return new cn.yhq.adapter.recycler.ViewHolder(mFooterViews.get(mFooterPosition++));
            }
            return mAdapter.onCreateViewHolder(parent, viewType);
        }

        public boolean isHeader(int position) {
            return position < mHeaderViews.size();
        }

        public boolean isFooter(int position) {
            return position >= getItemCount() - mFooterViews.size();
        }

        public int getHeadersCount() {
            return mHeaderViews.size();
        }

        public int getFootersCount() {
            return mFooterViews.size();
        }

        @Override
        public void onBindViewHolder(cn.yhq.adapter.recycler.ViewHolder holder, int position) {
            if (isHeader(position)) {
                return;
            }
            if (isFooter(position)) {
                return;
            }
            int adjPosition = position - getHeadersCount();
            int adapterCount;
            if (mAdapter != null) {
                adapterCount = mAdapter.getItemCount();
                if (adjPosition < adapterCount) {
                    mAdapter.onBindViewHolder(holder, adjPosition);
                    return;
                }
            }
        }

        @Override
        public int getItemCount() {
            if (mAdapter != null) {
                return getHeadersCount() + getFootersCount() + mAdapter.getItemCount();
            } else {
                return getHeadersCount() + getFootersCount();
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (isHeader(position)) {
                return TYPE_HEADER;
            }
            if (isFooter(position)) {
                return TYPE_FOOTER;
            }
            int adjPosition = position - getHeadersCount();
            int adapterCount;
            if (mAdapter != null) {
                adapterCount = mAdapter.getItemCount();
                if (adjPosition < adapterCount) {
                    return mAdapter.getItemViewType(adjPosition);
                }
            }
            return TYPE_NORMAL;
        }

        @Override
        public long getItemId(int position) {
            if (mAdapter != null && position >= getHeadersCount()) {
                int adjPosition = position - getHeadersCount();
                int adapterCount = mAdapter.getItemCount();
                if (adjPosition < adapterCount) {
                    return mAdapter.getItemId(adjPosition);
                }
            }
            return -1;
        }

        @Override
        public void unregisterAdapterDataObserver(AdapterDataObserver observer) {
            if (mAdapter != null) {
                mAdapter.unregisterAdapterDataObserver(observer);
            }
        }

        @Override
        public void registerAdapterDataObserver(AdapterDataObserver observer) {
            if (mAdapter != null) {
                mAdapter.registerAdapterDataObserver(observer);
            }
        }

    }

    public RecyclerView.ItemDecoration getDividerDecoration() {
        return mDividerDecoration;
    }

    public void setDividerDecoration(RecyclerView.ItemDecoration itemDecoration) {
        if (mDividerDecoration != null) {
            this.removeItemDecoration(mDividerDecoration);
        }
        this.addItemDecoration(itemDecoration);
        this.mDividerDecoration = itemDecoration;
    }

}
