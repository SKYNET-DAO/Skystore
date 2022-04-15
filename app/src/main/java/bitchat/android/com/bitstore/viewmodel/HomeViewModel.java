package bitchat.android.com.bitstore.viewmodel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import bitchat.android.com.bitstore.R;
import bitchat.android.com.bitstore.bean.HomeDataBean;
import bitchat.android.com.bitstore.bean.HomeDataBean.DatasBean.AppsBean;
import bitchat.android.com.bitstore.bean.RandomIp;
import com.android.base.app.BaseApp;
import com.android.base.mvvm.viewmodel.BaseViewModel;
import com.android.base.net.helper.LzyResponse;
import com.android.mine_android.api.Api;
import com.orhanobut.logger.Logger;

import java.util.List;
import java.util.Map;


public class HomeViewModel extends BaseViewModel {

    private MutableLiveData<HomeDataBean> bannerData;

    private MutableLiveData<List<AppsBean>> homeLoadmoreMutableLiveData;

    private MutableLiveData<RandomIp> randomIP;
    private String[] Ips;
    private int index=0;


    public HomeViewModel(@NonNull Application application) {
        super(application);
        Logger.e("------init---->"+getClass().getSimpleName());
        Ips= BaseApp.getContext().getResources().getStringArray(R.array.main_channels);
    }

    public MutableLiveData<HomeDataBean> getBanner(){  //VerifyCodeBean
        if(bannerData==null){
            bannerData=new MutableLiveData<>();
        }
        return bannerData;
    }

    public MutableLiveData<List<AppsBean>> homeLoadmoreMutableLiveData(){  //VerifyCodeBean
        if(homeLoadmoreMutableLiveData==null){
            homeLoadmoreMutableLiveData=new MutableLiveData<>();
        }
        return homeLoadmoreMutableLiveData;
    }

    public MutableLiveData<RandomIp> getRandomIP(){  //VerifyCodeBean
        if(randomIP==null){
            randomIP=new MutableLiveData<>();
        }
        return randomIP;
    }

    @SuppressLint("CheckResult")
    public void getBanner(Map<String,String> Param){
        setDisposable(Api.Companion.getBanner(Param).subscribe(this::onBannerSuccess,this::onReqFail));
    }

    public void getMoreData(Map<String,Object> Param){
        setDisposable(Api.Companion.getMoreData(Param).subscribe(this::onloadMoreSuccess,this::onReqFail));
    }

    @SuppressLint("CheckResult")
    public void getRandomIp(){
        getProgress().postValue(true);
        setDisposable(Api.Companion.getRandomIp().subscribe(this::onRandomSuccess,this::onRandomError));
    }
    @SuppressLint("CheckResult")
    public void getRandomIp(String url){
        getProgress().postValue(true);
        setDisposable(Api.Companion.getRandomIp(url).subscribe(this::onRandomSuccess,this::onRandomError));
    }

    private void onBannerSuccess(LzyResponse<HomeDataBean> bean){
        if(bean!=null&&bean.getResult()!=null){
            getBanner().postValue(bean.getResult());}
    }

    private void onRandomSuccess(LzyResponse<RandomIp> bean){
        getProgress().postValue(false);
        if(bean!=null&&bean.getData()!=null){
            getRandomIP().postValue(bean.getData());}
    }

    private void onloadMoreSuccess(LzyResponse<List<AppsBean>> bean){
        if(bean!=null&&bean.getResult()!=null){
            homeLoadmoreMutableLiveData().postValue(bean.getResult());}
    }

    private void onReqFail(Throwable e){
        getProgress().postValue(false);
        onError(e);
    }

    private void onRandomError(Throwable e){
        onError(e);
        //reset url to connect
        new Handler().postDelayed(() -> {
            index+=1;
            if(index>Ips.length-1)index=0;
            getRandomIp(Ips[index]);
        },5000);

    }
}
