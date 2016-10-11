package cn.yhq.page.core;

/**
 * 上拉加载下拉刷新的组件提供器
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
     * @param success
     */
    void onRefreshComplete(boolean success);

    /**
     * 设置刷新监听
     *
     * @param onRefreshListener
     */
    void setOnRefreshListener(OnRefreshListener onRefreshListener);

}
