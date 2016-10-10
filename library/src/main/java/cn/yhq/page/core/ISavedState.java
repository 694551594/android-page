package cn.yhq.page.core;

import android.os.Bundle;

/**
 * 状态保存
 * 
 * @author Yanghuiqiang 2015-5-26
 *
 */
public interface ISavedState {
  Bundle onSaveInstanceState();

  void onRestoreInstanceState(Bundle state);
}
