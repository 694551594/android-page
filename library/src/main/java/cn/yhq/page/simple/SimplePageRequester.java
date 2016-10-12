package cn.yhq.page.simple;

import android.content.Context;

import java.util.List;

import cn.yhq.page.core.Page;
import cn.yhq.page.core.PageAction;
import cn.yhq.page.core.PageRequester;

/**
 * Created by Yanghuiqiang on 2016/10/12.
 */

public abstract class SimplePageRequester<T> extends PageRequester<List<T>, T> {

    public SimplePageRequester(Context context) {
        super(context);
    }

    @Override
    public void executeRequest(Context context, PageAction pageAction, Page page) {
        this.callNetworkResponse(getSimplePageData());
    }

    @Override
    public void onCancel() {

    }

    public abstract List<T> getSimplePageData();
}
