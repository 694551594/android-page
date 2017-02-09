package cn.yhq.page.core;

import java.io.Serializable;

/**
 * 分页信息
 *
 * @param <T>
 * @author Yanghuiqiang 2015-5-24
 */
public final class Page<T> implements Serializable {
    // 数据大小
    long dataSize;
    // 一页的大小
    int pageSize = 20;
    // 页数
    int pageCount;
    // 当前页号
    int currentPage = 1;
    // 附带数据
    T mData;

    void init() {
        int count = (int) (this.dataSize / this.pageSize);
        if (this.dataSize % this.pageSize == 0) {
            pageCount = count;
        } else {
            pageCount = count + 1;
        }
        mData = null;
    }

    void reset() {
        currentPage = 1;
    }

    void next() {
        currentPage++;
        if (currentPage >= pageCount) {
            currentPage = pageCount;
        }
    }

    void previous() {
        currentPage--;
        if (currentPage <= 0) {
            currentPage = 0;
        }
    }

    /**
     * 是否有下一页
     *
     * @return
     */
    public boolean haveNextPage() {
        if (currentPage >= pageCount) {
            return false;
        }
        return true;
    }

    public long getDataSize() {
        return dataSize;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getPageCount() {
        return pageCount;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public T getData() {
        return mData;
    }

    public Page clone() {
        Page page = new Page();
        page.currentPage = currentPage;
        page.dataSize = dataSize;
        page.mData = mData;
        page.pageCount = pageCount;
        page.pageSize = pageSize;
        return page;
    }
}
