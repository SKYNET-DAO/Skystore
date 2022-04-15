package bitchat.android.com.bitstore.bean;

import android.content.Context;
import bitchat.android.com.bitstore.utils.AppUtils;

import java.io.Serializable;

public class FileBean implements Serializable {


    public String path;

    public int iconId;

    public String title;
    public long size;
    public AppUtils.AppInfo appInfo;
    public boolean ischeck;

    public FileBean(Context context,String path, String title, long size, int iconId) {
        this.path = path;
        this.iconId = iconId;
        this.size=size;
        this.title=title;
        appInfo=AppUtils.apkInfo(path,context);
    }
}
