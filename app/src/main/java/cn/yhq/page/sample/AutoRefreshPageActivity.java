package cn.yhq.page.sample;

import android.os.Bundle;
import android.view.View;

import cn.yhq.http.core.ICall;
import cn.yhq.page.core.IPageAdapter;
import cn.yhq.page.core.IPageDataParser;
import cn.yhq.page.http.RetrofitPageActivity;
import cn.yhq.page.sample.entity.AlbumInfo;
import cn.yhq.page.sample.entity.Tracks;
import cn.yhq.page.ui.PageConfig;
import cn.yhq.widget.AutoRefreshListView;

/**
 * Created by Yanghuiqiang on 2016/10/12.
 */

public class AutoRefreshPageActivity extends RetrofitPageActivity<AlbumInfo, Tracks> {
    private AutoRefreshListView mListView;
    private AlbumPageAdapter mPageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_auto_refresh;
    }

    @Override
    public void onViewCreated(Bundle savedInstanceState) {
        super.onViewCreated(savedInstanceState);
        mListView = (AutoRefreshListView) this.findViewById(R.id.list_view);
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
        // 设置分页大小
        pageConfig.setPageSize(12);
    }

    @Override
    public ICall<AlbumInfo> executePageRequest(int pageSize, int currentPage, Tracks mData) {
        return HttpAPIClient.getAPI().getAlbumInfo("ldh", pageSize, currentPage);
    }

    @Override
    public IPageAdapter<Tracks> getPageAdapter() {
        return mPageAdapter;
    }

    @Override
    public IPageDataParser<AlbumInfo, Tracks> getPageDataParser() {
        return new PageDataParser();
    }
}
