package bitchat.android.com.bitstore.bean;

import com.google.gson.annotations.SerializedName;

public class CommitAppBean {


    private String account;
    private String developer="";//developer
    private String source="android"; //source
    private int categary=0;//categary
    @SerializedName("appsize")
    private String size="";//size
    private String version="0.0.0";//version
    private String appname="";//app name
    private String appicon="";
   // private String update="";//update ---> describe
    private String describe="";

    private String tag="";//tag
    private String associate="";
    private String introduce;//introduce img
    private String appname_en;
    private String lang;
    private String sign;
    private String apphash;
    private String iconhash;

    private int status;




    private String packagename;

    public String getDefaultlang() {
        return defaultlang;
    }





    private String defaultlang;


    private String appname_zh;



    private String downloadurl;



    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getDeveloper() {
        return developer;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getCategary() {
        return categary;
    }

    public void setCategary(int categary) {
        this.categary = categary;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public String getAppicon() {
        return appicon;
    }

    public void setAppicon(String appicon) {
        this.appicon = appicon;
    }

    public String getUpdate() {
        return describe;
    }

    public void setUpdate(String update) {
        this.describe = update;
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
        return downloadurl;
    }

    public void setDownloadurl(String downloadurl) {
        this.downloadurl = downloadurl;
    }



    public String getAppname_en() {
        return appname_en;
    }

    public void setAppname_en(String appname_en) {
        this.appname_en = appname_en;
    }

    public String getAppname_zh() {
        return appname_zh;
    }

    public void setAppname_zh(String appname_zh) {
        this.appname_zh = appname_zh;
    }


    public void setDefaultlang(String defaultlang) {
        this.defaultlang = defaultlang;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getPackagename() {
        return packagename;
    }

    public void setPackagename(String packagename) {
        this.packagename = packagename;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }


    public String getApphash() {
        return apphash;
    }

    public void setApphash(String apphash) {
        this.apphash = apphash;
    }

    public String getIconhash() {
        return iconhash;
    }

    public void setIconhash(String iconhash) {
        this.iconhash = iconhash;
    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


}
