package cn.yhq.page.sample;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import cn.yhq.dialog.core.IDialog;
import cn.yhq.http.core.ICall;
import cn.yhq.page.core.IPageAdapter;
import cn.yhq.page.core.IPageDataParser;
import cn.yhq.page.http.RetrofitPageDataDialog;
import cn.yhq.page.sample.entity.AlbumInfo;
import cn.yhq.page.sample.entity.Tracks;
import cn.yhq.page.ui.PageConfig;

/**
 * Created by Yanghuiqiang on 2016/10/21.
 */

public class AlbumPageDialog1 extends RetrofitPageDataDialog<AlbumInfo, Tracks> {
    private ListView mListView;
    private AlbumPageAdapter mPageAdapter;

    public AlbumPageDialog1(Context context) {
        super(context);
    }

    @Override
    protected IDialog onCreateDialog(Bundle args) {
        return this.getDefaultDialogBuilder().setTitle("搜索结果").create();
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
    public void onViewCreated() {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.activity_network_page, null, false);
        mListView = (ListView) v.findViewById(R.id.list_view);
        mPageAdapter = new AlbumPageAdapter(getContext());
        mListView.setAdapter(mPageAdapter);
    }

    @Override
    public ICall<AlbumInfo> executePageRequest(int pageSize, int currentPage, Tracks mData) {
        return HttpAPIClient.getAPI().getAlbumInfo("夜曲", pageSize, currentPage);
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
