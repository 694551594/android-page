package cn.yhq.page.sample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import cn.yhq.dialog.core.IDialog;
import cn.yhq.page.core.IPageAdapter;
import cn.yhq.page.simple.SimplePageActivity;

public class MainActivity extends SimplePageActivity<String> {
    private ListView mListView;
    private SimplePageAdapter mPageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setSwipeBackWrapper(false);
        super.onCreate(savedInstanceState);
        HttpAPIClient.init(this);
    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public IDialog createDialog(int id, Bundle args) {
        switch (id) {
            case 1:
                return new AlbumPageDialog3(this).create();
            case 2:
                return new AlbumPageDialog1(this).create();
            case 3:
                return new AlbumPageDialog2(this).create();
        }
        return null;
    }

    @Override
    public void onViewCreated(Bundle savedInstanceState) {
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        mListView = (ListView) this.findViewById(R.id.list_view);
        mPageAdapter = new SimplePageAdapter(this);
        mListView.setAdapter(mPageAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = null;
                switch (position) {
                    case 0:
                        intent = new Intent(MainActivity.this, SimplePageActivity1.class);
                        break;
                    case 1:
                        intent = new Intent(MainActivity.this, SimplePageActivity2.class);
                        break;
                    case 2:
                        intent = new Intent(MainActivity.this, NetworkPageActivity.class);
                        break;
                    case 3:
                        intent = new Intent(MainActivity.this, SwipeRefreshLayoutPageActivity.class);
                        break;
                    case 4:
                        intent = new Intent(MainActivity.this, OkHttpPageActivity.class);
                        break;
                    case 5:
                        intent = new Intent(MainActivity.this, AutoRefreshPageActivity.class);
                        break;
                    case 6:
                        showDialogFragment(1);
                        return;
                    case 7:
                        showDialogFragment(2);
                        return;
                    case 8:
                        showDialogFragment(3);
                        return;

                }
                startActivity(intent);
            }
        });
    }

    @Override
    public View getPageView() {
        return mListView;
    }

    @Override
    public IPageAdapter<String> getPageAdapter() {
        return mPageAdapter;
    }

    // 如果是非耗时操作，则可以直接返回要适配的数据
    @Override
    public List<String> getPageData() {
        List<String> data = new ArrayList<>();
        data.add("非耗时返回本地数据");
        data.add("耗时返回本地数据");
        data.add("耗时返回网络数据");
        data.add("自定义下拉刷新控件返回网络数据");
        data.add("自定义网络请求框架返回网络数据");
        data.add("类似微信、QQ拉到头部自动刷新的listview");
        data.add("耗时返回本地数据对话框");
        data.add("耗时返回网络数据对话框");
        data.add("自定义网络请求框架返回网络数据对话框");
        return data;
    }

}
