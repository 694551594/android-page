package cn.yhq.page.ui;

import android.view.View;
import android.widget.ListView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/27.
 */

public class PageViewHandlerFactory {

    private final static Map<Class<? extends View>, Class<? extends IPageViewHandler<?>>> handlers = new HashMap<>();

    static {
        handlers.put(ListView.class, PageListViewHandler.class);
    }

    public static IPageViewHandler createPageViewHandler(View pageView) {
        Class<? extends IPageViewHandler<?>> handler = handlers.get(pageView.getClass());
        try {
            return handler.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void register(Class<? extends View> viewClass, Class<? extends IPageViewHandler<?>> handlerClass) {
        handlers.put(viewClass, handlerClass);
    }

}
