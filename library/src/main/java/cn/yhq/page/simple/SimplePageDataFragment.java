package cn.yhq.page.simple;

import android.os.Bundle;

import java.util.List;

import cn.yhq.page.core.IPageDataParser;
import cn.yhq.page.ui.PageDataFragment;

/**
 * 
 * @author Yanghuiqiang 2015-5-27
 *
 * @param <I>
 */
public abstract class SimplePageDataFragment<I> extends PageDataFragment<List<I>, I> {

  private SimplePageDataParser mSimplePageDataParser;

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    mSimplePageDataParser = new SimplePageDataParser();
    super.onActivityCreated(savedInstanceState);
  }

  @Override
  public IPageDataParser<List<I>, I> getPageDataParser() {
    return mSimplePageDataParser;
  }

}
