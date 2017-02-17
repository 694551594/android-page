package cn.yhq.page.core;

import java.util.List;

/**
 * 可以checked的接口
 *
 * @author Yanghuiqiang
 *         <p>
 *         2015-10-30
 */
public interface IPageChecker<T> extends IPageCheckerImpl, IStateSaved {

    void setPageData(List<T> pageData);

    void appendPageData(List<T> pageData);

}
