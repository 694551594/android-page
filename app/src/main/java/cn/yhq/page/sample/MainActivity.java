package cn.yhq.page.sample;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.List;

import cn.yhq.dialog.core.IDialog;
import cn.yhq.page.simple.SimpleListViewPageActivity;

public class MainActivity extends SimpleListViewPageActivity<String> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HttpAPIClient.init(this);
    }

    @Override
    protected void onConfig(Config config) {
        super.onConfig(config);
        config.setSwipeBackWrapper(false);
    }

    @Override
    public IDialog createDialog(int id, Bundle args) {
        switch (id) {
            case 1:
                return new AlbumPageDialog4(this).create();
            case 2:
                return new AlbumPageDialog1(this).create();
            case 3:
                return new AlbumPageDialog2(this).create();
        }
        return null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        super.onItemClick(parent, view, position, id);
        switch (position) {
            case 0:
                startActivity(SimplePageActivity1.class);
                break;
            case 1:
                startActivity(SimplePageActivity2.class);
                break;
            case 2:
                startActivity(NetworkPageActivity.class);
                break;
            case 3:
                startActivity(SwipeRefreshLayoutPageActivity.class);
                break;
            case 4:
                startActivity(OkHttpPageActivity.class);
                break;
            case 5:
                startActivity(AutoRefreshPageActivity.class);
                break;
            case 6:
                showDialogFragment(1);
                break;
            case 7:
                showDialogFragment(2);
                break;
            case 8:
                showDialogFragment(3);
                break;
            case 9:
                startActivity(RecyclerPageActivity.class);
                break;
        }
    }

    @Override
    public void onViewCreated(Bundle savedInstanceState) {
        super.onViewCreated(savedInstanceState);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        this.setListAdapter(new SimplePageAdapter(this));
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
        data.add("swipeRefreshLayout");
        return data;
    }

}
