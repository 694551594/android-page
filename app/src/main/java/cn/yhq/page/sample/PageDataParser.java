package cn.yhq.page.sample;

import java.util.List;

import cn.yhq.page.core.IPageDataParser;
import cn.yhq.page.sample.entity.AlbumInfo;
import cn.yhq.page.sample.entity.Tracks;

/**
 * Created by Yanghuiqiang on 2016/10/18.
 */

public class PageDataParser implements IPageDataParser<AlbumInfo, Tracks> {

    @Override
    public List<Tracks> getPageList(AlbumInfo data, boolean isFromCache) {
        return data.getTracks();
    }

    @Override
    public long getPageTotal(AlbumInfo data, boolean isFromCache) {
        return data.getTotal_tracks();
    }
}
