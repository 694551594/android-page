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
        this.getPageChecker().clearAllChecked();
    }

    @Override
    public void toggleChecked(int position) {
        this.getPageChecker().toggleChecked(position);
    }

    @Override
    public boolean isChecked(int position) {
        return this.getPageChecker().isChecked(position);
    }

    @Override
    public boolean isDisable(int position) {
        return this.getPageChecker().isDisable(position);
    }

    @Override
    public void setChecked(int position, boolean isChecked) {
        this.getPageChecker().setChecked(position, isChecked);
    }

    @Override
    public boolean isAllChecked() {
        return this.getPageChecker().isAllChecked();
    }

    @Override
    public boolean isAllEnable() {
        return this.getPageChecker().isAllEnable();
    }

    @Override
    public void setAllChecked(boolean isChecked) {
        this.getPageChecker().setAllChecked(isChecked);
    }

    @Override
    public void setCheckedEntityList(List<I> list) {
        this.getPageChecker().setCheckedEntityList(list);
    }

    @Override
    public void setDisableEntityList(List<I> list) {
        this.getPageChecker().setDisableEntityList(list);
    }

    @Override
    public List<I> getCheckedEntityList(boolean appendDisableEntity) {
        return this.getPageChecker().getCheckedEntityList(appendDisableEntity);
    }

    @Override
    public List<I> getAddedEntityList() {
        return this.getPageChecker().getAddedEntityList();
    }

    @Override
    public List<I> getRemovedEntityList() {
        return this.getPageChecker().getRemovedEntityList();
    }

    @Override
    public int getCheckEntityCount(boolean appendDisableEntity) {
        return this.getPageChecker().getCheckEntityCount(appendDisableEntity);
    }

    @Override
    public void setPageData(List<I> pageData) {
        this.getPageChecker().setPageData(pageData);
    }

    @Override
    public List<I> getDisabledEntityList() {
        return this.getPageChecker().getDisabledEntityList();
    }
}
