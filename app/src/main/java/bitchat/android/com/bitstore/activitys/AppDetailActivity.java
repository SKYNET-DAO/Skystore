package bitchat.android.com.bitstore.activitys;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import bitchat.android.com.bitstore.R;
import bitchat.android.com.bitstore.bean.HomeDataBean;
import bitchat.android.com.bitstore.fragments.CommentFragment;
import bitchat.android.com.bitstore.fragments.IntroducesFragment;
import bitchat.android.com.bitstore.fragments.RecommendFrament;
import bitchat.android.com.bitstore.listener.ListDownloadListener;
import bitchat.android.com.bitstore.utils.AppUtils;
import bitchat.android.com.bitstore.utils.GlideUtil;
import bitchat.android.com.bitstore.utils.ShareUtil;
import bitchat.android.com.bitstore.viewmodel.AppInfoViewModel;
import bitchat.android.com.bitstore.widget.ProgressButton;
import com.android.base.activitys.WfcBaseActivity;
import com.android.base.net.AppConst;
import com.android.base.utils.JsonUtil;
import com.flyco.tablayout.SegmentTabLayout;
import com.lzy.okserver.OkDownload;
import com.lzy.okserver.download.DownloadTask;
import com.orhanobut.logger.Logger;
import com.vondear.rxtool.RxAppTool;
import com.vondear.rxtool.RxFileTool;

import java.io.File;
import java.util.ArrayList;


public class AppDetailActivity extends WfcBaseActivity {


    private SegmentTabLayout tabs;
    private ArrayList<Fragment> fragments=new ArrayList<>();
    private TextView title;
    private String[] titles;
    private HomeDataBean.DatasBean.AppsBean appsBean;
    private String source="0";
    private ImageView appicon;
    private TextView appname;
    private TextView installcount;
    private LinearLayout buttom_comment;
    private DownloadTask task;
    private ProgressButton download;
    private View share;


    @Override
    protected void afterViews() {
        share=findViewById(R.id.share);
        title=findViewById(R.id.title);
        tabs=findViewById(R.id.segmenttablayout);
        appicon= findViewById(R.id.appicon);
        installcount= findViewById(R.id.installcount);
        appname=  findViewById(R.id.appname);
        buttom_comment= findViewById(R.id.buttom_comment);
        titles=new String[]{getString(R.string.str_introduce),getString(R.string.str_comment),getString(R.string.str_tuijian)};
        download=findViewById(R.id.download);

        share.setOnClickListener(v->{
            ShareUtil.share(this,AppConst.BASE_URL+appsBean.getDownloadurl());
        });
        buttom_comment.setOnClickListener(v->{
            if(tabs.getCurrentTab()!=1){
                tabs.setCurrentTab(1);
            }else{
                

            }

        });

        initData();
    }

    private void initData(){


        if(getIntent()!=null&&getIntent().getExtras()!=null){

            appsBean= (HomeDataBean.DatasBean.AppsBean) getIntent().getExtras().get("appsbean");
            source= (String) getIntent().getExtras().get("source");
            task=OkDownload.getInstance().getTask(appsBean.getAppid());
            if(task!=null){
                download.setText(R.string.str_open_app);
               if(!RxFileTool.isFileExists(task.progress.filePath)){
                   task.restart();
               }else{

                   AppUtils.AppInfo appInfo= AppUtils.apkInfo(task.progress.filePath,this);
                   download.setText(R.string.str_open_app);
                   download.setOnClickListener(v->{


                      

                       if(!RxAppTool.isInstallApp(this,appInfo.getPackagename())){

                           AppUtils.installApk(this,task.progress.filePath);

                       }else{
                           RxAppTool.launchApp(this,appInfo.getPackagename());
                       }


                   });

               }

            }else{
                download.setText(R.string.str_install);
                download.setOnClickListener(v->{
                    download.setTag(appsBean.getAppid());
                    Logger.e("------download--->"+ appsBean.getDownloadurl());
                    AppUtils.StartDownload(appsBean.getAppid(),appsBean, new ListDownloadListener(appsBean.getAppid(),download,true));

                });
            }
        }

        if(appsBean!=null){
            appname.setText(appsBean.getAppname());
            installcount.setText(Formatter.formatFileSize(this, Long.parseLong(appsBean.getAppsize())));

            Logger.e("-----appsBean.getAppicon()--->"+appsBean.getAppicon());
            if(appsBean.getAppicon().startsWith("http://")||appsBean.getAppicon().startsWith("https://")){
                GlideUtil.loadImageView(this,appsBean.getAppicon(),appicon);

            }else{
                GlideUtil.loadImageView(this, AppConst.BASE_URL +appsBean.getAppicon(),appicon);
            }


            fragments.add(new IntroducesFragment(appsBean.getAppid()));
            fragments.add(new CommentFragment(appsBean.getAppid()));
            fragments.add(new RecommendFrament());
            tabs.setTabData(titles,this, R.id.fragmentcontainer, fragments);

        }


    }



    @Override
    public boolean isAcceptAppManager() {
        return false;
    }

    @Override
    protected int contentLayout() {
        return R.layout.activity_app_detail;
    }
}
