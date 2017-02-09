package cn.yhq.page.ui;

import android.os.Bundle;

/**
 * 分页配置
 *
 * @author Yanghuiqiang 2015-5-25
 */
public final class PageConfig {
    // 分页大小
    int pageSize;
    // 是否自动载入数据
    boolean autoInitPageData;
    // 是否在请求前清空listview的数据
    boolean clearPageDataBeforeRequest;

    static final String KEY_SAVED_STATE = "saved_state_pageconfig";

    PageConfig() {
        pageSize = 20;
        autoInitPageData = true;
        clearPageDataBeforeRequest = false;
    }

    void onSaveInstanceState(Bundle bundle) {
        Bundle b = new Bundle();
        b.putInt("pageSize", pageSize);
        b.putBoolean("autoInitPageData", autoInitPageData);
        b.putBoolean("clearPageDataBeforeRequest", clearPageDataBeforeRequest);
        bundle.putBundle(KEY_SAVED_STATE, b);
    }

    void onRestoreInstanceState(Bundle bundle) {
        Bundle b = bundle.getBundle(KEY_SAVED_STATE);
        pageSize = b.getInt("pageSize");
        autoInitPageData = b.getBoolean("autoInitPageData");
        clearPageDataBeforeRequest = b.getBoolean("clearPageDataBeforeRequest");
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
}
