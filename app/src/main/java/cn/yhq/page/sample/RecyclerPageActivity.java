package cn.yhq.page.sample;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.EditText;

import cn.yhq.adapter.recycler.OnRecyclerViewItemClickListener;
import cn.yhq.http.core.ICall;
import cn.yhq.page.core.DefaultPageSearcher;
import cn.yhq.page.core.IPageAdapter;
import cn.yhq.page.core.IPageDataParser;
import cn.yhq.page.http.RetrofitPageActivity;
import cn.yhq.page.sample.entity.AlbumInfo;
import cn.yhq.page.sample.entity.Tracks;
import cn.yhq.page.ui.PageConfig;
import cn.yhq.widget.xrecyclerview.XRecyclerListView;

/**
 * Created by yanghuijuan on 2017/1/31.
 */

public class RecyclerPageActivity extends RetrofitPageActivity<AlbumInfo, Tracks> {
    private XRecyclerListView mListView;
    private AlbumRecyclerPageAdapter mPageAdapter;
    private EditText mEditText;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_recycler;
    }

    @Override
    public void onViewCreated(Bundle savedInstanceState) {
        mListView = this.getView(R.id.list_view);
        mPageAdapter = new AlbumRecyclerPageAdapter(getContext());
        mListView.setAdapter(mPageAdapter);
        mListView.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onRecyclerViewItemClick(View itemView, int position) {
                showToast("哈哈" + position);

            }
        });
        mListView.setLayoutManager(new LinearLayoutManager(this));
        mEditText = this.getView(R.id.editText);
        this.attachSearchEditText(mEditText);
        this.setPageSearcher(new DefaultPageSearcher<Tracks>(this) {
            @Override
            public String getFilterName(Tracks entity) {
                return entity.getTitle();
            }
        });
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
