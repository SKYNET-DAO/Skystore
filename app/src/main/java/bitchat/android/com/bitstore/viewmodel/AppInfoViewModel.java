package bitchat.android.com.bitstore.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import bitchat.android.com.bitstore.bean.*;
import bitchat.android.com.bitstore.utils.FileUtils;
import bitchat.android.com.bitstore.utils.UploadUtils;
import com.android.base.mvvm.viewmodel.BaseViewModel;
import com.android.base.net.helper.LzyResponse;
import com.android.mine_android.api.Api;
import com.orhanobut.logger.Logger;
import io.reactivex.functions.Consumer;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;
import java.util.Map;

public class AppInfoViewModel extends BaseViewModel {


    private MutableLiveData<AppDetailBean> appDetail;

    private MutableLiveData<AppInfoBean> appinfo;

    private MutableLiveData<List<UploadListBean>> applist;

    private MutableLiveData<List<FileBean>> localapps;

    private MutableLiveData<List<FileBean>> localgzs;

    private MutableLiveData<UploadBean> appicon;

    private MutableLiveData<Object> commitapp;

    private MutableLiveData<DrawAppBean> drawdownapp;

    private MutableLiveData<AppInfoBean> updateapp;



    public AppInfoViewModel(@NotNull Application application) {
        super(application);
    }


    public MutableLiveData<AppInfoBean> onUpdateApp(){  
        if(updateapp==null){
            updateapp=new MutableLiveData<>();
        }
        return updateapp;
    }

    public MutableLiveData<UploadBean> onUploadIcon(){  
        if(appicon==null){
            appicon=new MutableLiveData<>();
        }
        return appicon;
    }


    public MutableLiveData<Object> onCommitAppInfo(){
        if(commitapp==null){
            commitapp=new MutableLiveData<>();
        }
        return commitapp;
    }


    public MutableLiveData<DrawAppBean> onDrawdownApp(){
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
    public void checkAppVersion(Map<String,Object> param){
        setDisposable(Api.Companion.updateApp(param).subscribe(this::checkResult,this::onReqFail));

    }

    @SuppressLint("CheckResult")
    public void drawdownApp(Map<String,Object> param){
        getProgress().postValue(true);
        setDisposable(Api.Companion.drawdownApp(param).subscribe(this::drawdownResult,this::onReqFail));

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
        if(bean!=null&&bean.getResult()!=null){
            onAppDetail().postValue(bean.getResult());}
    }


    private void onApplist(LzyResponse<List<UploadListBean>> bean){
        if(bean!=null&&bean.getResult()!=null){
            onuploadApplist().postValue(bean.getResult());}
    }


    private void onAppIconUpload(LzyResponse<UploadBean> bean){
        getProgress().postValue(false);
        if(bean!=null&&bean.getData()!=null){
            onUploadIcon().postValue(bean.getData());}
    }

    private void onCommitAppInfoResult(LzyResponse<Object> bean){
        getProgress().postValue(false);
        if(bean!=null&&bean.getResult()!=null){
            onCommitAppInfo().postValue(1);}
    }

    private void onAppInfoSuccess(LzyResponse<AppInfoBean> bean){
        getProgress().postValue(false);
        if(bean!=null&&bean.getData()!=null){
            onAppInfo().postValue(bean.getData());}
    }


    private void drawdownResult(LzyResponse<DrawAppBean> bean){
        getProgress().postValue(false);
        if(bean!=null&&bean.getResult()!=null){
            onDrawdownApp().postValue(bean.getResult());}
    }

    private void checkResult(LzyResponse<AppInfoBean> bean){
        if(bean!=null&&bean.getData()!=null){
            onUpdateApp().postValue(bean.getData());}
    }

    private void onReqFail(Throwable e){
        getProgress().postValue(false);
        onError(e);
    }




}
