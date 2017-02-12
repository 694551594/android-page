package cn.yhq.page.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/2/12.
 */

public class PageChecker<T> implements IPageChecker<T> {
    private List<T> mCheckedList;
    private List<T> mDisabledList;
    private List<T> mAllPageDataList;
    private List<T> mOriginalCheckedList;

    public final static int CHECK_MODEL_SINGLE = 1;
    public final static int CHECK_MODEL_MUTIPLE = 2;

    public int mCheckModel = CHECK_MODEL_MUTIPLE;

    private IEquals<T> mEquals;

    public PageChecker(int type) {
        this(type, new IEquals<T>() {
            @Override
            public boolean equals(T t1, T t2) {
                return t1 == t2;
            }
        });
    }

    public PageChecker(int type, IEquals<T> equals) {
        this.mCheckModel = type;
        this.mEquals = equals;
        mCheckedList = new ArrayList<>();
        mDisabledList = new ArrayList<>();
    }

    public void setAllPageDataList(List<T> allPageDataList) {
        this.mAllPageDataList = allPageDataList;
    }

    @Override
    public void clearAllChecked() {
        mCheckedList.clear();
    }

    private T getItem(int index) {
        return mCheckedList.get(index);
    }

    @Override
    public void toggleChecked(int position) {
        setChecked(position, !isChecked(position));
    }

    @Override
    public boolean isChecked(int position) {
        T entity = getItem(position);
        if (entity == null) {
            return false;
        }
        return containsChecked(entity);
    }

    @Override
    public boolean isDisable(int position) {
        T entity = getItem(position);
        if (entity == null) {
            return false;
        }
        return containsDisable(entity);
    }

    @Override
    public void setChecked(int position, boolean isChecked) {
        T entity = getItem(position);
        if (entity == null) {
            return;
        }
        if (isChecked) {
            if (!containsDisable(entity)) {
                if (mCheckModel == CHECK_MODEL_SINGLE) {
                    mCheckedList.clear();
                }
                mCheckedList.add(this.getItem(position));
            }
        } else {
            mCheckedList.remove(this.getItem(position));
        }
    }

    @Override
    public boolean isAllEnable() {
        for (T entity : mAllPageDataList) {
            if (entity == null) {
                continue;
            }
            if (!containsDisable(entity)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isAllChecked() {
        if (mCheckModel == CHECK_MODEL_SINGLE) {
            if (!mCheckedList.isEmpty()) {
                return true;
            } else {
                return false;
            }
        } else {
            for (T entity : mAllPageDataList) {
                if (entity == null) {
                    continue;
                }
                if (!containsDisable(entity) && !containsChecked(entity)) {
                    return false;
                }
            }
            return true;
        }

    }

    @Override
    public void setAllChecked(boolean isChecked) {
        this.clearAllChecked();
        for (int i = 0; i < this.mAllPageDataList.size(); i++) {
            this.setChecked(i, isChecked);
        }
    }

    @Override
    public void setCheckedEntityList(List<T> list) {
        this.mOriginalCheckedList = list;
        mCheckedList.clear();
        for (int i = 0; i < this.mAllPageDataList.size(); i++) {
            if (contains(this.getItem(i), list)) {
                mCheckedList.add(this.getItem(i));
            }
        }
    }

    @Override
    public void setDisableEntityList(List<T> list) {
        this.mDisabledList = list;
        mDisabledList.clear();
        for (int i = 0; i < this.mAllPageDataList.size(); i++) {
            if (contains(this.getItem(i), list)) {
                mDisabledList.add(this.getItem(i));
            }
        }
    }

    public List<T> getCheckedEntityList() {
        return getCheckedEntityList(false);
    }

    @Override
    public List<T> getCheckedEntityList(boolean appendDisableEntity) {
        if (appendDisableEntity) {
            List<T> list = new ArrayList<>();
            list.addAll(mCheckedList);
            list.addAll(mDisabledList);
            return list;
        }
        return mCheckedList;
    }

    private boolean containsDisable(T entity) {
        return this.mDisabledList.contains(entity);
    }

    private boolean containsOriginalChecked(T entity) {
        return mOriginalCheckedList.contains(entity);
    }

    private boolean containsChecked(T entity) {
        return this.mCheckedList.contains(entity);
    }

    private boolean contains(T entity, List<T> list) {
        for (T t : list) {
            if (mEquals.equals(t, entity)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<T> getAddedEntityList() {
        List<T> addEntityList = new ArrayList<>();
        for (T entity : mCheckedList) {
            if (!containsOriginalChecked(entity)) {
                addEntityList.add(entity);
            }
        }
        return addEntityList;
    }

    @Override
    public List<T> getRemovedEntityList() {
        List<T> removeEntityList = new ArrayList<T>();
        for (T entity : mOriginalCheckedList) {
            if (!containsChecked(entity)) {
                removeEntityList.add(entity);
            }
        }
        return removeEntityList;
    }

    @Override
    public int getCheckEntityCount(boolean appendDisableEntity) {
        return this.getCheckedEntityList(appendDisableEntity).size();
    }

    @Override
    public void setPageData(List<T> pageData) {
        this.setAllPageDataList(pageData);
    }
}
