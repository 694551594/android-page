package cn.yhq.page.ui;

/**
 * Created by Yanghuiqiang on 2016/10/12.
 */

public interface IPageViewManager {

    void startPageRequest();

    void completePageRequest(int count);


    void setOnReRequestListener(OnReRequestListener onReRequestListener);

}
