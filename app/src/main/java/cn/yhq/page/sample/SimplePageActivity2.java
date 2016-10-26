package cn.yhq.page.sample;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import cn.yhq.page.core.IPageAdapter;
import cn.yhq.page.core.IPageRequester;
import cn.yhq.page.core.Page;
import cn.yhq.page.core.PageAction;
import cn.yhq.page.core.PageRequester;
import cn.yhq.page.simple.SimplePageDataActivity;

/**
 * Created by Yanghuiqiang on 2016/10/12.
 */

public class SimplePageActivity2 extends SimplePageDataActivity<String> {
    private ListView mListView;
    private SimplePageAdapter mPageAdapter;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void onViewCreated(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        mListView = (ListView) this.findViewById(R.id.list_view);
        mPageAdapter = new SimplePageAdapter(this);
        mListView.setAdapter(mPageAdapter);
    }

    @Override
    public View getPageView() {
        return mListView;
    }

    @Override
    public IPageAdapter<String> getPageAdapter() {
        return mPageAdapter;
    }

    // 如果是非耗时操作，则不需要重写此方法
    // 如果是耗时操作，则需要重写
    @Override
    public IPageRequester<List<String>, String> getPageRequester() {
        return new PageRequester<List<String>, String>(this) {
            private Thread thread;

            @Override
            public void onCancel() {
                thread.interrupt();
            }

            @Override
            public void executeRequest(Context context, PageAction pageAction, Page<String> page) {
                final Handler handler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        switch (msg.what) {
                            case 1:
                                callCacheResponse((List<String>) msg.obj);
                                break;
                            case 2:
                                callNetworkResponse((List<String>) msg.obj);
                                break;
                        }
                    }
                };
                // 模拟耗时操作，1秒后获取缓存数据并返回，2秒后获取请求数据返回
                thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Message msg1 = new Message();
                        msg1.obj = getCacheData();
                        msg1.what = 1;
                        handler.sendMessage(msg1);
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Message msg2 = new Message();
                        msg2.obj = getNetworkData();
                        msg2.what = 2;
                        handler.sendMessage(msg2);
                    }
                });
                thread.start();
            }
        };
    }

    private List<String> getNetworkData() {
        List<String> data = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            data.add("请求条目" + i);
//        }
        return data;
    }

    private List<String> getCacheData() {
        List<String> data = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            data.add("缓存条目" + i);
        }
        return data;
    }

    @Override
    public List<String> getPageData() {
        return null;
    }

//    @Override
//    public IPageViewProvider getPageViewProvider() {
//        return new IPageViewProvider() {
//
//            @Override
//            public View getPageView() {
//                return SimplePageActivity2.this.getPageView();
//            }
//
//            @Override
//            public int getPageLoadingView() {
//                return R.layout.custom_loading_view;
//            }
//
//            @Override
//            public int getPageEmptyView() {
//                return R.layout.custom_empty_view;
//            }
//        };
//    }
}
