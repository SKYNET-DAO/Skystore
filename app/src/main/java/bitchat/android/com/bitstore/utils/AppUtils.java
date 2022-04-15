package bitchat.android.com.bitstore.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import bitchat.android.com.bitstore.BuildConfig;
import bitchat.android.com.bitstore.adapter.HDownloadListAdapter;
import bitchat.android.com.bitstore.bean.HomeDataBean;
import bitchat.android.com.bitstore.listener.ListDownloadListener;
import com.android.base.net.AppConst;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okserver.OkDownload;
import com.lzy.okserver.download.DownloadListener;
import com.lzy.okserver.download.DownloadTask;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.Serializable;

public class AppUtils {


    public static synchronized String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static synchronized String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    public static synchronized int getVersionCode(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }



    public static synchronized String getPackageName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.packageName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    public static synchronized Bitmap getBitmap(Context context) {
        PackageManager packageManager = null;
        ApplicationInfo applicationInfo = null;
        try {
            packageManager = context.getApplicationContext()
                    .getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(
                    context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            applicationInfo = null;
        }
        Drawable d = packageManager.getApplicationIcon(applicationInfo); 
        BitmapDrawable bd = (BitmapDrawable) d;
        Bitmap bm = bd.getBitmap();
        return bm;
    }





    public static AppInfo apkInfo(String absPath, Context context) {

        PackageManager pm = context.getPackageManager();
        PackageInfo pkgInfo = pm.getPackageArchiveInfo(absPath, PackageManager.GET_ACTIVITIES);
        AppInfo info=new AppInfo();
        if (pkgInfo != null) {
            ApplicationInfo appInfo = pkgInfo.applicationInfo;


            appInfo.sourceDir = absPath;
            appInfo.publicSourceDir = absPath;
            String appName = pm.getApplicationLabel(appInfo).toString();
            String packageName = appInfo.packageName; 
            String version = pkgInfo.versionName; 

            Drawable icon1 = pm.getApplicationIcon(appInfo);
            Drawable icon2 = appInfo.loadIcon(pm);
            String pkgInfoStr = String.format("PackageName:%s, Vesion: %s, AppName: %s", packageName, version, appName);

            info.setAppname(appName);
            info.setAppversion(version);
            info.setIcon(icon1==null?icon2:icon1);
            info.setPackagename(packageName);
            return info;
          //  Log.i(TAG, String.format("PkgInfo: %s", pkgInfoStr));
        }
        return null;
    }




    public static void installApk(Context context, String apkPath) {
        if (context == null || TextUtils.isEmpty(apkPath)) {
            return;
        }


        File file = new File(apkPath);
        Intent intent = new Intent(Intent.ACTION_VIEW);


        if (Build.VERSION.SDK_INT >= 24) {
            Logger.e("-----AppUtils------>"+"7.0 above，installing apk...");
            //provider authorities
            Uri apkUri = FileProvider.getUriForFile(context, "bitchat.android.com.bitstore.fileprovider", file);
            //Granting Temporary Permissions to a URI
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {

            Logger.e("-----AppUtils------>"+"7.0 below，installing apk...");
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }

        context.startActivity(intent);

    }



    public static void StartDownload(String tag, HomeDataBean.DatasBean.AppsBean model, ListDownloadListener listener) {

        
        Logger.e("-------StartDownload-register-->"+tag);
        GetRequest<File> request = OkGo.<File>get(AppConst.BASE_URL+model.getDownloadurl());//
//                .headers("aaa", "111")//
//                .params("bbb", "222");

        
        DownloadTask task= OkDownload.request(tag, request)//
                .priority(1)//
                .extra1(model)//
                .save()//
                .register(listener);
        task.start();

    }



  public static class AppInfo implements Serializable {
        private Drawable icon;
        private String appname;
        private String appversion;
        private long size;

      private String packagename;

        public Drawable getIcon() {
            return icon;
        }

        public void setIcon(Drawable icon) {
            this.icon = icon;
        }

        public String getAppname() {
            return appname;
        }

        public void setAppname(String appname) {
            this.appname = appname;
        }

        public String getAppversion() {
            return appversion;
        }

        public void setAppversion(String appversion) {
            this.appversion = appversion;
        }

      public String getPackagename() {
          return packagename;
      }

      public void setPackagename(String packagename) {
          this.packagename = packagename;
      }
      public long getSize() {
          return size;
      }

      public void setSize(long size) {
          this.size = size;
      }
    }



}
