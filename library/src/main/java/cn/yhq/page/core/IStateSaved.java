package cn.yhq.page.core;

import android.os.Bundle;

/**
 * Created by Yanghuiqiang on 2017/2/17.
 */

public interface IStateSaved {
    boolean saveState(Bundle state);

    boolean restoreState(Bundle state);
}
