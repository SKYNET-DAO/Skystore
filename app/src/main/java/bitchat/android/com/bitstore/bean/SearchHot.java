package bitchat.android.com.bitstore.bean;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;
import java.util.List;

public class SearchHot extends LitePalSupport implements Serializable {

    private String showtype;
    private List<HomeDataBean.DatasBean.AppsBean> datas;

    public SearchHot(String showtype, List<HomeDataBean.DatasBean.AppsBean> datas) {
        this.showtype = showtype;
        this.datas = datas;
    }

    public String getShowtype() {
        return showtype;
    }

    public void setShowtype(String showtype) {
        this.showtype = showtype;
    }

    public List<HomeDataBean.DatasBean.AppsBean> getData() {
        return datas;
    }

    public void setData(List<HomeDataBean.DatasBean.AppsBean> history) {
        this.datas = history;
    }
}
