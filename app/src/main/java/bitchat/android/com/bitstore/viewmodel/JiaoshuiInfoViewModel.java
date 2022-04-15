package bitchat.android.com.bitstore.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import bitchat.android.com.bitstore.bean.*;
import bitchat.android.com.bitstore.utils.FileUtils;
import bitchat.android.com.bitstore.utils.UploadUtils;
import com.android.base.mvvm.viewmodel.BaseViewModel;
import com.android.base.net.helper.LzyResponse;
import com.android.mine_android.api.Api;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;
import java.util.Map;

public class JiaoshuiInfoViewModel extends BaseViewModel {


    private MutableLiveData<AppDetailBean> appDetail;

    private MutableLiveData<AppInfoBean> appinfo;

    private MutableLiveData<List<UploadListBean>> applist;

    private MutableLiveData<List<FileBean>> localapps;

    private MutableLiveData<List<FileBean>> localgzs;

    private MutableLiveData<UploadBean> appicon;

    private MutableLiveData<Object> commitapp;

    private MutableLiveData<Object> drawdownapp;

    private MutableLiveData<Object> updateapp;

    private MutableLiveData<List<JiaoshuiBean>> jiaoshuilist;


    private MutableLiveData<DrawAppBean> deljiaoshui;

    private MutableLiveData<DrawAppBean> addjiaoshui;



    public JiaoshuiInfoViewModel(@NotNull Application application) {
        super(application);
    }


    public MutableLiveData<Object> onUpdateApp(){  //VerifyCodeBean
        if(updateapp==null){
            updateapp=new MutableLiveData<>();
        }
        return updateapp;
    }

    public MutableLiveData<UploadBean> onUploadIcon(){  //VerifyCodeBean
        if(appicon==null){
            appicon=new MutableLiveData<>();
        }
        return appicon;
    }


    public MutableLiveData<DrawAppBean> onAddJiaoshui(){  //VerifyCodeBean
        if(addjiaoshui==null){
            addjiaoshui=new MutableLiveData<>();
        }
        return addjiaoshui;
    }


    public MutableLiveData<List<JiaoshuiBean>> onJiaoshuiList(){  //VerifyCodeBean
        if(jiaoshuilist==null){
            jiaoshuilist=new MutableLiveData<>();
        }
        return jiaoshuilist;
    }


    public MutableLiveData<DrawAppBean> onDelJiaoshui(){  //VerifyCodeBean
        if(deljiaoshui==null){
            deljiaoshui=new MutableLiveData<>();
        }
        return deljiaoshui;
    }


    public MutableLiveData<Object> onCommitAppInfo(){
        if(commitapp==null){
            commitapp=new MutableLiveData<>();
        }
        return commitapp;
    }


    public MutableLiveData<Object> onDrawdownApp(){
        if(drawdownapp==null){
            drawdownapp=new MutableLiveData<>();
        }
        return drawdownapp;
    }


    public MutableLiveData<AppDetailBean> onAppDetail(){  //VerifyCodeBean
        if(appDetail==null){
            appDetail=new MutableLiveData<>();
        }
        return appDetail;
    }


    public MutableLiveData<List<FileBean>> onLocalapp(){  //VerifyCodeBean
        if(localapps==null){
            localapps=new MutableLiveData<>();
        }
        return localapps;
    }


    public MutableLiveData<List<FileBean>> onLocalGz(){  //VerifyCodeBean
        if(localgzs==null){
            localgzs=new MutableLiveData<>();
        }
        return localgzs;
    }


    public MutableLiveData<AppInfoBean> onAppInfo(){  //info
        if(appinfo==null){
            appinfo=new MutableLiveData<>();
        }
        return appinfo;
    }


    public MutableLiveData<List<UploadListBean>> onuploadApplist(){  //info
        if(applist==null){
            applist=new MutableLiveData<>();
        }
        return applist;
    }




    @SuppressLint("CheckResult")
    public void uploadAppIcon(AppCompatActivity activity,String url, List<File> files,UploadUtils.OkGoCallBack callBack){
        getProgress().postValue(true);
        UploadUtils.upLoadFiles(activity,url,files,callBack);

    }



    @SuppressLint("CheckResult")
    public void getJiaoshuiList(Map<String,Object> param){
        getProgress().postValue(true);
        setDisposable(Api.Companion.getJiaoshuiList(param).subscribe(this::JiaoshuiListResult,this::onReqFail));

    }

    @SuppressLint("CheckResult")
    public void addJiaoshui(String json){
        getProgress().postValue(true);
        setDisposable(Api.Companion.addJiaoshui(json).subscribe(this::AddJiaoshuiResult,this::onReqFail));

    }


    @SuppressLint("CheckResult")
    public void delJiaoshui(Map<String,Object> param){
        getProgress().postValue(true);
        setDisposable(Api.Companion.delJiaoshui(param).subscribe(this::delJiaoshuiResult,this::onReqFail));

    }


    @SuppressLint("CheckResult")
    public void uploadAppIcon(Map<String,Object> param, List<File> files){
        getProgress().postValue(true);
        setDisposable(Api.Companion.uploadAppFile(param,files).subscribe(this::onAppIconUpload,this::onReqFail));

    }


    @SuppressLint("CheckResult")
    public void commitAppInfo(String json){
        getProgress().postValue(true);
        setDisposable(Api.Companion.commitApp2Store(json).subscribe(this::onCommitAppInfoResult,this::onReqFail));

    }




    @SuppressLint("CheckResult")
    public void getLocalApps(Context context){
        getProgress().postValue(true);
        FileUtils.getLocalApps(context)
        .subscribe(this::ongetLocalAppSuccess,this::onReqFail);
    }


    @SuppressLint("CheckResult")
    public void getLocalGzs(Context context){
        getProgress().postValue(true);
        FileUtils.getLocalGzs(context)
                .subscribe(this::ongetLocalGzSuccess,this::onReqFail);
    }

    @SuppressLint("CheckResult")
    public void getApplist(Map<String,Object> Param){
        setDisposable(Api.Companion.getSelfApplist(Param).subscribe(this::onApplist,this::onReqFail));
    }



    @SuppressLint("CheckResult")
    public void getAppDetail(Map<String,Object> Param){
        setDisposable(Api.Companion.getAppDetail(Param).subscribe(this::onBannerSuccess,this::onReqFail));
    }


    @SuppressLint("CheckResult")
    public void getAppInfo(Map<String,Object> Param){
        getProgress().postValue(true);
        setDisposable(Api.Companion.getAppInfo(Param).subscribe(this::onAppInfoSuccess,this::onReqFail));
    }



    private void ongetLocalAppSuccess(List<FileBean> bean){
        getProgress().postValue(false);
        if(bean!=null&&bean.size()>0){
            onLocalapp().postValue(bean);}
    }

    private void ongetLocalGzSuccess(List<FileBean> bean){
        getProgress().postValue(false);
        if(bean!=null&&bean.size()>0){
            onLocalGz().postValue(bean);}
    }


    private void onBannerSuccess(LzyResponse<AppDetailBean> bean){
        if(bean!=null&&bean.getData()!=null){
            onAppDetail().postValue(bean.getData());}
    }


    private void onApplist(LzyResponse<List<UploadListBean>> bean){
        if(bean!=null&&bean.getData()!=null){
            onuploadApplist().postValue(bean.getData());}
    }


    private void onAppIconUpload(LzyResponse<UploadBean> bean){
        getProgress().postValue(false);
        if(bean!=null&&bean.getData()!=null){
            onUploadIcon().postValue(bean.getData());}
    }

    private void onCommitAppInfoResult(LzyResponse<Object> bean){
        getProgress().postValue(false);
        if(bean!=null&&bean.getData()!=null){
            onCommitAppInfo().postValue(bean.getData());}
    }

    private void onAppInfoSuccess(LzyResponse<AppInfoBean> bean){
        getProgress().postValue(false);
        if(bean!=null&&bean.getData()!=null){
            onAppInfo().postValue(bean.getData());}
    }


    private void delJiaoshuiResult(LzyResponse<DrawAppBean> bean){
        getProgress().postValue(false);
        if(bean!=null&&bean.getResult()!=null){
            onDelJiaoshui().postValue(bean.getResult());}
    }


    private void JiaoshuiListResult(LzyResponse<List<JiaoshuiBean>> bean){
        getProgress().postValue(false);
        if(bean!=null&&bean.getData()!=null){
            onJiaoshuiList().postValue(bean.getData());} }


    private void AddJiaoshuiResult(LzyResponse<DrawAppBean> bean){
        getProgress().postValue(false);
        if(bean!=null&&bean.getData()!=null){
            onAddJiaoshui().postValue(bean.getData());} }


    private void checkResult(LzyResponse<Object> bean){
        if(bean!=null&&bean.getData()!=null){
            onDrawdownApp().postValue(bean.getData());}
    }

    private void onReqFail(Throwable e){
        getProgress().postValue(false);
        onError(e);
    }




}
