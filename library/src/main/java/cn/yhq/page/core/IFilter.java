package cn.yhq.page.core;

/**
 * Created by Yanghuiqiang on 2017/2/9.
 */

public interface IFilter<I> {

    boolean filter(String keyword, I entity);

}
