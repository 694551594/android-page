package cn.yhq.page.core;

/**
 * Created by Yanghuiqiang on 2017/2/13.
 */

public interface OnPageCheckedInitListener<I> {
    boolean isEnable(int position, I entity);

    boolean isChecked(int position, I entity);
}
