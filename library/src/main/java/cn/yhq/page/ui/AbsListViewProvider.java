package cn.yhq.page.ui;

import android.view.View;
import android.widget.AbsListView;

/**
 * Created by Yanghuiqiang on 2016/10/11.
 */

class AbsListViewProvider implements PageViewProvider {

    @Override
    public void setEmptyView(View pageView, View emptyView) {
        ((AbsListView) pageView).setEmptyView(emptyView);
    }
}
