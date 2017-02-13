package cn.yhq.page.core;

/**
 * Created by Administrator on 2017/2/12.
 */

public interface OnPageCheckedEquals<T> {
    /**
     * 两个实体之间是否相等
     *
     * @param t1
     * @param t2
     * @return
     */
    boolean equals(T t1, T t2);
}
