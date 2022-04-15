package bitchat.android.com.bitstore.bean;

import java.io.Serializable;
import java.util.List;

public class CommentBean implements Serializable {


    private int commentid;
    private String appid;
    private String model;
    private int praisecount;
    private String account;
    private String commenttext;
    private long ctime;
    private long utime;
    private String id;

    public int getCommentid() {
        return commentid;
    }

    public void setCommentid(int commentid) {
        this.commentid = commentid;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getPraisecount() {
        return praisecount;
    }

    public void setPraisecount(int praisecount) {
        this.praisecount = praisecount;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getCommenttext() {
        return commenttext;
    }

    public void setCommenttext(String commenttext) {
        this.commenttext = commenttext;
    }

    public long getCtime() {
        return ctime;
    }

    public void setCtime(int ctime) {
        this.ctime = ctime;
    }

    public long getUtime() {
        return utime;
    }

    public void setUtime(int utime) {
        this.utime = utime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
