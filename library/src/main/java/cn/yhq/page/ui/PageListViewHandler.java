package cn.yhq.page.ui;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * Created by Administrator on 2017/3/27.
 */

public class PageListViewHandler implements IPageViewHandler<ListView> {

    @Override
    public void setup(ListView pageView, View loadingView, View emptyView) {
        ViewGroup viewGroup = (ViewGroup) pageView.getParent();
        viewGroup.addView(loadingView);
        viewGroup.addView(emptyView);
        pageView.setEmptyView(loadingView);
        pageView.setEmptyView(emptyView);
        emptyView.setVisibility(View.GONE);
        loadingView.setVisibility(View.GONE);
    }

    @Override
    public void showPageLoadingView(ListView pageView, View loadingView, View emptyView) {
        loadingView.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.GONE);
    }

    @Override
    public void showPageEmptyView(ListView pageView, View loadingView, View emptyView) {
        emptyView.setVisibility(View.VISIBLE);
        loadingView.setVisibility(View.GONE);
    }

    @Override
    public void showPageView(ListView pageView) {
        pageView.setVisibility(View.VISIBLE);
    }
}
