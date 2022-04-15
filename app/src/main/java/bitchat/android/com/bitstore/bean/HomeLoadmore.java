package bitchat.android.com.bitstore.bean;

import com.android.base.net.AppConst;

import java.io.Serializable;
import java.util.List;

public class HomeLoadmore implements Serializable {




    private String account;
    private String appname;
    private String version;
    private String appsize;
    private int categary;
    private String sign;
    private String lang;
    private String downloadurl;
    private String appicon;
    private long ctime;
    private long utime;
    private String id;
    private String status;
    private List<?> release;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
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

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getDownloadurl() {
       return AppConst.BASE_URL+downloadurl;
    }

    public void setDownloadurl(String downloadurl) {
        this.downloadurl = downloadurl;
    }

    public String getAppicon() {
        if(appicon.startsWith("https://"))
            return appicon;
        else
            return AppConst.BASE_URL +appicon;
    }

    public void setAppicon(String appicon) {
        this.appicon = appicon;


    }

    public long getCtime() {
        return ctime;
    }

    public void setCtime(long ctime) {
        this.ctime = ctime;
    }

    public long getUtime() {
        return utime;
    }

    public void setUtime(long utime) {
        this.utime = utime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<?> getRelease() {
        return release;
    }

    public void setRelease(List<?> release) {
        this.release = release;
    }
}
