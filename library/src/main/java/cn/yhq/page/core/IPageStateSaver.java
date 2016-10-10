package cn.yhq.page.core;

import android.os.Bundle;

/**
 * 分页内容的保存
 * 
 * @author Yanghuiqiang 2015-5-22
 * 
 */
public interface IPageStateSaver {
  /**
   * 保存
   * 
   * @param pageState
   */
  Bundle onSave(PageState pageState);

  /**
   * 恢复
   * 
   * @param savedInstanceState
   */
  PageState onRestore(Bundle savedInstanceState);

}
