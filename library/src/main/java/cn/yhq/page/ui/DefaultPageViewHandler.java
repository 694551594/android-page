package cn.yhq.page.ui;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2017/3/27.
 */

public class DefaultPageViewHandler extends PageViewHandler<View> {
    private ViewGroup mParentView;
    private ViewGroup mPageLayout;
    private ViewGroup.LayoutParams mParams;

    @Override
    public void setup(View pageView, View loadingView, View emptyView) {
        super.setup(pageView, loadingView, emptyView);
        this.mPageLayout = (ViewGroup) this.mPageView.getParent();
        this.mParentView = (ViewGroup) mPageLayout.getParent();
        this.mParams = this.mPageLayout.getLayoutParams();
    }

    @Override
    public void showPageLoadingView() {
        reset();
        this.mParentView.addView(this.mLoadingView, mParams);
    }

    @Override
    public void showPageEmptyView() {
        reset();
        this.mParentView.addView(this.mEmptyView, mParams);
    }

    private void reset() {
        this.mPageLayout.setVisibility(View.GONE);
        if (this.mEmptyView != null) {
            this.mParentView.removeView(this.mEmptyView);
        }
        if (this.mLoadingView != null) {
            this.mParentView.removeView(this.mLoadingView);
        }
    }

    @Override
    public void showPageView() {
        super.showPageView();
        this.mPageLayout.setVisibility(View.VISIBLE);
    }
}
