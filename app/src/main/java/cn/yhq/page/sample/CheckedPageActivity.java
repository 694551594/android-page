package cn.yhq.page.sample;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import java.util.List;

import cn.yhq.adapter.recycler.OnRecyclerViewItemClickListener;
import cn.yhq.http.core.ICall;
import cn.yhq.page.core.IEquals;
import cn.yhq.page.core.IPageAdapter;
import cn.yhq.page.core.IPageDataParser;
import cn.yhq.page.core.OnPageCheckedChangeListener;
import cn.yhq.page.core.PageChecker;
import cn.yhq.page.http.RetrofitPageCheckedActivity;
import cn.yhq.page.sample.entity.AlbumInfo;
import cn.yhq.page.sample.entity.Tracks;
import cn.yhq.page.ui.PageConfig;
import cn.yhq.widget.xrecyclerview.XRecyclerListView;

/**
 * Created by yanghuijuan on 2017/1/31.
 */

public class CheckedPageActivity extends RetrofitPageCheckedActivity<AlbumInfo, Tracks> {
    private XRecyclerListView mListView;
    private AlbumCheckedPageAdapter mPageAdapter;
    private Button mOKButton;
    private CheckBox mAllCheckButton;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_chooser;
    }

    @Override
    public void onViewCreated(Bundle savedInstanceState) {
        mAllCheckButton = this.getView(R.id.cb_choose_side);
        mOKButton = this.getView(R.id.btn_choose);
        mListView = this.getView(R.id.list_view);
        mPageAdapter = new AlbumCheckedPageAdapter(getContext());
        mListView.setAdapter(mPageAdapter);
        this.setPageChecker(PageChecker.CHECK_MODEL_MUTIPLE, new IEquals<Tracks>() {
            @Override
            public boolean equals(Tracks t1, Tracks t2) {
                return t1.getId() == t2.getId();
            }
        }, new OnPageCheckedChangeListener<Tracks>() {
            @Override
            public void onPageCheckedChanged(List<Tracks> checkedList, int count) {
                mPageAdapter.notifyDataSetChanged();
                mAllCheckButton.setChecked(isAllChecked());
                mOKButton.setText("选择(" + count + ")");
            }
        });
        mListView.setOnRecyclerViewItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onRecyclerViewItemClick(View itemView, int position) {
                toggleChecked(position);
            }
        });
        mAllCheckButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAllChecked(mAllCheckButton.isChecked());
            }
        });
        mOKButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Tracks> list = getCheckedEntityList(false);
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
