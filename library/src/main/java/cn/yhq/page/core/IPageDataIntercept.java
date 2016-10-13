package cn.yhq.page.core;

import java.util.List;

/**
 * 主要负责对解析后以及适配到列表之前的数据的拦截，有的时候我们可能要对这部分数据进行特殊处理，那我们就可以使用分页列表数据拦截器。
 * <p>
 * Created by Yanghuiqiang on 2016/10/11.
 */

public interface IPageDataIntercept<I> {
    List<I> intercept(Chain<I> chain) throws Exception;

    interface Chain<I> {
        List<I> data();

        List<I> handler(List<I> data) throws Exception;
    }
}
