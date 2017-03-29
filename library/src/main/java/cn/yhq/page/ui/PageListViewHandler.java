package cn.yhq.page.ui;

import android.view.View;
import android.widget.AbsListView;

/**
 * Created by Administrator on 2017/3/27.
 */

public class PageListViewHandler extends BasePageViewHandler<AbsListView> {

    @Override
    public void setEmptyView(AbsListView pageView, View view) {
        pageView.setEmptyView(view);
    }

}
