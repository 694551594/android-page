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
    private OnPageCheckedChangeListener<T> mOnPageCheckedChangeListener;
    private OnPageCheckedInitListener<T> mOnPageCheckedInitListener;

    public final static int CHECK_MODEL_SINGLE = 1;
    public final static int CHECK_MODEL_MUTIPLE = 2;

    public int mCheckModel = CHECK_MODEL_MUTIPLE;

    private OnPageCheckedEquals<T> mEquals;

    public PageChecker(int type, OnPageCheckedInitListener<T> listener) {
        this(type, new OnPageCheckedEquals<T>() {
            @Override
            public boolean equals(T t1, T t2) {
                return t1 == t2;
            }
        }, listener);
    }

    public PageChecker(int type, OnPageCheckedEquals<T> equals, OnPageCheckedInitListener<T> listener) {
        this.mCheckModel = type;
        this.mEquals = equals;
        this.mOnPageCheckedInitListener = listener;
        mCheckedList = new ArrayList<>();
        mDisabledList = new ArrayList<>();
        mAllPageDataList = new ArrayList<>();
        mOriginalCheckedList = new ArrayList<>();
    }

    public void setOnCheckedChangeListener(OnPageCheckedChangeListener<T> mOnPageCheckedChangeListener) {
        this.mOnPageCheckedChangeListener = mOnPageCheckedChangeListener;
    }

    private void listener() {
        if (mOnPageCheckedChangeListener != null) {
            mOnPageCheckedChangeListener.onPageCheckedChanged(getCheckedEntityList(false), getCheckEntityCount(false));
        }
    }

    @Override
    public void clearAllChecked() {
        mCheckedList.clear();
        this.listener();
    }

    private T getItem(int index) {
        return mAllPageDataList.get(index);
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
        this.listener();
    }

    @Override
    public boolean isAllEnable() {
        for (T entity : mAllPageDataList) {
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
        return contains(entity, mDisabledList);
    }

    private boolean containsOriginalChecked(T entity) {
        return contains(entity, mOriginalCheckedList);
    }

    private boolean containsChecked(T entity) {
        return contains(entity, mCheckedList);
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
        mCheckedList.clear();
        mDisabledList.clear();
        mOriginalCheckedList.clear();
        mAllPageDataList.clear();
        appendPageData(pageData);
    }

    @Override
    public void appendPageData(List<T> pageData) {
        int size = this.mAllPageDataList.size();
        this.mAllPageDataList.addAll(pageData);
        for (int i = size; i < mAllPageDataList.size(); i++) {
            T entity = mAllPageDataList.get(i);
            if (mOnPageCheckedInitListener.isChecked(i, entity)) {
                this.setChecked(i, true);
                mOriginalCheckedList.add(entity);
            }
            if (!mOnPageCheckedInitListener.isEnable(i, entity)) {
                this.mDisabledList.add(entity);
            }
        }

        this.listener();
    }

    @Override
    public List<T> getDisabledEntityList() {
        return mDisabledList;
    }
}
