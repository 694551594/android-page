package cn.yhq.page.core;

import java.util.List;

/**
 * Created by Yanghuiqiang on 2016/10/11.
 * <p>
 * 主要负责将解析后的数据适配到Adapter上并显示出来，
 * <p>
 * 如ListAdapter、ExpandableListAdapter以及RecyclerAdapter，
 * <p>
 * 只要实现了此接口，都是可以进行数据适配的。
 */
public interface IPageAdapter<I> {

    int getPageDataCount();

    void clear();

    void appendBefore(List<I> data);

    void appendAfter(List<I> data);

    List<I> getPageListData();

    void notifyDataSetChanged();

}
