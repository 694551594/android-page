package cn.yhq.page.ui;

import android.view.View;

import cn.yhq.widget.xrecyclerview.BaseRecyclerView;

/**
 * Created by Administrator on 2017/3/27.
 */

public class PageRecyclerViewHandler extends BasePageViewHandler<BaseRecyclerView> {

    @Override
    public void setEmptyView(BaseRecyclerView pageView, View view) {
        pageView.setEmptyView(view);
    }

}
