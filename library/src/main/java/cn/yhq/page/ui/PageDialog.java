package cn.yhq.page.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import java.util.List;

import cn.yhq.dialog.core.DialogBuilder;
import cn.yhq.dialog.core.IDialog;
import cn.yhq.page.core.IPageDataIntercept;
import cn.yhq.page.core.LetterNameGetter;
import cn.yhq.page.core.OnPageListener;
import cn.yhq.page.core.OnPullToRefreshProvider;
import cn.yhq.page.core.PageAction;

/**
 * Created by Yanghuiqiang on 2016/10/20.
 */

public abstract class PageDialog<T, I> implements
        OnPageListener,
        IPageContextProvider<T, I>,
        DialogInterface.OnCancelListener,
        DialogInterface.OnDismissListener,
        DialogInterface.OnShowListener,
        DialogBuilder.OnStateChangeListener {
    private PageContext<T, I> mPageContext;
    private Context mContext;
    private Bundle savedInstanceState;

    public PageDialog(Context context) {
        this.mContext = context;
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

    protected final DialogBuilder getDefaultDialogBuilder() {
        return DialogBuilder.otherDialog(mContext)
                .setContentView(getDialogContentView(this.getPageView()))
                .setOnPositiveButtonClickListener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onPositiveButtonClick(dialog, which);
                    }
                })
                .setOnNegativeButtonClickListener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onNegativeButtonClick(dialog, which);
                    }
                })
                .setOnCancelListener(this)
                .setOnDismissListener(this)
                .setOnShowListener(this)
                .setOnStateChangeListener(this);
    }

    protected IDialog onCreateDialog(Bundle args) {
        return getDefaultDialogBuilder().create();
    }

    public final IDialog create() {
        return create(null);
    }

    public final IDialog create(Bundle args) {
        onViewCreated(args);
        return onCreateDialog(args);
    }

    public void onPositiveButtonClick(DialogInterface dialog, int which) {

    }

    public void onNegativeButtonClick(DialogInterface dialog, int which) {

    }

    public final IDialog show() {
        IDialog dialog = create().show();
        return dialog;
    }

    @Override
    public void onShow(DialogInterface dialogInterface) {
        mPageContext = PageContext.Builder
                .createBuilder(this.getContext(), this)
                .addOnPageListener(this)
                .build();
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

    public final void searchPageData(String keyword, LetterNameGetter<I> listener) {
        mPageContext.searchPageData(keyword, listener);
    }

    public final void attachSearchEditText(final EditText searchEditText, final LetterNameGetter<I> listener) {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                searchPageData(searchEditText.getText().toString(), listener);
            }
        });
    }

    /**
     * 分页配置
     *
     * @param pageConfig
     */
    @Override
    public void onPageConfig(PageConfig pageConfig) {
    }

    public abstract void onViewCreated(Bundle args);

    public final PageContext<T, I> getPageContext() {
        return mPageContext;
    }

    public final PageConfig getPageConfig() {
        return mPageContext.getPageConfig();
    }

    @Override
    public void addPageDataIntercept(List<IPageDataIntercept<I>> pageDataIntercepts) {

    }

    @Override
    public void addOnPageListener(List<OnPageListener> mOnPageListeners) {

    }

    @Override
    public IPageViewManager getPageViewManager() {
        return null;
    }

    @Override
    public IPageViewProvider getPageViewProvider() {
        return null;
    }

    @Override
    public OnPullToRefreshProvider getOnPullToRefreshProvider() {
        return null;
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
