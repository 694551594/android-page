package cn.yhq.page.ui;

import android.view.View;
import android.widget.AbsListView;

import java.util.HashMap;
import java.util.Map;

import cn.yhq.widget.xrecyclerview.BaseRecyclerView;

/**
 * Created by Administrator on 2017/3/27.
 */

public final class PageViewHandlerFactory {

    private final static Map<Class<? extends View>, Class<? extends IPageViewHandler<?>>> handlers = new HashMap<>();

    static {
        register(AbsListView.class, PageListViewHandler.class);
        register(BaseRecyclerView.class, PageRecyclerViewHandler.class);
    }

    public static IPageViewHandler createPageViewHandler(View pageView) {
        IPageViewHandler handler = createPageViewHandler(pageView.getClass());
        if (handler == null) {
            return new DefaultPageViewHandler();
        }
        return handler;
    }

    public static IPageViewHandler createPageViewHandler(Class<? extends View> clazz) {
        try {
            Class<? extends IPageViewHandler<?>> handler = handlers.get(clazz);
            if (handler != null) {
                return handler.newInstance();
            }
            Class<?> superClass = clazz.getSuperclass();
            if (superClass == Object.class) {
                return null;
            }
            return createPageViewHandler((Class<? extends View>) superClass);
        } catch (Exception e) {
            return null;
        }
    }

    public static void register(Class<? extends View> viewClass, Class<? extends IPageViewHandler<?>> handlerClass) {
        handlers.put(viewClass, handlerClass);
    }

}
