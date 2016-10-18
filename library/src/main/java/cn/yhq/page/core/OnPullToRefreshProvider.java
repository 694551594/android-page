package cn.yhq.page.core;

/**
 * 主要负责提供上拉加载下拉刷新组件，我们这里提供了XListView、XExpandableListView以及google的SwipeRefreshLayout组件，如果你使用的是其他的上拉加载下拉刷新组件，你需要实现OnPullToRefreshProvider接口。
 *
 * @author Yanghuiqiang 2014-9-4
 */
public interface OnPullToRefreshProvider {

    /**
     * 刷新接口
     *
     * @author Yanghuiqiang 2014-10-9
     */
    interface OnRefreshListener {
        void onPullToRefresh();

        void onPullToLoadMore();
    }

    /**
     * 设置是否有更多数�?
     *
     * @param isHaveMoreData
     */
    void setHaveMoreData(boolean isHaveMoreData);

    /**
     * 下拉刷新是否有效
     *
     * @param enable
     */
    void setPullRefreshEnable(boolean enable);

    boolean isPullRefreshEnable();

    /**
     * 上拉加载是否有效
     *
     * @param enable
     */
    void setPullLoadMoreEnable(boolean enable);

    boolean isPullLoadMoreEnable();

    /**
     * 刷新完成
     *
     * @param newDataSize
     * @param success
     */
    void onRefreshComplete(int newDataSize, boolean success);

    /**
     * 设置刷新监听
     *
     * @param onRefreshListener
     */
    void setOnRefreshListener(OnRefreshListener onRefreshListener);

}
