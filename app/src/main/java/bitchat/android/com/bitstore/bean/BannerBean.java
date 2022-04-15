package bitchat.android.com.bitstore.bean;

import com.android.base.net.AppConst;

public class BannerBean {

    public String getUrl() {
        return AppConst.BASE_URL +url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getImg() {
        return AppConst.BASE_URL+img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    private String url;

    private String img;
}
