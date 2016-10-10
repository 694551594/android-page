package cn.yhq.page.core;

import android.os.Bundle;

/**
 * page上下文接口
 *
 * Created by 杨慧强 on 2016/2/16.
 */
public interface IPageContext<T, L, I> {

    /**
     * 强制刷新
     *
     */
    void forceRefresh();


    /**
     * 初始化
     *
     */
    void initPageData();

    /**
     * 保存数据
     *
     */
    void onSavePageData(Bundle savedInstanceState);

    /**
     * 恢复数据
     *
     * @param savedInstanceState
     */
    void onRestorePageData(Bundle savedInstanceState);

    /**
     * 取消请求
     *
     */
    void cancelRequest();

    /**
     * 销毁
     *
     */
    void onDestroy();

    /**
     * 开始
     *
     */
    void onCreated(Bundle savedInstanceState);

    /**
     * 分页管理器
     *
     * @return
     */
    PageManager<T, L, I> getPageManager();
}
