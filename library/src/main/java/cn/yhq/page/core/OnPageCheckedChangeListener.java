package cn.yhq.page.core;

import java.util.List;

/**
 * Created by Administrator on 2017/2/12.
 */

public interface OnPageCheckedChangeListener<I> {
    void onPageCheckedChanged(List<I> checkedList, int count);
}
