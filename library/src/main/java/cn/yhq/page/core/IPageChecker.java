package cn.yhq.page.core;

import java.util.List;

/**
 * 可以checked的接口
 *
 * @author Yanghuiqiang
 *         <p>
 *         2015-10-30
 */
public interface IPageChecker<T> {

    /**
     * 清除所有的选中状态
     */
    void clearAllChecked();

    /**
     * 切换某个位置的选中和不选中的状态
     *
     * @param position
     */
    void toggleChecked(int position);

    /**
     * 这个位置是否选中
     *
     * @param position
     * @return
     */
    boolean isChecked(int position);

    /**
     * 这个位置是否不可选
     *
     * @param position
     * @return
     */
    boolean isDisable(int position);

    /**
     * 设置某一个位置选择或者不选中
     *
     * @param position
     * @param isChecked
     */
    void setChecked(int position, boolean isChecked);

    /**
     * 是否全选
     *
     * @return
     */
    boolean isAllChecked();

    /**
     * 全选按钮是否可以选择
     *
     * @return
     */
    boolean isAllEnable();

    /**
     * 全选或者全不选
     *
     * @param isChecked
     */
    void setAllChecked(boolean isChecked);

    /**
     * 设置选中的list
     *
     * @param list
     */
    void setCheckedEntityList(List<T> list);

    /**
     * 设置不可选的list
     *
     * @param list
     */
    void setDisableEntityList(List<T> list);

    /**
     * 获取checked的list
     *
     * @return
     */
    List<T> getCheckedEntityList(boolean appendDisableEntity);

    /**
     * 和初始化checked的list相比，新添加了哪些
     *
     * @return
     */
    List<T> getAddedEntityList();

    /**
     * 和初始化checked的list相比，移除了哪些
     *
     * @return
     */
    List<T> getRemovedEntityList();


    int getCheckEntityCount(boolean appendDisableEntity);

    void setPageData(List<T> pageData);

    List<T> getDisabledEntityList();
}
