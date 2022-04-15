package bitchat.android.com.bitstore.bean;

import com.android.base.net.AppConst;

import java.io.Serializable;
import java.util.List;


public class AppDetailBean implements Serializable {



    private String _id;
    private String account;
    private String lang;
    private String appname;
    private String version;
    private String appsize;
    private int categary;
    private String appicon;
    private String source;
    private String developer;
    private String describe;
    private String tag;
    private String associate;
    private String introduce;
    private String downloadurl;
    private long c_time;
    private long u_time;
    private int commentcount;
    private int downcount;
    private int recommendlevel;
    private String id;
    private List<?> release;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getAppsize() {
        return appsize;
    }

    public void setAppsize(String appsize) {
        this.appsize = appsize;
    }

    public int getCategary() {
        return categary;
    }

    public void setCategary(int categary) {
        this.categary = categary;
    }

    public String getAppicon() {
        return AppConst.BASE_URL +appicon;
    }

    public void setAppicon(String appicon) {
        this.appicon = appicon;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDeveloper() {
        return developer;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getAssociate() {
        return associate;
    }

    public void setAssociate(String associate) {
        this.associate = associate;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getDownloadurl() {
        return AppConst.BASE_URL+downloadurl;
    }

    public void setDownloadurl(String downloadurl) {
        this.downloadurl = downloadurl;
    }

    public long getC_time() {
        return c_time;
    }

    public void setC_time(long c_time) {
        this.c_time = c_time;
    }

    public long getU_time() {
        return u_time;
    }

    public void setU_time(long u_time) {
        this.u_time = u_time;
    }

    public int getCommentcount() {
        return commentcount;
    }

    public void setCommentcount(int commentcount) {
        this.commentcount = commentcount;
    }

    public int getDowncount() {
        return downcount;
    }

    public void setDowncount(int downcount) {
        this.downcount = downcount;
    }

    public int getRecommendlevel() {
        return recommendlevel;
    }

    public void setRecommendlevel(int recommendlevel) {
        this.recommendlevel = recommendlevel;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<?> getRelease() {
        return release;
    }

    public void setRelease(List<?> release) {
        this.release = release;
    }
}
