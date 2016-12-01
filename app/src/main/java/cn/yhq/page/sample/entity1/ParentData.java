package cn.yhq.page.sample.entity1;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Yanghuiqiang on 2016/12/1.
 */

public class ParentData implements Serializable {
    private List<Data> childDatas;
    private String title;
    private int icon;

    public List<Data> getChildDatas() {
        return childDatas;
    }

    public void setChildDatas(List<Data> childDatas) {
        this.childDatas = childDatas;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
