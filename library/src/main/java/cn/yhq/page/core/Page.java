package cn.yhq.page.core;

import java.io.Serializable;

/**
 * 分页信息
 * 
 * @author Yanghuiqiang 2015-5-24
 * 
 * @param <T>
 */
public final class Page<T> implements Serializable {
  // 数据大小
  public long dataSize;
  // 一页的大小
  public int pageSize = 20;
  // 页数
  public int pageCount;
  // 当前页号
  public int currentPage = 1;
  // 附带数据
  public T mData;

  public void init() {
    int count = (int) (this.dataSize / this.pageSize);
    if (this.dataSize % this.pageSize == 0) {
      pageCount = count;
    } else {
      pageCount = count + 1;
    }
    mData = null;
  }

  public void reset() {
    currentPage = 1;
  }

  public void next() {
    currentPage++;
    if (currentPage >= pageCount) {
      currentPage = pageCount;
    }
  }

  public void previous() {
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

}
