package cn.yhq.page.pulltorefresh;

import android.view.View;

/**
 * Created by Yanghuiqiang on 2016/12/1.
 */

public class PullToRefreshDefaultContext extends PullToRefreshContext<View> {

    public PullToRefreshDefaultContext(View pageView) {
        super(pageView);
    }

    @Override
    public void setHaveMoreData(boolean isHaveMoreData) {

    }

    @Override
    public void setPullRefreshEnable(boolean enable) {

    }

    @Override
    public boolean isPullRefreshEnable() {
        return false;
    }

    @Override
    public void setPullLoadMoreEnable(boolean enable) {

    }

    @Override
    public boolean isPullLoadMoreEnable() {
        return false;
    }

    @Override
    public void onRefreshComplete(int newDataSize, boolean success) {

    }

    @Override
    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {

    }
}
