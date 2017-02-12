package cn.yhq.page.http;

import java.util.List;

import cn.yhq.page.core.IPageChecker;

/**
 * Created by Administrator on 2017/2/12.
 */

public abstract class RetrofitPageCheckedActivity<T, I> extends RetrofitPageActivity<T, I>
        implements IPageChecker<I> {

    @Override
    public void clearAllChecked() {
        this.getPageContext().getPageChecker().clearAllChecked();
    }

    @Override
    public void toggleChecked(int position) {
        this.getPageContext().getPageChecker().toggleChecked(position);
    }

    @Override
    public boolean isChecked(int position) {
        return this.getPageContext().getPageChecker().isChecked(position);
    }

    @Override
    public boolean isDisable(int position) {
        return this.getPageContext().getPageChecker().isDisable(position);
    }

    @Override
    public void setChecked(int position, boolean isChecked) {
        this.getPageContext().getPageChecker().setChecked(position, isChecked);
    }

    @Override
    public boolean isAllChecked() {
        return this.getPageContext().getPageChecker().isAllChecked();
    }

    @Override
    public boolean isAllEnable() {
        return this.getPageContext().getPageChecker().isAllEnable();
    }

    @Override
    public void setAllChecked(boolean isChecked) {
        this.getPageContext().getPageChecker().setAllChecked(isChecked);
    }

    @Override
    public void setCheckedEntityList(List<I> list) {
        this.getPageContext().getPageChecker().setCheckedEntityList(list);
    }

    @Override
    public void setDisableEntityList(List<I> list) {
        this.getPageContext().getPageChecker().setDisableEntityList(list);
    }

    @Override
    public List<I> getCheckedEntityList(boolean appendDisableEntity) {
        return this.getPageContext().getPageChecker().getCheckedEntityList(appendDisableEntity);
    }

    @Override
    public List<I> getAddedEntityList() {
        return this.getPageContext().getPageChecker().getAddedEntityList();
    }

    @Override
    public List<I> getRemovedEntityList() {
        return this.getPageContext().getPageChecker().getRemovedEntityList();
    }

    @Override
    public int getCheckEntityCount(boolean appendDisableEntity) {
        return this.getPageContext().getPageChecker().getCheckEntityCount(appendDisableEntity);
    }

    @Override
    public void setPageData(List<I> pageData) {
        this.getPageContext().getPageChecker().setPageData(pageData);
    }

    @Override
    public List<I> getDisabledEntityList() {
        return this.getPageContext().getPageChecker().getDisabledEntityList();
    }
}
