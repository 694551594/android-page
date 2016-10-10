package cn.yhq.page.core;


import android.util.Log;

public class Debug {
  public static void debug(String TAG, String msg) {
    Log.i(TAG, msg);
  }

  public static void debug(String TAG, Throwable t) {
    Log.e(TAG, t.getLocalizedMessage(), t);
  }
}
