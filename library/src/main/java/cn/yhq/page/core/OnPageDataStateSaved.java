package cn.yhq.page.core;

import android.os.Bundle;

import java.util.List;

/**
 * Created by Yanghuiqiang on 2017/2/20.
 */

public interface OnPageDataStateSaved<I> {
    void onStateSaved(Bundle state, String key, List<I> entity);

    List<I> onStateRestored(Bundle state, String key);
}
