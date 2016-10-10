package cn.yhq.page.list;

import java.util.List;

import cn.developer.sdk.page2.core.PageManager;

/**
 * 适配数据为List的pagedata解析器
 *
 * Created by 杨慧强 on 2016/2/17.
 */
public abstract class IPageListDataParser<T, I> implements PageManager.IPageDataParser<T, List<I>, I> {

    @Override
    public I getPageDataItem(List<I> list, int position) {
        return list.get(position);
    }

    @Override
    public int getPageDataSize(List<I> list) {
        return list.size();
    }

}
