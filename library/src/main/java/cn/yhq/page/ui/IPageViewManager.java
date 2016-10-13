package cn.yhq.page.ui;

import cn.yhq.page.core.PageAction;

/**
 * 主要是对IPageViewProvider提供的视图进行管理的，还有一个点击空视图重试的功能，它里面实现了分页列表监听器OnPageListener，根据分页列表请求的生命周期来显示不同的视图。如果你想自定义分页列表请求生命周期过程的UI，你可以实现此接口。
 *
 * Created by Yanghuiqiang on 2016/10/12.
 */

public interface IPageViewManager {

    void startPageRequest(PageAction pageAction);

    void completePageRequest(PageAction pageAction, int count);

    void cancelPageRequest(int count);

    void setOnReRequestListener(OnReRequestListener onReRequestListener);

}
