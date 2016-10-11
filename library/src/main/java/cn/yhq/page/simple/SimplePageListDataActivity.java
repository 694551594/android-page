package cn.yhq.page.simple;

import android.os.Bundle;

import java.util.List;

import cn.yhq.page.core.IPageDataParser;
import cn.yhq.page.ui.PageDataActivity;

/**
 * 
 * @author Yanghuiqiang 2015-5-27
 * 
 * @param <I>
 */
public abstract class SimplePageListDataActivity<I> extends PageDataActivity<List<I>, I> {
  private SimplePageDataParser mSimplePageDataParser;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    mSimplePageDataParser = new SimplePageDataParser();
    super.onCreate(savedInstanceState);
  }

  @Override
  public IPageDataParser<List<I>, I> getPageDataParser() {
    return mSimplePageDataParser;
  }
}
