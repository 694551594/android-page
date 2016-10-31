package cn.yhq.page.simple;

import java.util.List;

import cn.yhq.page.core.IPageDataParser;
import cn.yhq.page.core.IPageRequester;
import cn.yhq.page.ui.PageFragment;

/**
 * @param <I>
 * @author Yanghuiqiang 2015-5-27
 */
public abstract class SimplePageFragment<I> extends PageFragment<List<I>, I> {

    @Override
    public IPageDataParser<List<I>, I> getPageDataParser() {
        return new SimplePageDataParser();
    }

    @Override
    public IPageRequester<List<I>, I> getPageRequester() {
        return new SimplePageRequester<I>(this.getContext()) {
            @Override
            public List<I> getSimplePageData() {
                return getPageData();
            }
        };
    }

    public abstract List<I> getPageData();
}
