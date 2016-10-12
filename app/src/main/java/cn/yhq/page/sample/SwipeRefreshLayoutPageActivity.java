package cn.yhq.page.sample;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.markmao.pulltorefresh.widget.XListView;

import java.util.List;

import cn.yhq.page.core.IPageAdapter;
import cn.yhq.page.core.IPageDataParser;
import cn.yhq.page.core.OnPullToRefreshProvider;
import cn.yhq.page.http.RetrofitPageDataActivity;
import cn.yhq.page.sample.entity.AlbumInfo;
import cn.yhq.page.sample.entity.Tracks;
import cn.yhq.page.ui.PageConfig;
import cn.yhq.page.ui.PullToRefreshSwipeLayoutListViewContext;
import retrofit2.Call;

/**
 * Created by Yanghuiqiang on 2016/10/12.
 */

public class SwipeRefreshLayoutPageActivity extends RetrofitPageDataActivity<AlbumInfo, Tracks> {
    private XListView mListView;
    private AlbumPageAdapter mPageAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(Bundle savedInstanceState) {
        setContentView(R.layout.activity_swipe_refresh);
        mListView = (XListView) this.findViewById(R.id.list_view);
        mPageAdapter = new AlbumPageAdapter(this);
        mListView.setAdapter(mPageAdapter);
        mSwipeRefreshLayout = (SwipeRefreshLayout) this.findViewById(R.id.swiperefreshlayout);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
    }

    @Override
    public OnPullToRefreshProvider getOnPullToRefreshProvider() {
        return new PullToRefreshSwipeLayoutListViewContext(mSwipeRefreshLayout, mListView);
    }

    @Override
    public View getPageView() {
        return mListView;
    }

    @Override
    public void onPageConfig(PageConfig pageConfig) {
        super.onPageConfig(pageConfig);
        // 设置分页大小
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
