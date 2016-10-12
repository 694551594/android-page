package cn.yhq.page.sample;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.List;

import cn.yhq.page.core.IPageAdapter;
import cn.yhq.page.core.IPageDataParser;
import cn.yhq.page.http.RetrofitPageDataActivity;
import cn.yhq.page.sample.entity.AlbumInfo;
import cn.yhq.page.sample.entity.Tracks;
import cn.yhq.page.ui.PageConfig;
import retrofit2.Call;

/**
 * Created by Yanghuiqiang on 2016/10/12.
 */

public class NetworkPageActivity extends RetrofitPageDataActivity<AlbumInfo, Tracks> {
    private ListView mListView;
    private AlbumPageAdapter mPageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(Bundle savedInstanceState) {
        setContentView(R.layout.activity_network_page);
        mListView = (ListView) this.findViewById(R.id.list_view);
        mPageAdapter = new AlbumPageAdapter(this);
        mListView.setAdapter(mPageAdapter);
    }

    @Override
    public View getPageView() {
        return mListView;
    }

    @Override
    public void onPageConfig(PageConfig pageConfig) {
        super.onPageConfig(pageConfig);
        pageConfig.setPageSize(5);
    }

    @Override
    public Call<AlbumInfo> executePageRequest(int pageSize, int currentPage, Tracks mData) {
        return HttpAPIClient.getAPI().getAlbumInfo("夜曲", pageSize, currentPage);
    }

    @Override
    public IPageAdapter<Tracks> getPageAdapter() {
        return mPageAdapter;
    }

    @Override
    public IPageDataParser<AlbumInfo, Tracks> getPageDataParser() {
        return new IPageDataParser<AlbumInfo, Tracks>() {

            @Override
            public List<Tracks> getPageList(AlbumInfo data, boolean isFromCache) {
                return data.getTracks();
            }

            @Override
            public long getPageTotal(AlbumInfo data, boolean isFromCache) {
                return data.getTotal_tracks();
            }
        };
    }
}
