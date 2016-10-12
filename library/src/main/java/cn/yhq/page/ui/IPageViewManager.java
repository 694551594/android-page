package cn.yhq.page.ui;

import cn.yhq.page.core.PageAction;

/**
 * Created by Yanghuiqiang on 2016/10/12.
 */

public interface IPageViewManager {

    void startPageRequest(PageAction pageAction);

    void completePageRequest(PageAction pageAction, int count);

    void cancelPageRequest(int count);

    void setOnReRequestListener(OnReRequestListener onReRequestListener);

}
