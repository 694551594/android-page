package cn.yhq.page.core;

import android.os.Bundle;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Yanghuiqiang on 2017/2/20.
 */

public class DefaultOnPageDataStateSaved<I> implements OnPageDataStateSaved<I> {

    @Override
    public void onStateSaved(Bundle state, String key, List<I> entity) {
        if (entity instanceof Serializable) {
            state.putSerializable(key, (Serializable) entity);
        }
    }

    @Override
    public List<I> onStateRestored(Bundle state, String key) {
        List<I> list = (List<I>) state.getSerializable(key);
        return list;
    }

}
