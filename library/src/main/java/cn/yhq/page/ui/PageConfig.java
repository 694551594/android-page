package cn.yhq.page.ui;

import android.os.Bundle;

/**
 * 分页配置
 *
 * @author Yanghuiqiang 2015-5-25
 */
public class PageConfig {
    // 分页大小
    int pageSize;
    // 是否自动载入数据
    boolean autoInitPageData;
    // 是否在请求前清空listview的数据
    boolean clearPageDataBeforeRequest;
    // 下拉刷新是否有效
    boolean pullRefreshEnable;
    // 上拉加载是否有效
    boolean pullLoadMoreEnable;
    // pull（下拉或者上拉）是否有效
    boolean pullEnable;
    // listview是否总是显示
    boolean listViewAlwaysVisiable;

    static final String KEY_SAVED_STATE = "saved_state_pageconfig";

    PageConfig() {
        pageSize = 20;
        autoInitPageData = true;
        clearPageDataBeforeRequest = false;
        pullRefreshEnable = true;
        pullLoadMoreEnable = true;
        listViewAlwaysVisiable = false;
        pullEnable = true;
    }

    void onSaveInstanceState(Bundle bundle) {
        Bundle b = new Bundle();
        b.putInt("pageSize", pageSize);
        b.putBoolean("autoInitPageData", autoInitPageData);
        b.putBoolean("clearPageDataBeforeRequest", clearPageDataBeforeRequest);
        b.putBoolean("setPullRefreshEnable", pullRefreshEnable);
        b.putBoolean("setPullLoadMoreEnable", pullLoadMoreEnable);
        b.putBoolean("listViewAlwaysVisiable", listViewAlwaysVisiable);
        b.putBoolean("pullEnable", pullEnable);
        bundle.putBundle(KEY_SAVED_STATE, b);
    }

    void onRestoreInstanceState(Bundle bundle) {
        Bundle b = bundle.getBundle(KEY_SAVED_STATE);
        pageSize = b.getInt("pageSize");
        autoInitPageData = b.getBoolean("autoInitPageData");
        clearPageDataBeforeRequest = b.getBoolean("clearPageDataBeforeRequest");
        pullRefreshEnable = b.getBoolean("setPullRefreshEnable");
        pullLoadMoreEnable = b.getBoolean("setPullLoadMoreEnable");
        listViewAlwaysVisiable = b.getBoolean("listViewAlwaysVisiable");
        pullEnable = b.getBoolean("pullEnable");
    }

    public PageConfig setPageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public PageConfig setAutoInitPageData(boolean autoInitPageData) {
        this.autoInitPageData = autoInitPageData;
        return this;
    }

    public PageConfig setClearPageDataBeforeRequest(boolean clearPageDataBeforeRequest) {
        this.clearPageDataBeforeRequest = clearPageDataBeforeRequest;
        return this;
    }

    public PageConfig setPullRefreshEnable(boolean pullRefreshEnable) {
        this.pullRefreshEnable = pullRefreshEnable;
        return this;
    }

    public PageConfig setPullLoadMoreEnable(boolean pullLoadMoreEnable) {
        this.pullLoadMoreEnable = pullLoadMoreEnable;
        return this;
    }

    public PageConfig setListViewAlwaysVisiable(boolean listViewAlwaysVisiable) {
        this.listViewAlwaysVisiable = listViewAlwaysVisiable;
        return this;
    }

    public PageConfig setPullEnable(boolean pullEnable) {
        this.pullEnable = pullEnable;
        if (!pullEnable) {
            this.pullRefreshEnable = false;
            this.pullLoadMoreEnable = false;
        }
        return this;
    }

}
