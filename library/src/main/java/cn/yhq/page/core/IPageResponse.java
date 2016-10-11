package cn.yhq.page.core;

import android.content.Context;

/**
 * Created by Yanghuiqiang on 2016/10/11.
 */

public interface IPageResponse<T> {
  void onResponse(PageAction pageAction, T response, boolean isFromCache);

  void onException(Context context, Throwable throwable);
}
