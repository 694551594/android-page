package cn.yhq.page.core;

import android.os.Bundle;

/**
 * Created by Yanghuiqiang on 2017/2/17.
 */

interface IStateSaved<I> {
    boolean saveState(Bundle state, OnPageDataStateSaved<I> listener);

    boolean restoreState(Bundle state, OnPageDataStateSaved<I> listener);
}
