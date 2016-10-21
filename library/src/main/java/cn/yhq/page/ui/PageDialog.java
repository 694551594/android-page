package cn.yhq.page.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import java.util.List;

import cn.yhq.dialog.core.DialogBuilder;
import cn.yhq.dialog.core.IDialog;
import cn.yhq.page.core.IPageDataIntercept;
import cn.yhq.page.core.OnPageListener;
import cn.yhq.page.core.OnPullToRefreshProvider;
import cn.yhq.page.core.PageAction;

/**
 * Created by Yanghuiqiang on 2016/10/20.
 */

public abstract class PageDialog<T, I> implements OnPageListener, IPageContextProvider<T, I>, DialogInterface.OnCancelListener, DialogInterface.OnDismissListener, DialogInterface.OnShowListener, DialogBuilder.OnStateChangeListener {
    private PageContext<T, I> mPageContext;
    private Context mContext;
    private Bundle savedInstanceState;

    public PageDialog(Context context) {
        this.mContext = context;
        this.mPageContext = new PageContext<>(mContext);
        this.mPageContext.addOnPageListener(this);
    }

    public final Context getContext() {
        return mContext;
    }

    private View getDialogContentView(View pageView) {
        if (pageView.getParent() == null) {
            return pageView;
        } else {
            return getDialogContentView((View) pageView.getParent());
        }
    }

    private static int dp2Px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public final IDialog create() {
        onViewCreated();
        return DialogBuilder.otherDialog(mContext)
                .setContentView(getDialogContentView(this.getPageView()), new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, dp2Px(getContext(), 800)))
                .setOnCancelListener(this)
                .setOnDismissListener(this)
                .setOnShowListener(this)
                .setOnStateChangeListener(this)
                .create();
    }

    public final IDialog show() {
        IDialog dialog = create().show();
        return dialog;
    }

    @Override
    public void onShow(DialogInterface dialogInterface) {
        mPageContext.initPageContext(this);
        mPageContext.start(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(IDialog dialog, Bundle savedInstanceState) {
        mPageContext.savePageDataState(savedInstanceState);
    }

    /**
     * 此方法是在对话框显示之前回调的，所以这个地方不能直接调用mPageContext.start(savedInstanceState);
     * 因为 mPageContext.initPageContext(this);必须在视图初始化完成后才可以调用
     *
     * @param dialog
     * @param savedInstanceState
     */
    @Override
    public void onRestoreInstanceState(IDialog dialog, Bundle savedInstanceState) {
        this.savedInstanceState = savedInstanceState;
    }

    @Override
    public void onCancel(DialogInterface dialogInterface) {
        mPageContext.onDestroy();
    }

    @Override
    public void onDismiss(DialogInterface dialogInterface) {
        mPageContext.onDestroy();
    }


    public final void initPageData() {
        mPageContext.initPageData();
    }

    public final void refreshPageData() {
        mPageContext.refreshPageData();
    }

    /**
     * 分页配置
     *
     * @param pageConfig
     */
    @Override
    public void onPageConfig(PageConfig pageConfig) {
    }

    public abstract void onViewCreated();

    public final PageContext<T, I> getPageContext() {
        return mPageContext;
    }

    public final PageConfig getPageConfig() {
        return mPageContext.getPageConfig();
    }

    @Override
    public void addPageDataIntercepts(List<IPageDataIntercept<I>> pageDataIntercepts) {

    }

    /**
     * 获取pageview，比如listview，gridview，recyclerview等等
     *
     * @return
     */
    public abstract View getPageView();

    @Override
    public IPageViewManager getPageViewManager() {
        return mPageContext.getDefaultPageViewManager(this.getPageViewProvider());
    }

    @Override
    public IPageViewProvider getPageViewProvider() {
        return mPageContext.getDefaultPageViewProvider(this.getPageView());
    }

    @Override
    public OnPullToRefreshProvider getOnPullToRefreshProvider() {
        return PageContext.getDefaultPullToRefreshProvider(this.getPageView());
    }

    @Override
    public void onPageCancelRequests() {

    }

    @Override
    public void onPageRequestStart(PageAction pageAction) {

    }

    @Override
    public void onPageLoadComplete(PageAction pageAction, boolean isFromCache, boolean isSuccess) {

    }

    @Override
    public void onPageLoadCache(PageAction pageAction, boolean isHaveCache) {

    }

    @Override
    public void onPageRefresh() {

    }

    @Override
    public void onPageLoadMore() {

    }

    @Override
    public void onPageInit() {

    }

    @Override
    public void onPageException(Throwable e) {

    }

}
