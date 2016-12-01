package cn.yhq.page.sample;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.yhq.adapter.core.ViewHolder;
import cn.yhq.adapter.expand.ChildItemViewProvider2;
import cn.yhq.adapter.expand.GroupItemViewProvider2;
import cn.yhq.page.adapter.PageExpandableListAdapter;
import cn.yhq.page.core.IPageAdapter;
import cn.yhq.page.core.IPageDataParser;
import cn.yhq.page.core.IPageRequester;
import cn.yhq.page.core.Page;
import cn.yhq.page.core.PageAction;
import cn.yhq.page.core.PageRequester;
import cn.yhq.page.sample.entity1.Data;
import cn.yhq.page.sample.entity1.ParentData;
import cn.yhq.page.sample.entity1.Response;
import cn.yhq.page.ui.PageActivity;

/**
 * Created by Yanghuiqiang on 2016/11/30.
 */

public class TestActivity extends PageActivity<Response, ParentData> {
    private ExpandableListView mPageView;
    private PageExpandableListAdapter<ParentData, Data> mPageAdapter;

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_swipe_layout;
    }

    @Override
    protected void onViewCreated(Bundle savedInstanceState) {
        super.onViewCreated(savedInstanceState);
        this.mPageView = this.getView(R.id.list_view);
        mPageAdapter = new PageExpandableListAdapter<ParentData, Data>(getContext()) {

            @Override
            public Data getChild(ParentData group, int childPosition) {
                return group.getChildDatas().get(childPosition);
            }

            @Override
            public int getChildrenCount(ParentData group) {
                return group.getChildDatas().size();
            }
        };
        mPageAdapter.register(new GroupItemViewProvider2<ParentData>() {
            @Override
            public void setupView(ViewHolder viewHolder, int position, ParentData entity, boolean isExpanded) {
                viewHolder
                        .setText(android.R.id.text1, entity.getTitle())
                        .setTextColor(android.R.id.text1, R.color.colorPrimary);
            }

            @Override
            public int getItemViewLayoutId() {
                return android.R.layout.simple_list_item_1;
            }

            @Override
            public boolean isForProvider(int position, ParentData entity) {
                return true;
            }
        });

        mPageAdapter.register(new ChildItemViewProvider2<ParentData, Data>() {
            @Override
            public int getItemViewLayoutId() {
                return android.R.layout.simple_list_item_1;
            }

            @Override
            public void setupView(ViewHolder viewHolder, int groupPosition, ParentData groupEntity, int childPosition, Data childEntity) {
                viewHolder.setText(android.R.id.text1, childEntity.getProductName());
            }

            @Override
            public boolean isForProvider(int groupPosition, ParentData groupEntity, int childPosition, Data childEntity) {
                return true;
            }
        });
        this.mPageView.setAdapter(mPageAdapter);
        this.mPageView.setGroupIndicator(null);
        this.mPageView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });
    }

    @Override
    public void onPageLoadComplete(PageAction pageAction, boolean isFromCache, boolean isSuccess) {
        super.onPageLoadComplete(pageAction, isFromCache, isSuccess);
        for (int i = 0; i < this.mPageAdapter.getGroupCount(); i++) {
            this.mPageView.expandGroup(i);
        }
    }

    @Override
    public IPageAdapter<ParentData> getPageAdapter() {
        return mPageAdapter;
    }

    @Override
    public IPageRequester<Response, ParentData> getPageRequester() {
        return new PageRequester<Response, ParentData>(getContext()) {

            @Override
            public void onCancel() {

            }

            @Override
            public void executeRequest(Context context, PageAction pageAction, Page<ParentData> page) {
                String json = "{\n" +
                        "  \"appName\": \"h5\",\n" +
                        "  \"bizType\": \"M026\",\n" +
                        "  \"customerId\": \"100000405752\",\n" +
                        "  \"data\": [\n" +
                        "    {\n" +
                        "      \"activityFlag\": \"0\",\n" +
                        "      \"actualAmount\": 0,\n" +
                        "      \"amount\": 20000000,\n" +
                        "      \"expectProfitRate\": \"5\",\n" +
                        "      \"extraProfitRate\": \"0\",\n" +
                        "      \"maxAmount\": 50000,\n" +
                        "      \"minAmount\": 100,\n" +
                        "      \"onlineTime\": \"20161122051949\",\n" +
                        "      \"period\": 720,\n" +
                        "      \"periodType\": 0,\n" +
                        "      \"process\": \"0.0000\",\n" +
                        "      \"productCode\": \"103\",\n" +
                        "      \"productId\": 2871,\n" +
                        "      \"productName\": \"活期流程测试\",\n" +
                        "      \"productStatus\": 1,\n" +
                        "      \"profitType\": 1,\n" +
                        "      \"recommendFlag\": \"0\",\n" +
                        "      \"returnType\": \"99\",\n" +
                        "      \"totalInvestAmount\": 0,\n" +
                        "      \"visibleTerminal\": \"3,0,1,2\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"activityFlag\": \"0\",\n" +
                        "      \"actualAmount\": 500,\n" +
                        "      \"amount\": 2500000,\n" +
                        "      \"expectProfitRate\": \"9.2\",\n" +
                        "      \"extraProfitRate\": \"0\",\n" +
                        "      \"maxAmount\": 90000,\n" +
                        "      \"minAmount\": 100,\n" +
                        "      \"onlineTime\": \"20161116044241\",\n" +
                        "      \"period\": 90,\n" +
                        "      \"periodType\": 0,\n" +
                        "      \"process\": \"1.0000\",\n" +
                        "      \"productCode\": \"200\",\n" +
                        "      \"productId\": 2850,\n" +
                        "      \"productName\": \"mmm\",\n" +
                        "      \"productStatus\": 2,\n" +
                        "      \"profitType\": 1,\n" +
                        "      \"recommendFlag\": \"0\",\n" +
                        "      \"returnType\": \"01\",\n" +
                        "      \"totalInvestAmount\": 2500000,\n" +
                        "      \"visibleTerminal\": \"3,0,1,2\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"activityFlag\": \"0\",\n" +
                        "      \"actualAmount\": 600,\n" +
                        "      \"amount\": 30000,\n" +
                        "      \"expectProfitRate\": \"9\",\n" +
                        "      \"extraProfitRate\": \"0.29\",\n" +
                        "      \"maxAmount\": 10000,\n" +
                        "      \"minAmount\": 100,\n" +
                        "      \"onlineTime\": \"20161115110551\",\n" +
                        "      \"period\": 5,\n" +
                        "      \"periodType\": 0,\n" +
                        "      \"process\": \"0.0200\",\n" +
                        "      \"productCode\": \"201\",\n" +
                        "      \"productId\": 2835,\n" +
                        "      \"productName\": \"新手04\",\n" +
                        "      \"productStatus\": 2,\n" +
                        "      \"profitType\": 1,\n" +
                        "      \"recommendFlag\": \"0\",\n" +
                        "      \"returnType\": \"01\",\n" +
                        "      \"totalInvestAmount\": 600,\n" +
                        "      \"visibleTerminal\": \"3,0,1,2\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"activityFlag\": \"1\",\n" +
                        "      \"actualAmount\": 90000,\n" +
                        "      \"amount\": 90000,\n" +
                        "      \"expectProfitRate\": \"9.2\",\n" +
                        "      \"extraProfitRate\": \"0.6\",\n" +
                        "      \"maxAmount\": 10000,\n" +
                        "      \"minAmount\": 200,\n" +
                        "      \"onlineTime\": \"20161115115008\",\n" +
                        "      \"period\": 10,\n" +
                        "      \"periodType\": 0,\n" +
                        "      \"process\": \"1.0000\",\n" +
                        "      \"productCode\": \"200\",\n" +
                        "      \"productId\": 2836,\n" +
                        "      \"productName\": \"活动标01\",\n" +
                        "      \"productStatus\": 2,\n" +
                        "      \"profitType\": 1,\n" +
                        "      \"recommendFlag\": \"0\",\n" +
                        "      \"returnType\": \"01\",\n" +
                        "      \"totalInvestAmount\": 90000,\n" +
                        "      \"visibleTerminal\": \"3,0,1,2\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"activityFlag\": \"0\",\n" +
                        "      \"actualAmount\": 0,\n" +
                        "      \"amount\": 80000,\n" +
                        "      \"expectProfitRate\": \"7.02\",\n" +
                        "      \"extraProfitRate\": \"0\",\n" +
                        "      \"maxAmount\": 1000,\n" +
                        "      \"minAmount\": 100,\n" +
                        "      \"onlineTime\": \"20161115115431\",\n" +
                        "      \"period\": 15,\n" +
                        "      \"periodType\": 0,\n" +
                        "      \"process\": \"0.0000\",\n" +
                        "      \"productCode\": \"200\",\n" +
                        "      \"productId\": 2837,\n" +
                        "      \"productName\": \"推荐标\",\n" +
                        "      \"productStatus\": 2,\n" +
                        "      \"profitType\": 1,\n" +
                        "      \"recommendFlag\": \"1\",\n" +
                        "      \"returnType\": \"01\",\n" +
                        "      \"totalInvestAmount\": 0,\n" +
                        "      \"visibleTerminal\": \"3,0,1,2\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"activityFlag\": \"0\",\n" +
                        "      \"actualAmount\": 26600,\n" +
                        "      \"amount\": 100000,\n" +
                        "      \"expectProfitRate\": \"5.5\",\n" +
                        "      \"extraProfitRate\": \"0\",\n" +
                        "      \"maxAmount\": 10000,\n" +
                        "      \"minAmount\": 100,\n" +
                        "      \"onlineTime\": \"20161114120000\",\n" +
                        "      \"period\": 30,\n" +
                        "      \"periodType\": 0,\n" +
                        "      \"process\": \"0.2660\",\n" +
                        "      \"productCode\": \"200\",\n" +
                        "      \"productId\": 2828,\n" +
                        "      \"productName\": \"蘑菇普通1\",\n" +
                        "      \"productStatus\": 2,\n" +
                        "      \"profitType\": 1,\n" +
                        "      \"recommendFlag\": \"0\",\n" +
                        "      \"returnType\": \"04\",\n" +
                        "      \"totalInvestAmount\": 26600,\n" +
                        "      \"visibleTerminal\": \"3,0,1,2\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"activityFlag\": \"0\",\n" +
                        "      \"actualAmount\": 2005000,\n" +
                        "      \"amount\": 6000000,\n" +
                        "      \"expectProfitRate\": \"8.32\",\n" +
                        "      \"extraProfitRate\": \"0\",\n" +
                        "      \"maxAmount\": 2000000,\n" +
                        "      \"minAmount\": 100,\n" +
                        "      \"onlineTime\": \"20161108120000\",\n" +
                        "      \"period\": 90,\n" +
                        "      \"periodType\": 0,\n" +
                        "      \"process\": \"0.3341\",\n" +
                        "      \"productCode\": \"200\",\n" +
                        "      \"productId\": 2790,\n" +
                        "      \"productName\": \"使用活动红包专用\",\n" +
                        "      \"productStatus\": 2,\n" +
                        "      \"profitType\": 1,\n" +
                        "      \"recommendFlag\": \"0\",\n" +
                        "      \"returnType\": \"01\",\n" +
                        "      \"totalInvestAmount\": 2005000,\n" +
                        "      \"visibleTerminal\": \"3,0,1,2\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"activityFlag\": \"1\",\n" +
                        "      \"actualAmount\": 0,\n" +
                        "      \"amount\": 90000,\n" +
                        "      \"distId\": \"20161111\",\n" +
                        "      \"expectProfitRate\": \"9.1\",\n" +
                        "      \"extraProfitRate\": \"0.1\",\n" +
                        "      \"maxAmount\": 10000,\n" +
                        "      \"minAmount\": 100,\n" +
                        "      \"onlineTime\": \"20161108120000\",\n" +
                        "      \"period\": 88,\n" +
                        "      \"periodType\": 0,\n" +
                        "      \"process\": \"0.0000\",\n" +
                        "      \"productCode\": \"200\",\n" +
                        "      \"productId\": 2789,\n" +
                        "      \"productName\": \"双11活动03\",\n" +
                        "      \"productStatus\": 2,\n" +
                        "      \"profitType\": 1,\n" +
                        "      \"recommendFlag\": \"0\",\n" +
                        "      \"returnType\": \"01\",\n" +
                        "      \"totalInvestAmount\": 0,\n" +
                        "      \"visibleTerminal\": \"3,0,1,2\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"activityFlag\": \"0\",\n" +
                        "      \"actualAmount\": 600,\n" +
                        "      \"amount\": 200000,\n" +
                        "      \"expectProfitRate\": \"9\",\n" +
                        "      \"extraProfitRate\": \"1\",\n" +
                        "      \"maxAmount\": 10000,\n" +
                        "      \"minAmount\": 100,\n" +
                        "      \"onlineTime\": \"20161109120000\",\n" +
                        "      \"period\": 3,\n" +
                        "      \"periodType\": 0,\n" +
                        "      \"process\": \"0.0030\",\n" +
                        "      \"productCode\": \"201\",\n" +
                        "      \"productId\": 2797,\n" +
                        "      \"productName\": \"蘑菇1新手标\",\n" +
                        "      \"productStatus\": 2,\n" +
                        "      \"profitType\": 1,\n" +
                        "      \"recommendFlag\": \"0\",\n" +
                        "      \"returnType\": \"01\",\n" +
                        "      \"totalInvestAmount\": 600,\n" +
                        "      \"visibleTerminal\": \"3,0,1,2\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"activityFlag\": \"0\",\n" +
                        "      \"actualAmount\": 19400,\n" +
                        "      \"amount\": 20000,\n" +
                        "      \"expectProfitRate\": \"6.8\",\n" +
                        "      \"extraProfitRate\": \"0\",\n" +
                        "      \"maxAmount\": 20000,\n" +
                        "      \"minAmount\": 200,\n" +
                        "      \"onlineTime\": \"20161109120000\",\n" +
                        "      \"period\": 30,\n" +
                        "      \"periodType\": 0,\n" +
                        "      \"process\": \"0.9700\",\n" +
                        "      \"productCode\": \"200\",\n" +
                        "      \"productId\": 2803,\n" +
                        "      \"productName\": \"蘑菇推荐标1\",\n" +
                        "      \"productStatus\": 2,\n" +
                        "      \"profitType\": 1,\n" +
                        "      \"recommendFlag\": \"1\",\n" +
                        "      \"returnType\": \"01\",\n" +
                        "      \"totalInvestAmount\": 19400,\n" +
                        "      \"visibleTerminal\": \"3,0,1,2\"\n" +
                        "    }\n" +
                        "  ],\n" +
                        "  \"deviceId\": \"\",\n" +
                        "  \"responseCode\": \"0000\",\n" +
                        "  \"responseTime\": \"20161201152524\"\n" +
                        "}";
                this.callNetworkResponse(new Gson().fromJson(json, Response.class));
            }
        };
    }

    @Override
    public IPageDataParser<Response, ParentData> getPageDataParser() {
        return new IPageDataParser<Response, ParentData>() {
            private List<ParentData> list;

            @Override
            public List<ParentData> getPageList(Response data, boolean isFromCache) {
                if (list == null) {
                    return new ArrayList<>();
                }
                return list;
            }

            @Override
            public long getPageTotal(Response data, boolean isFromCache) {
                list = new ArrayList<>();
                Map<String, ParentData> tempMapper = new HashMap<>();
                List<Data> datas = data.getData();
                for (Data entity : datas) {
                    String productCode = entity.getProductCode();
                    ParentData parentData = tempMapper.get(productCode);
                    if (parentData == null) {
                        parentData = new ParentData();
                        String title = "";
                        if ("103".equals(productCode)) {
                            title = "新手标的";
                        } else if ("200".equals(productCode)) {
                            title = "全民钱包";
                        } else if ("201".equals(productCode)) {
                            title = "全民安选";
                        }
                        parentData.setTitle(title);
                        parentData.setChildDatas(new ArrayList<Data>());
                        tempMapper.put(productCode, parentData);
                        list.add(parentData);
                    }
                    parentData.getChildDatas().add(entity);
                }
                return list.size();
            }
        };
    }

    @Override
    public View getPageView() {
        return this.mPageView;
    }
}
